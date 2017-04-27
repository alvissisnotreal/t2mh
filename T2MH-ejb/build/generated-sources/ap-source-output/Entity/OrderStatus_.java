package Entity;

import Entity.Orders;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-04-28T05:50:52")
@StaticMetamodel(OrderStatus.class)
public class OrderStatus_ { 

    public static volatile SingularAttribute<OrderStatus, String> orderStatusName;
    public static volatile SingularAttribute<OrderStatus, Integer> orderStatusID;
    public static volatile CollectionAttribute<OrderStatus, Orders> ordersCollection;
    public static volatile SingularAttribute<OrderStatus, String> descriptions;

}