
package View.Customer;


public class branchReivewHistory {
    String namebranch,timePost,DatePostNew;
    int idReview;

    public String getNamebranch() {
        return namebranch;
    }

    public void setNamebranch(String namebranch) {
        this.namebranch = namebranch;
    }

    public String getTimePost() {
        return timePost;
    }

    public void setTimePost(String timePost) {
        this.timePost = timePost;
    }

    public String getDatePostNew() {
        return DatePostNew;
    }

    public void setDatePostNew(String DatePostNew) {
        this.DatePostNew = DatePostNew;
    }

    public int getIdReview() {
        return idReview;
    }

    public void setIdReview(int idReview) {
        this.idReview = idReview;
    }

    public branchReivewHistory(String namebranch, String timePost, String DatePostNew, int idReview) {
        this.namebranch = namebranch;
        this.timePost = timePost;
        this.DatePostNew = DatePostNew;
        this.idReview = idReview;
        
    }

    public branchReivewHistory() {
    }
   
}
