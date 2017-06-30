package hagai.edu.firebasedatabase.models;


import com.google.firebase.auth.FirebaseUser;

/**
 * A User Model Class
 * //We want to save objects to the database, but there are rules...
 * //1) must have an empty Constructor
 * //2) must have generated getters and setters
 *
 * //the getValue(User.class) requires that
 */

public class User {
    private String displayName;
    private String profileImage = "https://d1w2poirtb3as9.cloudfront.net/default.jpeg"; //Not all the users have profile images...
    private String uid;
    private String email;


    //Empty constructor:
    public User() {
        //Log.d("Ness", "Hi!");
    }

    //Constructor:
    public User(FirebaseUser user) {
        this.displayName = user.getDisplayName();
        if (user.getPhotoUrl()!= null)
            profileImage = user.getPhotoUrl().toString();

        this.uid = user.getUid();
        this.email = user.getEmail();
    }

    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getProfileImage() {
        return profileImage;
    }
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfileImage(String profileImage) {
        //We Hates Nulls...
        if (profileImage != null)
            this.profileImage = profileImage;
    }

    @Override
    public String toString() {
        return "User{" +
                "displayName='" + displayName + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
