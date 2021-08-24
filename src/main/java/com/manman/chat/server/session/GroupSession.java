package com.manman.chat.server.session;

import io.netty.channel.Channel;

import java.util.List;
import java.util.Set;

/**
 * 聊天组会话管理接口
 */
public interface GroupSession {

    /**
     * 创建一个聊天组，如果不存在才能创建
     * @param name  组名
     * @param members  成员
     * @return
     */
    Group createGroup(String name, Set<String> members);

    /**
     * 加入聊天组
     * @param name
     * @param member
     * @return
     */
    Group joinMember(String name, String member);

    /**
     * 移除组员
     * @param name
     * @param member
     * @return 如果组不存在，返回 null，否则返回组对象
     */
    Group removeMember(String name, String member);

    /**
     * 移除聊天组
     * @param name
     * @return
     */
    Group removeGroup(String name);

    /**
     * 获取组成员
     * @param name
     * @return
     */
    Set<String> getMembers(String name);

    /**
     * 获取组成员的 channel 集合，只有在线的 channel 才会返回
     * @param name
     * @return
     */
    List<Channel> getMembersChannel(String name);
}
