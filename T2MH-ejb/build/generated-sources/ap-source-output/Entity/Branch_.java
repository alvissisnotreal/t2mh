package Entity;

import Entity.BranchManager;
import Entity.BranchReview;
import Entity.Payment;
import Entity.Product;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-04-28T05:50:52")
@StaticMetamodel(Branch.class)
public class Branch_ { 

    public static volatile SingularAttribute<Branch, Integer> branchID;
    public static volatile CollectionAttribute<Branch, BranchManager> branchManagerCollection;
    public static volatile CollectionAttribute<Branch, Product> productCollection;
    public static volatile SingularAttribute<Branch, Integer> branchStatus;
    public static volatile SingularAttribute<Branch, String> branchName;
    public static volatile SingularAttribute<Branch, String> branchDescriptions;
    public static volatile CollectionAttribute<Branch, BranchReview> branchReviewCollection;
    public static volatile CollectionAttribute<Branch, Payment> paymentCollection;

}