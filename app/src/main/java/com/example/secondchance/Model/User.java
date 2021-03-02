package com.example.secondchance.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    private String userID;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String photoUrl;
    private Long lastUpdated;
    private String phone;


    public Map<String,Object> toMap(){
        HashMap<String,Object> result=new HashMap<>();
        result.put("userID",userID);
        result.put("firstName",firstName);
        result.put("lastName",lastName);
        result.put("password",password);
        result.put("email",email);
        result.put("photoUrl",photoUrl);
        result.put("lastUpdated", FieldValue.serverTimestamp());
        result.put("phone",phone);

        return result;
    }

    public void fromMap(Map<String,Object> map){

        userID=(String)map.get("userID");
        firstName=(String)map.get("firstName");
        lastName=(String)map.get("lastName");
        password=(String)map.get("password");
        email=(String)map.get("email");
        photoUrl=(String)map.get("photoUrl");
        Timestamp ts=(Timestamp)map.get("lastUpdated");
        if(ts==null)
            ts = Timestamp.now();
        lastUpdated = ts.getSeconds();
        phone=(String)map.get("phone");


    }

    @NonNull
    public String getUserID() { return userID; }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public String getPhone() { return phone;  }

    public void setPhone(String phone) { this.phone = phone; }

    public void setUserID(@NonNull String userID) {
        this.userID = userID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
