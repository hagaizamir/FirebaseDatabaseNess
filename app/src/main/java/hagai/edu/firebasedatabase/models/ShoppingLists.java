package hagai.edu.firebasedatabase.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A Shopping List title and owner.
 * POJO:
 */
//we want to transfer the model in intents or newInstance -> Parcelable
public class ShoppingLists implements Parcelable {
    private String ownerUID;
    private String listUID;
    private String name;

    //POJO constructor (Used for Firebase data retrieval.) snapshot.getValue(ShoppingList.class)
    public ShoppingLists() {
    }

    //Constructor * The constructor that we use to construct objects.
    public ShoppingLists(String ownerUID, String listUID, String name) {
        this.ownerUID = ownerUID;
        this.listUID = listUID;
        this.name = name;
    }

    //Getters and Setters:
    public String getOwnerUID() {
        return ownerUID;
    }
    public void setOwnerUID(String ownerUID) {
        this.ownerUID = ownerUID;
    }
    public String getListUID() {
        return listUID;
    }
    public void setListUID(String listUID) {
        this.listUID = listUID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ShoppingLists{" +
                "ownerUID='" + ownerUID + '\'' +
                ", listUID='" + listUID + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ownerUID);
        dest.writeString(this.listUID);
        dest.writeString(this.name);
    }
    protected ShoppingLists(Parcel in) {
        this.ownerUID = in.readString();
        this.listUID = in.readString();
        this.name = in.readString();
    }
    public static final Parcelable.Creator<ShoppingLists> CREATOR = new Parcelable.Creator<ShoppingLists>() {
        @Override
        public ShoppingLists createFromParcel(Parcel source) {
            return new ShoppingLists(source);
        }

        @Override
        public ShoppingLists[] newArray(int size) {
            return new ShoppingLists[size];
        }
    };
}
