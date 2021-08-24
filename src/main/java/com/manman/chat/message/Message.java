package com.manman.chat.message;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息封装
 */
@Data
public abstract class Message implements Serializable {

    public static Class<?> getMessageClass(int messageType) {
        return messageClasses.get(messageType);
    }

    private int sequenceId;

    private int messageType;

    public abstract int getMessageType();

    public static final int LoginRequestMessage = 0;
    public static final int LoginResponseMessage = 1;
    public static final int ChatRequestMessage = 2;
    public static final int ChatResponseMessage = 3;
    public static final int GroupCreateRequestMessage = 4;
    public static final int GroupCreateResponseMessage = 5;
    public static final int GroupJoinRequestMessage = 6;
    public static final int GroupJoinResponseMessage = 7;
    public static final int GroupQuitRequestMessage = 8;
    public static final int GroupQuitResponseMessage = 9;
    public static final int GroupChatRequestMessage = 10;
    public static final int GroupChatResponseMessage = 11;
    public static final int GroupMembersRequestMessage = 12;
    public static final int GroupMembersResponseMessage = 13;

    public static final int PingMessage = 14;
    public static final int PongMessage = 15;

    private static final Map<Integer, Class<?>> messageClasses = new HashMap<>();

    static {
        messageClasses.put(LoginRequestMessage, com.manman.chat.message.LoginRequestMessage.class);
        messageClasses.put(LoginResponseMessage, com.manman.chat.message.LoginResponseMessage.class);
        messageClasses.put(ChatRequestMessage, com.manman.chat.message.ChatRequestMessage.class);
        messageClasses.put(ChatResponseMessage, com.manman.chat.message.ChatResponseMessage.class);
        messageClasses.put(GroupCreateRequestMessage, com.manman.chat.message.GroupCreateRequestMessage.class);
        messageClasses.put(GroupCreateResponseMessage, com.manman.chat.message.GroupCreateResponseMessage.class);
        messageClasses.put(GroupJoinRequestMessage, com.manman.chat.message.GroupJoinRequestMessage.class);
        messageClasses.put(GroupJoinResponseMessage, com.manman.chat.message.GroupJoinResponseMessage.class);
        messageClasses.put(GroupQuitRequestMessage, com.manman.chat.message.GroupQuitRequestMessage.class);
        messageClasses.put(GroupQuitResponseMessage, com.manman.chat.message.GroupQuitResponseMessage.class);
        messageClasses.put(GroupChatRequestMessage, com.manman.chat.message.GroupChatRequestMessage.class);
        messageClasses.put(GroupChatResponseMessage, com.manman.chat.message.GroupChatResponseMessage.class);
        messageClasses.put(GroupMembersRequestMessage, com.manman.chat.message.GroupMembersRequestMessage.class);
        messageClasses.put(GroupMembersResponseMessage, com.manman.chat.message.GroupMembersResponseMessage.class);
        messageClasses.put(PingMessage, com.manman.chat.message.PingMessage.class);
        messageClasses.put(PongMessage, com.manman.chat.message.PongMessage.class);
    }

}
