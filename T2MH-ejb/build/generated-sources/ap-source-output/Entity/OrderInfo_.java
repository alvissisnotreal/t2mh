package Entity;

import Entity.OrderInfoPK;
import Entity.Orders;
import Entity.Product;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-04-28T05:50:52")
@StaticMetamodel(OrderInfo.class)
public class OrderInfo_ { 

    public static volatile SingularAttribute<OrderInfo, OrderInfoPK> orderInfoPK;
    public static volatile SingularAttribute<OrderInfo, Product> product;
    public static volatile SingularAttribute<OrderInfo, Integer> quantity;
    public static volatile SingularAttribute<OrderInfo, Orders> orders;
    public static volatile SingularAttribute<OrderInfo, String> descriptions;

}