package com.manman.chat.message;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class GroupChatRequestMessage extends Message {
    private String content;
    private String groupName;
    private String from;

    public GroupChatRequestMessage() {}

    public GroupChatRequestMessage(String from, String groupName, String content) {
        this.content = content;
        this.from = from;
        this.groupName = groupName;
    }

    @Override
    public int getMessageType() {
        return GroupChatRequestMessage;
    }
}
