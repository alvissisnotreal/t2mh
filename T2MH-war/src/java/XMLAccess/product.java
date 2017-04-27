package XMLAccess;

public class product {

    private int id, count;
    private String avatar, category;

    public product() {
    }

    public product(int id, int count, String avatar, String category) {
        this.id = id;
        this.count = count;
        this.avatar = avatar;
        this.category = category;
    }

    public product(String category) {
        this.category = category;
    }
    
    public product(int id, int count) {
        this.id = id;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
