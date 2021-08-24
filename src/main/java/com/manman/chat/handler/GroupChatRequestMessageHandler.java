package com.manman.chat.handler;

import com.manman.chat.message.GroupChatRequestMessage;
import com.manman.chat.server.session.GroupSessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;

@ChannelHandler.Sharable
public class GroupChatRequestMessageHandler extends SimpleChannelInboundHandler<GroupChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, GroupChatRequestMessage groupChatRequestMessage) throws Exception {
        List<Channel> channels = GroupSessionFactory.getGroupSession()
                .getMembersChannel(groupChatRequestMessage.getGroupName());

        for (Channel channel : channels) {
            channel.writeAndFlush(new GroupChatRequestMessage(groupChatRequestMessage.getFrom(), msg.getContent()));
        }
    }
}
