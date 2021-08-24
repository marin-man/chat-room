package com.manman.chat.protocol;

import com.manman.chat.config.Config;
import com.manman.chat.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 必须和 LengthFieldBasedFrameDecoder 一起使用，确保读到的 bytebuf 是完整的
 */
@ChannelHandler.Sharable
@Slf4j
public class MessageCodecSharable extends MessageToMessageCodec<ByteBuf, Message> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, List<Object> list) throws Exception {
        ByteBuf byteBuf = channelHandlerContext.alloc().buffer();
        // 1. 约定魔数
        byteBuf.writeBytes(new byte[]{1, 2, 3, 4});
        // 2. 字节的版本
        byteBuf.writeByte(1);
        // 3. 字节的序列化方式：1-jdk，2-json
        byteBuf.writeByte(Config.getSerializerAlgorithm().ordinal());
        // 4. 字节的指令类型
        byteBuf.writeByte(message.getMessageType());
        // 5. 请求序号   4 个字节
        byteBuf.writeInt(message.getSequenceId());
        // 无意义，使 header 对齐
        byteBuf.writeByte(0xff);
        // 6. 获取内容字节数组
        byte[] bytes = Config.getSerializerAlgorithm().serialize(message);
        // 7. 长度
        byteBuf.writeInt(bytes.length);
        // 8. 写入内容
        byteBuf.writeBytes(bytes);
        list.add(byteBuf);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf buf, List<Object> list) throws Exception {
        int magicNum = buf.readInt();
        byte version = buf.readByte();
        byte serializerAlgorithm = buf.readByte();
        byte messageType = buf.readByte();
        int sequenceId = buf.readInt();
        buf.readByte();
        int length = buf.readInt();
        byte[] bytes = new byte[length];
        buf.readBytes(bytes, 0, length);
        // 找到反序列化算法
        Serializer.Algorithm algorithm = Serializer.Algorithm.values()[serializerAlgorithm];
        // 确定具体消息类型
        Class<?> messageClass = Message.getMessageClass(messageType);
        Object message = algorithm.deserialize(messageClass, bytes);
        log.debug("{}, {}, {}, {}, {}, {}", magicNum, version, serializerAlgorithm, messageType, sequenceId, length);
        log.debug("{}", message);
        list.add(message);
    }
}
