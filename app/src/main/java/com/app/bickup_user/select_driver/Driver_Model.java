package com.app.bickup_user.select_driver;

public class Driver_Model {
    private String ids;
    private String name;
    private String avatar;

    public Driver_Model(String ids, String name, String avatar) {
        this.ids = ids;
        this.name = name;
        this.avatar = avatar;
    }

    public String getIds() {
        return ids;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }
}