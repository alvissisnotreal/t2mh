/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Customer;

/**
 *
 * @author Son
 */
public class branchCommentHistory {
    String nameBranch,timePost;
    int idReview,idComment;

    public String getNameBranch() {
        return nameBranch;
    }

    public void setNameBranch(String nameBranch) {
        this.nameBranch = nameBranch;
    }

    public String getTimePost() {
        return timePost;
    }

    public void setTimePost(String timePost) {
        this.timePost = timePost;
    }

    public int getIdReview() {
        return idReview;
    }

    public void setIdReview(int idReview) {
        this.idReview = idReview;
    }

    public int getIdComment() {
        return idComment;
    }

    public void setIdComment(int idComment) {
        this.idComment = idComment;
    }

    public branchCommentHistory() {
    }

    public branchCommentHistory(String nameBranch, String timePost, int idReview, int idComment) {
        this.nameBranch = nameBranch;
        this.timePost = timePost;
        this.idReview = idReview;
        this.idComment = idComment;
    }
    
    
}
