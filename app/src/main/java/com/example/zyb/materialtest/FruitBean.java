package com.example.zyb.materialtest;

/**
 * Created by zyb on 2017/2/24.
 */

public class FruitBean {

    private String name;
    private int imageId;

    public FruitBean(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }
}
