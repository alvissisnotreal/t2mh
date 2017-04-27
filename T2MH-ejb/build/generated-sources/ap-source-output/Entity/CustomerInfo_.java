package Entity;

import Entity.Customer;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-04-28T05:50:52")
@StaticMetamodel(CustomerInfo.class)
public class CustomerInfo_ { 

    public static volatile SingularAttribute<CustomerInfo, String> customerAddress;
    public static volatile SingularAttribute<CustomerInfo, String> customerPhone;
    public static volatile SingularAttribute<CustomerInfo, String> customerEmail;
    public static volatile SingularAttribute<CustomerInfo, Integer> customerID;
    public static volatile SingularAttribute<CustomerInfo, String> descriptions;
    public static volatile SingularAttribute<CustomerInfo, String> customerName;
    public static volatile SingularAttribute<CustomerInfo, Customer> customer;

}