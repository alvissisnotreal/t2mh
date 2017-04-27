package Entity;

import Entity.Category;
import Entity.Product;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-04-28T05:50:52")
@StaticMetamodel(Groups.class)
public class Groups_ { 

    public static volatile SingularAttribute<Groups, String> groupDescriptions;
    public static volatile SingularAttribute<Groups, String> groupName;
    public static volatile CollectionAttribute<Groups, Product> productCollection;
    public static volatile SingularAttribute<Groups, Category> cateID;
    public static volatile SingularAttribute<Groups, Integer> groupID;
    public static volatile SingularAttribute<Groups, Integer> groupStatus;

}