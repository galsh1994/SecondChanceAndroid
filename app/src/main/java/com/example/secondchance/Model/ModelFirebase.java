package com.example.secondchance.Model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ModelFirebase {

    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();


    public void registerAuthFB(User user, String password, Model.idSaverListener listener) {

        mAuth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            listener.onComplete(true, mAuth.getCurrentUser().getUid());
                            Log.d("fb", "p" + user.getUserID());
                            Model.instance.addUser(user, new Model.addUserListener() {
                                @Override
                                public void onComplete() {
                                    Log.d("fb", "p" + mAuth.getCurrentUser().getUid());
                                    Model.instance.refreshAllUsers(null);
                                }

                            });
                        } else {
                            if (!task.getException().equals(null)) {
                                Log.d("pp", "o" + task.getException().getMessage());
                            }
                            listener.onComplete(false, null);
                        }
                    }
                });
    }


    public void logInAuth(String email, String password, Model.idSaverListener listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            listener.onComplete(true, mAuth.getCurrentUser().getUid());
                        } else {
                            listener.onComplete(false, null);
                        }
                    }
                });
    }

    public void resetPass(String email, Model.SuccessListener listener) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    listener.onComplete(true);
                } else {
                    listener.onComplete(false);
                }
            }
        });
    }


    public void getAllUsers(Long lastUpdated, final Model.getAllUsersListener listener) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Timestamp ts = new Timestamp(lastUpdated, 0);
        db.collection("users").whereGreaterThanOrEqualTo("lastUpdated", ts).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<User> data = new LinkedList<User>();
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult()) {
                        User user = new User();
                        user.fromMap(doc.getData());
                        user.setUserID(doc.getId());
                        HashMap<String, Object> update = new HashMap<>();
                        update.put("userID", user.getUserID());
                        db.collection("users").document(user.getUserID()).update(update);
                        data.add(user);

                    }
                }
                listener.onComplete(data);
            }
        });

    }

    public void addUser(User user, final Model.addUserListener listener) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").add(user.toMap()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                listener.onComplete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onComplete();
            }
        });
    }


    public void updateUser(User user, final Model.UpdateUserListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getUserID()).set(user.toMap());
    }

    public void getUser(String id, final Model.GetUserListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User user = new User();
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {

                        user.fromMap(task.getResult().getData());
                    }
                }
                listener.onComplete(user);
            }
        });
    }

    public void deleteUser(User user, final Model.DeleteListener listener) {

        deleteUserPhoto(user.getEmail(),null);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getUserID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                HashMap<String, Object> deletedUser = new HashMap<>();
                deletedUser.put("deletedUserID", user.getUserID());
                deletedUser.put("lastDeleted", FieldValue.serverTimestamp());
                db.collection("deletedUsers").add(deletedUser).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        listener.onComplete();
                    }
                });

            }
        });

        mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Log.d("delete","success");
                else
                    Log.d("delete","fail");

            }
        });
    }


    public void uploadUserImage(Bitmap imageBmp, String name, final Model.UploadUserImageListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference imagesRef = storage.getReference().child("User images").child(name);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.onComplete(null);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUrl = uri;
                        listener.onComplete(downloadUrl.toString());
                    }
                });
            }
        });
    }



    public interface GetAllDeletedUsersListener extends Model.Listener<List<User>> {
    }

    public void getAllDeletedUsers(Long lastDeleted, GetAllDeletedUsersListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Timestamp ts = new Timestamp(lastDeleted, 0);
        db.collection("deletedUsers").whereGreaterThanOrEqualTo("lastDeleted", ts).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<User> data = new LinkedList<User>();
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult()) {
                        User user = new User();
                        user.setUserID((String) doc.getData().get(("deletedUserID")));
                        Timestamp ts = (Timestamp) doc.getData().get("lastDeleted");
                        user.setLastUpdated(ts.getSeconds());
                        data.add(user);

                    }
                }
                listener.onComplete(data);
            }
        });

    }

    public void getUserByEmail(String email, Model.GetUserListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        Task<QuerySnapshot> query = usersRef.whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                User user = new User();

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        user.fromMap(document.getData());
                        Log.d("msg", document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.d("msg", "Error getting documents: ", task.getException());
                }
                listener.onComplete(user);
            }
        });
    }
    //////////////////////////post section////////////////////////////////////////////////////////

    public void addPost(Post post, Model.addPostListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").add(post.toMap()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                listener.onComplete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onComplete();
            }
        });
    }

    public void updatePost(Post post, Model.UpdatePostListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").document(post.getPostID()).set(post.toMap());
    }

    public void getPost(String id, final Model.GetPostListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Post post = new Post();
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc != null) {
                        post.fromMap(task.getResult().getData());
                    }
                }
                listener.onComplete(post);
            }
        });
    }

    public void getAllPosts(Long lastUpdated, final Model.getAllPostsListener listener) {

        // need to use lastUpdated

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Timestamp ts = new Timestamp(lastUpdated, 0);
        db.collection("posts").whereGreaterThanOrEqualTo("lastUpdated", ts).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Post> data = new LinkedList<Post>();
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult()) {
                        Post post = new Post();
                        post.fromMap(doc.getData());
                        post.setPostID(doc.getId());
                        HashMap<String, Object> update = new HashMap<>();
                        update.put("postID", post.getPostID());
                        db.collection("posts").document(post.getPostID()).update(update);
                        data.add(post);

                    }
                }
                listener.onComplete(data);
            }
        });

    }


    public void deletePost(Post post, Model.DeleteListener listener) {
        String postStorageName= post.getCondition()+post.getDescription();
        deletePostPhoto(postStorageName,null);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").document(post.getPostID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                HashMap<String, Object> deletedPost = new HashMap<>();
                deletedPost.put("deletedPostID", post.getPostID());
                deletedPost.put("lastDeleted", FieldValue.serverTimestamp());
                db.collection("deletedPosts").add(deletedPost).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        listener.onComplete();
                    }
                });

            }
        });
    }

    public interface getAllPostsListener extends Model.Listener<List<Post>> {
    }

    public void getAllDeletedPosts(Long lastDeleted, getAllPostsListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Timestamp ts = new Timestamp(lastDeleted, 0);
        db.collection("deletedPosts").whereGreaterThanOrEqualTo("lastDeleted", ts).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Post> data = new LinkedList<Post>();
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult()) {
                        Post post = new Post();
                        post.setPostID((String) doc.getData().get(("deletedPostID")));
                        Timestamp ts = (Timestamp) doc.getData().get("lastDeleted");
                        post.setLastUpdated(ts.getSeconds());
                        data.add(post);

                    }
                }
                listener.onComplete(data);
            }
        });

    }

    public static void getLatLong(final Model.LatLongListener listener) {
        List<Double> latitudePoints = new LinkedList<Double>();
        List<Double> longitudePoints = new LinkedList<Double>();
        List<String> postID = new LinkedList<String>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot querySnapshot : task.getResult()) {
                        Post post = new Post();
                        post.fromMap(querySnapshot.getData());
                        latitudePoints.add(post.getCoordinatesLat());
                        longitudePoints.add(post.getCoordinatesLong());
                        Log.d("from modelFB= ", post.getCoordinatesLat().toString() + post.getCoordinatesLong().toString());
                        postID.add(post.getPostID());
                    }
                }
                listener.onComplete(latitudePoints, longitudePoints, postID);
            }
        });
    }

    public void uploadPostImage(Bitmap imageBmp, String name, final Model.UploadPostImageListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference imagesRef = storage.getReference().child("Posts images").child(name);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.onComplete(null);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUrl = uri;
                        listener.onComplete(downloadUrl.toString());
                    }
                });
            }
        });
    }

    public void deletePostPhoto(String postID, Model.DeletePostPhotoListener deleteListener) {
        final StorageReference imagesRef = storage.getReference().child("Posts images").child(postID);
        imagesRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful())
                    imagesRef.delete();
            }
        });
    }

    public void deleteUserPhoto(String userID, Model.DeleteUserPhotoListener deleteUserPhotoListener) {
        final StorageReference imagesRef = storage.getReference().child("User images").child(userID);
        imagesRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful())
                    imagesRef.delete();
            }
        });
    }



}