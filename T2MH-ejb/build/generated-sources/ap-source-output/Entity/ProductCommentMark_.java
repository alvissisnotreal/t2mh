package Entity;

import Entity.Customer;
import Entity.ProductCommentMarkPK;
import Entity.ProductReview;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-04-28T05:50:52")
@StaticMetamodel(ProductCommentMark.class)
public class ProductCommentMark_ { 

    public static volatile SingularAttribute<ProductCommentMark, ProductCommentMarkPK> productCommentMarkPK;
    public static volatile SingularAttribute<ProductCommentMark, Integer> stars;
    public static volatile SingularAttribute<ProductCommentMark, ProductReview> productReview;
    public static volatile SingularAttribute<ProductCommentMark, Customer> customer;

}