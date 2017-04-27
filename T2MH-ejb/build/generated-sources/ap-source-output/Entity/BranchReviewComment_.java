package Entity;

import Entity.BranchReview;
import Entity.Customer;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-04-28T05:50:52")
@StaticMetamodel(BranchReviewComment.class)
public class BranchReviewComment_ { 

    public static volatile SingularAttribute<BranchReviewComment, String> comments;
    public static volatile SingularAttribute<BranchReviewComment, Integer> brcid;
    public static volatile SingularAttribute<BranchReviewComment, BranchReview> brid;
    public static volatile SingularAttribute<BranchReviewComment, String> datePost;
    public static volatile SingularAttribute<BranchReviewComment, Customer> customerID;

}