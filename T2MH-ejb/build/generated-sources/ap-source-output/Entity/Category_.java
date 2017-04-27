package Entity;

import Entity.Groups;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-04-28T05:50:52")
@StaticMetamodel(Category.class)
public class Category_ { 

    public static volatile SingularAttribute<Category, Integer> cateStatus;
    public static volatile SingularAttribute<Category, String> cateDescriptions;
    public static volatile SingularAttribute<Category, Integer> cateID;
    public static volatile CollectionAttribute<Category, Groups> groupsCollection;
    public static volatile SingularAttribute<Category, String> cateName;

}