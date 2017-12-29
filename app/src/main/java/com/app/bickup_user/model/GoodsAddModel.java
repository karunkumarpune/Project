package com.app.bickup_user.model;

/**
 * Created by fluper-pc on 9/11/17.
 */

public class GoodsAddModel {

    private String id;
    private String name;

    public GoodsAddModel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public GoodsAddModel() {
    }
}