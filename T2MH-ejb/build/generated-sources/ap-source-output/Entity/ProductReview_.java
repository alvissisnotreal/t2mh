package Entity;

import Entity.Customer;
import Entity.Product;
import Entity.ProductCommentMark;
import Entity.ProductReviewComment;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-04-28T05:50:52")
@StaticMetamodel(ProductReview.class)
public class ProductReview_ { 

    public static volatile SingularAttribute<ProductReview, Integer> pRStars;
    public static volatile SingularAttribute<ProductReview, String> timePost;
    public static volatile SingularAttribute<ProductReview, String> comments;
    public static volatile SingularAttribute<ProductReview, Product> productID;
    public static volatile CollectionAttribute<ProductReview, ProductReviewComment> productReviewCommentCollection;
    public static volatile CollectionAttribute<ProductReview, ProductCommentMark> productCommentMarkCollection;
    public static volatile SingularAttribute<ProductReview, Customer> customerID;
    public static volatile SingularAttribute<ProductReview, Integer> prid;

}