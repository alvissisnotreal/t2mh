package Entity;

import Entity.Branch;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-04-28T05:50:52")
@StaticMetamodel(Payment.class)
public class Payment_ { 

    public static volatile SingularAttribute<Payment, Branch> branchID;
    public static volatile SingularAttribute<Payment, String> timeCreate;
    public static volatile SingularAttribute<Payment, Double> amount;
    public static volatile SingularAttribute<Payment, String> dateDebt;
    public static volatile SingularAttribute<Payment, String> endDate;
    public static volatile SingularAttribute<Payment, String> paymentID;
    public static volatile SingularAttribute<Payment, Double> paid;
    public static volatile SingularAttribute<Payment, Double> debt;
    public static volatile SingularAttribute<Payment, String> startDate;

}