package com.uniben.attsys.singletons;

import com.uniben.attsys.models.User;

public class UserObject {
    private User user;
    private static final  UserObject obj = new UserObject();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static UserObject getObj() {
        return obj;
    }
}
