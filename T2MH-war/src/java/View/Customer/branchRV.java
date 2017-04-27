
package View.Customer;


public class branchRV {
   String name,avatar,date,content; 
   int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public branchRV(String name, String avatar, String date, String content) {
        this.name = name;
        this.avatar = avatar;
        this.date = date;
        this.content = content;
    }

    public branchRV() {
    }

    public branchRV(String name, String avatar, String date, String content, int id) {
        this.name = name;
        this.avatar = avatar;
        this.date = date;
        this.content = content;
        this.id = id;
    }

    public branchRV(String name, String date, String content, int id) {
        this.name = name;
        this.date = date;
        this.content = content;
        this.id = id;
    }
   
}
