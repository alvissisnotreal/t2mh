
package View.Customer;


public class productReviewHistory {
     String nameProduct,timePost,DatePostNew;
    int idReview;

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
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

    public productReviewHistory(String nameProduct, String timePost, String DatePostNew, int idReview) {
        this.nameProduct = nameProduct;
        this.timePost = timePost;
        this.DatePostNew = DatePostNew;
        this.idReview = idReview;
    }

    public productReviewHistory() {
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
    
}
