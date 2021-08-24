package com.manman.chat.handler;

import com.manman.chat.message.GroupCreateRequestMessage;
import com.manman.chat.message.GroupCreateResponseMessage;
import com.manman.chat.server.session.Group;
import com.manman.chat.server.session.GroupSession;
import com.manman.chat.server.session.GroupSessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;
import java.util.Set;

public class GroupCreateRequestMessageHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, GroupCreateRequestMessage groupCreateRequestMessage) throws Exception {
        String groupName = groupCreateRequestMessage.getGroupName();
        Set<String> members = groupCreateRequestMessage.getMembers();
        // 群管理器
        GroupSession groupSession = GroupSessionFactory.getGroupSession();
        Group group = groupSession.createGroup(groupName, members);
        if (group == null) {
            // 发送拉群消息
            List<Channel> channels = groupSession.getMembersChannel(groupName);
            for (Channel channel : channels) {
                channel.writeAndFlush(new GroupCreateResponseMessage(true, "您已被拉入" + groupName));
            }
            channelHandlerContext.writeAndFlush(new GroupCreateResponseMessage(true, groupName + "创建成功"));
        } else
            channelHandlerContext.writeAndFlush(new GroupCreateResponseMessage(false, groupName + "已经存在"));
    }
}
