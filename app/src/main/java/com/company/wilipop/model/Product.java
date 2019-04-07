package com.company.wilipop.model;

public class Product {
    public String title;
    public String photoUrl;
    public String sellerUid;
    public String sellerName;

    public Product() {}

    public Product(String title, String sellerUid, String sellerName) {
        this.title = title;
        this.sellerUid = sellerUid;
        this.sellerName = sellerName;
        this.photoUrl = "http://viewtry-photos.s3.amazonaws.com/2015-06-19-442-appleredsideview.jpg";
    }
}
