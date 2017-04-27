package Entity;

import Entity.AdminRole;
import Entity.EmployeeInfo;
import Entity.Feedback;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-04-28T05:50:52")
@StaticMetamodel(Employee.class)
public class Employee_ { 

    public static volatile SingularAttribute<Employee, String> employeeUsername;
    public static volatile CollectionAttribute<Employee, Feedback> feedbackCollection;
    public static volatile SingularAttribute<Employee, Integer> employeeID;
    public static volatile SingularAttribute<Employee, EmployeeInfo> employeeInfo;
    public static volatile CollectionAttribute<Employee, AdminRole> adminRoleCollection;
    public static volatile SingularAttribute<Employee, String> employeePassword;
    public static volatile SingularAttribute<Employee, Integer> employeeStatus;

}