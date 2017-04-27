package Entity;

import Entity.Customer;
import Entity.Employee;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-04-28T05:50:52")
@StaticMetamodel(Feedback.class)
public class Feedback_ { 

    public static volatile SingularAttribute<Feedback, String> timePost;
    public static volatile SingularAttribute<Feedback, Integer> complainID;
    public static volatile SingularAttribute<Feedback, Customer> customerID;
    public static volatile SingularAttribute<Feedback, Employee> employeeID;
    public static volatile SingularAttribute<Feedback, String> replyContent;
    public static volatile SingularAttribute<Feedback, String> title;
    public static volatile SingularAttribute<Feedback, String> timeReply;
    public static volatile SingularAttribute<Feedback, String> content;

}