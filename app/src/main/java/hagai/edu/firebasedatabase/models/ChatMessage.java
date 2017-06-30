package hagai.edu.firebasedatabase.models;

import org.joda.time.LocalDateTime;

/**
 * Model class
 * //follow the rules:
 */
//NO-SQL!!!
//Database... DataWareHouse
public class ChatMessage {
    private String message;
    private String profileImage;
    private String sender;
    private String time;

    //Constructor:
    public ChatMessage() {

    }
    //Constructor
    public ChatMessage(User user, String message) {
        this.message = message;
        this.profileImage = user.getProfileImage();
        this.sender = user.getDisplayName();
        //new Date();
        //Calendar.getInstance();
        this.time = LocalDateTime.now().toString();
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getProfileImage() {
        return profileImage;
    }
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "message='" + message + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", sender='" + sender + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
