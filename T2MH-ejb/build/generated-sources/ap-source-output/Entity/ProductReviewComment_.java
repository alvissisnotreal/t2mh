package Entity;

import Entity.Customer;
import Entity.ProductReview;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-04-28T05:50:52")
@StaticMetamodel(ProductReviewComment.class)
public class ProductReviewComment_ { 

    public static volatile SingularAttribute<ProductReviewComment, String> timePost;
    public static volatile SingularAttribute<ProductReviewComment, String> comments;
    public static volatile SingularAttribute<ProductReviewComment, Integer> prcid;
    public static volatile SingularAttribute<ProductReviewComment, Customer> customerID;
    public static volatile SingularAttribute<ProductReviewComment, ProductReview> prid;

}