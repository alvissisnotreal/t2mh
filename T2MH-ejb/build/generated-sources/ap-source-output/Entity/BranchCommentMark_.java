package Entity;

import Entity.BranchCommentMarkPK;
import Entity.BranchReview;
import Entity.Customer;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-04-28T05:50:52")
@StaticMetamodel(BranchCommentMark.class)
public class BranchCommentMark_ { 

    public static volatile SingularAttribute<BranchCommentMark, Integer> bCMStars;
    public static volatile SingularAttribute<BranchCommentMark, BranchReview> branchReview;
    public static volatile SingularAttribute<BranchCommentMark, BranchCommentMarkPK> branchCommentMarkPK;
    public static volatile SingularAttribute<BranchCommentMark, Customer> customer;

}