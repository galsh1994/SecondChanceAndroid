package com.example.secondchance.Model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ModelFirebase {

 

    public void getAllUsers(Long lastUpdated, final Model.getAllUsersListener listener) {

         FirebaseFirestore db = FirebaseFirestore.getInstance();
        Timestamp ts=new Timestamp(lastUpdated,0);
        db.collection("users").whereGreaterThanOrEqualTo("lastUpdated",ts).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<User> data = new LinkedList<User>();
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult()) {
                       User user=new User();
                        user.fromMap(doc.getData());
                        user.setUserID(doc.getId());
                        db.collection("users").document(user.getUserID()).set(user.toMap());
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


        public void updateUser(User user, Model.addUserListener listener) {
           FirebaseFirestore db=FirebaseFirestore.getInstance();
           db.collection("users").document(user.getUserID()).set(user);
        }

        public void getUser(String id, final Model.GetUserListener listener) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    User user = new User();
                    if (task.isSuccessful()){
                        DocumentSnapshot doc = task.getResult();
                        if (doc != null) {
                            user.fromMap(task.getResult().getData());
                        }
                    }
                    listener.onComplete(user);
                }
            });
        }

        public void delete(User user, final Model.DeleteListener listener) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(user.getUserID())
                    .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    listener.onComplete();
                }
            });
        }


    public void uploadUserImage(Bitmap imageBmp, String name, final Model.UploadUserImageListener listener){
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


    //////////////////////////post section////////////////////////////////////////////////////////

    public void addPost(Post post, Model.addPostListener listener){
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

    public void updatePost(Post post, Model.addPostListener listener) {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("posts").document(post.getUserID()).set(post);
    }

    public void getPost(String id, final Model.GetPostListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Post post = null;
                if (task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if (doc != null) {
                        post = task.getResult().toObject(Post.class);
                    }
                }
                listener.onComplete(post);
            }
        });
    }

    public void getAllPosts(Long lastUpdated, final Model.getAllPostsListener listener) {

        // need to use lastUpdated

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Timestamp ts=new Timestamp(lastUpdated,0);
        db.collection("posts").whereGreaterThanOrEqualTo("lastUpdated",ts).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Post> data = new LinkedList<Post>();
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult()) {
                        Post post = new Post();
                        post.fromMap(doc.getData());
                        post.setPostID(doc.getId());
                        db.collection("posts").document(post.getPostID()).set(post.toMap());
                        data.add(post);

                    }
                }
                listener.onComplete(data);
            }
        });

    }
    public void uploadPostImage(Bitmap imageBmp, String name, final Model.UploadPostImageListener listener){
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


    public void deletePost(Post post, Model.DeleteListener listener){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("posts").document(post.getPostID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                HashMap<String,Object> deletedPost=new HashMap<>();
                deletedPost.put("deletedPostID",post.getPostID());
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

    public interface getAllPostsListener extends Model.Listener<List<Post>> {}

    public void getAllDeletedPosts(Long lastDeleted,getAllPostsListener listener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Timestamp ts=new Timestamp(lastDeleted,0);
        db.collection("deletedPosts").whereGreaterThanOrEqualTo("lastDeleted",ts).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Post> data = new LinkedList<Post>();
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult()) {
                        Post post = new Post();
                        post.setPostID((String) doc.getData().get(("deletedPostID")));
                        Timestamp ts=(Timestamp)doc.getData().get("lastUpdated");
                        post.setLastUpdated(ts.getSeconds());
                        data.add(post);

                    }
                }
                listener.onComplete(data);
            }
        });

    }

}

/*
    public void exampleToAcess() {


        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Alda");
        user.put("last", "Lovelace");
        user.put("born", 1815);
// if you want to make a shortcut , you can put a class (like student)
        //but it must be Serializable - getters & setters and public empty c'tor
// one more thing is to change the default id to real id,
//        lesson number 8 part b , 2:58

// Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });
    }
*/




