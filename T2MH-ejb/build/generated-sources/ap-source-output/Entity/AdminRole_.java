package Entity;

import Entity.Employee;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-04-28T05:50:52")
@StaticMetamodel(AdminRole.class)
public class AdminRole_ { 

    public static volatile SingularAttribute<AdminRole, String> roleID;
    public static volatile SingularAttribute<AdminRole, String> roleName;
    public static volatile CollectionAttribute<AdminRole, Employee> employeeCollection;
    public static volatile SingularAttribute<AdminRole, String> roleDescriptions;

}