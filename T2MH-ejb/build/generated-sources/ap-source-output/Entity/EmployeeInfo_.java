package Entity;

import Entity.Employee;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-04-28T05:50:52")
@StaticMetamodel(EmployeeInfo.class)
public class EmployeeInfo_ { 

    public static volatile SingularAttribute<EmployeeInfo, String> employeePhone;
    public static volatile SingularAttribute<EmployeeInfo, String> employeeName;
    public static volatile SingularAttribute<EmployeeInfo, String> employeeEmail;
    public static volatile SingularAttribute<EmployeeInfo, Integer> employeeID;
    public static volatile SingularAttribute<EmployeeInfo, String> employeeDescriptions;
    public static volatile SingularAttribute<EmployeeInfo, Employee> employee;

}