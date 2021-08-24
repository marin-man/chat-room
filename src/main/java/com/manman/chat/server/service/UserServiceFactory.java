package com.manman.chat.server.service;

import com.manman.chat.server.service.impl.UserServiceMemoryImpl;

public abstract class UserServiceFactory {

    private static UserService userService = new UserServiceMemoryImpl();

    public static UserService getUserService()
    {
        return userService;
    }
}
