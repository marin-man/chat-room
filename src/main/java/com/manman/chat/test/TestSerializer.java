package com.manman.chat.test;

import com.manman.chat.config.Config;
import com.manman.chat.message.LoginRequestMessage;
import com.manman.chat.message.Message;
import com.manman.chat.protocol.MessageCodecSharable;
import com.manman.chat.protocol.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LoggingHandler;

import java.io.IOException;

public class TestSerializer {
    public static void main(String[] args) throws IOException {
        MessageCodecSharable CODEC = new MessageCodecSharable();
        LoggingHandler LOGGING = new LoggingHandler();
        EmbeddedChannel channel = new EmbeddedChannel(LOGGING, CODEC, LOGGING);

        LoginRequestMessage zhangsan = new LoginRequestMessage("zhangsan", "123");
        channel.writeOutbound(zhangsan);

        ByteBuf buf = messageToByteBuf(zhangsan);
        channel.writeInbound(buf);
    }

    public static ByteBuf messageToByteBuf(Message msg) throws IOException {
        int algorithm = Config.getSerializerAlgorithm().ordinal();
        ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
        out.writeBytes(new byte[]{1, 2, 3, 4});
        out.writeByte(1);
        out.writeByte(algorithm);
        out.writeByte(msg.getMessageType());
        out.writeInt(msg.getSequenceId());
        out.writeByte(0xff);
        byte[] bytes = Serializer.Algorithm.values()[algorithm].serialize(msg);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
        return out;
    }
}
