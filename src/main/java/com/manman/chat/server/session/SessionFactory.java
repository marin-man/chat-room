package com.manman.chat.server.session;

import com.manman.chat.server.session.imp.SessionMemoryImpl;

public abstract class SessionFactory {

    private static Session session = new SessionMemoryImpl();

    public static Session getSession() {
        return session;
    }
}