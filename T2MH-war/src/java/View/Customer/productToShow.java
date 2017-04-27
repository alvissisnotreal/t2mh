
package View.Customer;


public class productToShow {
    int id,count;
    String productName,productAvatar;
    double productPrice;
    int inventory;
    public productToShow() {
    }

    public productToShow(int id, String productName, String productAvatar, double productPrice) {
        this.id = id;
        this.productName = productName;
        this.productAvatar = productAvatar;
        this.productPrice = productPrice;
    }

    public productToShow(int id, int count, String productName, String productAvatar, double productPrice) {
        this.id = id;
        this.count = count;
        this.productName = productName;
        this.productAvatar = productAvatar;
        this.productPrice = productPrice;
    }

    public productToShow(int id, int count, String productName, String productAvatar, double productPrice, int inventory) {
        this.id = id;
        this.count = count;
        this.productName = productName;
        this.productAvatar = productAvatar;
        this.productPrice = productPrice;
        this.inventory = inventory;
    }
    
    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductAvatar() {
        return productAvatar;
    }

    public void setProductAvatar(String productAvatar) {
        this.productAvatar = productAvatar;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public productToShow(int id, String productName, double productPrice) {
        this.id = id;
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public productToShow(int id, int count, String productAvatar) {
        this.id = id;
        this.count = count;
        this.productAvatar = productAvatar;
    }
    
}
