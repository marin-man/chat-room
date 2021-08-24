package com.manman.chat.handler;

import com.manman.chat.message.ChatRequestMessage;
import com.manman.chat.message.ChatResponseMessage;
import com.manman.chat.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class ChatRequestMessageHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ChatRequestMessage chatRequestMessage) throws Exception {
        String to = chatRequestMessage.getTo();
        Channel channel = SessionFactory.getSession().getChannel(to);
        if (channel != null) {
            // 对方在线
            channel.writeAndFlush(new ChatResponseMessage(chatRequestMessage.getFrom(), chatRequestMessage.getContent()));
        } else {
            // 对方不在线
            channelHandlerContext.writeAndFlush(new ChatResponseMessage(false, "对方用户不存在或不在线"));
        }
    }
}
