package hagai.edu.firebasedatabase.models;

/**
 * List Product POJO
 */

public class ShoppingListProduct {
    private String productName;
    private String ownerUID;
    private String ownerName;
    private boolean purchased;

    //Empty constructor
    public ShoppingListProduct() {
    }
    public ShoppingListProduct(String productName, String ownerUID, String ownerName, boolean purchased) {
        this.productName = productName;
        this.ownerUID = ownerUID;
        this.ownerName = ownerName;
        this.purchased = purchased;
    }

    //getters and setters
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getOwnerUID() {
        return ownerUID;
    }
    public void setOwnerUID(String ownerUID) {
        this.ownerUID = ownerUID;
    }
    public String getOwnerName() {
        return ownerName;
    }
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    public boolean isPurchased() {
        return purchased;
    }
    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    @Override
    public String toString() {
        return "ShoppingListProduct{" +
                "productName='" + productName + '\'' +
                ", ownerUID='" + ownerUID + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", purchased=" + purchased +
                '}';
    }
}
