package Entity;

import Entity.Customer;
import Entity.OrderInfo;
import Entity.OrderStatus;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-04-28T05:50:52")
@StaticMetamodel(Orders.class)
public class Orders_ { 

    public static volatile SingularAttribute<Orders, String> timeDelivery;
    public static volatile SingularAttribute<Orders, String> receiver;
    public static volatile SingularAttribute<Orders, Integer> orderID;
    public static volatile SingularAttribute<Orders, String> timeOrder;
    public static volatile SingularAttribute<Orders, String> descriptions;
    public static volatile CollectionAttribute<Orders, OrderInfo> orderInfoCollection;
    public static volatile SingularAttribute<Orders, String> senderPhone;
    public static volatile SingularAttribute<Orders, String> receiverAddress;
    public static volatile SingularAttribute<Orders, String> receiverPhone;
    public static volatile SingularAttribute<Orders, OrderStatus> orderStatusID;
    public static volatile SingularAttribute<Orders, String> sender;
    public static volatile SingularAttribute<Orders, Double> priceTotal;
    public static volatile SingularAttribute<Orders, Customer> customerID;

}