/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Customer;

import XMLAccess.*;

/**
 *
 * @author Huy-PC
 */
public class productImage {
    String image1,image2,image3,image4,cateName;
    String id1,id2,id3,id4,cateID;
    public String getImage1() {
        return image1;
    }

    public String getCateID() {
        return cateID;
    }

    public void setCateID(String cateID) {
        this.cateID = cateID;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public productImage(String image1, String image2, String image3, String image4, String id1, String id2, String id3, String id4, String cateName) {
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.id1 = id1;
        this.id2 = id2;
        this.id3 = id3;
        this.id4 = id4;
         this.cateName = cateName;
    }

    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    public String getId3() {
        return id3;
    }

    public void setId3(String id3) {
        this.id3 = id3;
    }

    public String getId4() {
        return id4;
    }

    public void setId4(String id4) {
        this.id4 = id4;
    }
    
    
    public productImage(String image1, String image2, String image3, String cateName) {
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.cateName = cateName;
    }

    public productImage(String image1, String image2, String image3, String image4, String cateName) {
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.cateName = cateName;
    }

    public productImage(String image1, String image2, String image3, String image4, String id1, String id2, String id3, String id4, String cateName, String cateID) {
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;       
        this.id1 = id1;
        this.id2 = id2;
        this.id3 = id3;
        this.id4 = id4;
         this.cateName = cateName;
        this.cateID = cateID;
    }
    
    
}
