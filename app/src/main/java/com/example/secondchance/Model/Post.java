package com.example.secondchance.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Post {

    @PrimaryKey
    @NonNull
    private String postID;
    private String userID;
    private String photoUrl;
    private String description;
    private String location;
    private String condition;
    private Long lastUpdated;


    public Map<String,Object> toMap(){
        HashMap<String,Object> result=new HashMap<>();
        result.put("postID",postID);
        result.put("userID",userID);
        result.put("photoUrl",photoUrl);
        result.put("description",description);
        result.put("location",location);
        result.put("condition",condition);
        result.put("lastUpdated", FieldValue.serverTimestamp());
        return result;
    }

    public void fromMap(Map<String,Object> map){
        postID=(String)map.get("postID");
        userID=(String)map.get("userID");
        photoUrl=(String)map.get("photoUrl");
        description=(String)map.get("description");
        location=(String)map.get("location");
        condition=(String)map.get("condition");

//        Timestamp ts=(Timestamp)map.get("lastUpdated");
//        lastUpdated=ts.getSeconds();
    }

    @NonNull
    public String getPostID() {
        return postID;
    }

    public void setPostID(@NonNull String postID) {
        this.postID = postID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
