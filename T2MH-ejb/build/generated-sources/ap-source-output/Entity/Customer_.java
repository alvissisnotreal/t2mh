package Entity;

import Entity.BranchCommentMark;
import Entity.BranchReview;
import Entity.BranchReviewComment;
import Entity.CustomerInfo;
import Entity.Feedback;
import Entity.Orders;
import Entity.ProductCommentMark;
import Entity.ProductReview;
import Entity.ProductReviewComment;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-04-28T05:50:52")
@StaticMetamodel(Customer.class)
public class Customer_ { 

    public static volatile CollectionAttribute<Customer, BranchReviewComment> branchReviewCommentCollection;
    public static volatile CollectionAttribute<Customer, ProductReviewComment> productReviewCommentCollection;
    public static volatile CollectionAttribute<Customer, Feedback> feedbackCollection;
    public static volatile CollectionAttribute<Customer, ProductCommentMark> productCommentMarkCollection;
    public static volatile SingularAttribute<Customer, CustomerInfo> customerInfo;
    public static volatile CollectionAttribute<Customer, Orders> ordersCollection;
    public static volatile CollectionAttribute<Customer, BranchReview> branchReviewCollection;
    public static volatile SingularAttribute<Customer, Integer> customerStatus;
    public static volatile CollectionAttribute<Customer, BranchCommentMark> branchCommentMarkCollection;
    public static volatile SingularAttribute<Customer, String> customerPassword;
    public static volatile SingularAttribute<Customer, Integer> customerID;
    public static volatile SingularAttribute<Customer, String> customerUsername;
    public static volatile CollectionAttribute<Customer, ProductReview> productReviewCollection;

}