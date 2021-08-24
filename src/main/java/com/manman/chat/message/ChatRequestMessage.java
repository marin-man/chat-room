package com.manman.chat.message;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class ChatRequestMessage extends Message {



    @Override
    public int getMessageType() {
        return ChatRequestMessage;
    }
}
