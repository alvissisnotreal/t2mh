package Entity;

import Entity.Branch;
import Entity.Groups;
import Entity.OrderInfo;
import Entity.ProductReview;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-04-28T05:50:52")
@StaticMetamodel(Product.class)
public class Product_ { 

    public static volatile SingularAttribute<Product, Branch> branchID;
    public static volatile SingularAttribute<Product, Integer> productID;
    public static volatile SingularAttribute<Product, Groups> groupID;
    public static volatile SingularAttribute<Product, Integer> productStatus;
    public static volatile SingularAttribute<Product, String> dateCreate;
    public static volatile SingularAttribute<Product, Integer> inventory;
    public static volatile SingularAttribute<Product, String> descriptions;
    public static volatile CollectionAttribute<Product, OrderInfo> orderInfoCollection;
    public static volatile SingularAttribute<Product, String> productName;
    public static volatile CollectionAttribute<Product, ProductReview> productReviewCollection;

}