package Entity;

import Entity.Branch;
import Entity.BranchCommentMark;
import Entity.BranchReviewComment;
import Entity.Customer;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-04-28T05:50:52")
@StaticMetamodel(BranchReview.class)
public class BranchReview_ { 

    public static volatile CollectionAttribute<BranchReview, BranchReviewComment> branchReviewCommentCollection;
    public static volatile SingularAttribute<BranchReview, String> timePost;
    public static volatile SingularAttribute<BranchReview, Branch> branchID;
    public static volatile CollectionAttribute<BranchReview, BranchCommentMark> branchCommentMarkCollection;
    public static volatile SingularAttribute<BranchReview, String> comments;
    public static volatile SingularAttribute<BranchReview, Integer> brid;
    public static volatile SingularAttribute<BranchReview, Integer> bRStars;
    public static volatile SingularAttribute<BranchReview, Customer> customerID;

}