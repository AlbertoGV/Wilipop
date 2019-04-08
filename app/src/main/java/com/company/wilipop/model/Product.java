package com.company.wilipop.model;

public class Product {
    public String description;
    public String photoUrl;
    public String uid;
    public String displayName;

    public Product() {}

    public Product(String description, String uid, String displayName) {
        this.description = description;
        this.uid = uid;
        this.displayName = displayName;
        this.photoUrl = "http://viewtry-photos.s3.amazonaws.com/2015-06-19-442-appleredsideview.jpg";
    }
}
