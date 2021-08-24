package com.manman.chat.server.session.imp;

import com.manman.chat.server.session.Group;
import com.manman.chat.server.session.GroupSession;
import com.manman.chat.server.session.SessionFactory;
import io.netty.channel.Channel;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupSessionMemoryImpl implements GroupSession {
    @Override
    public Group createGroup(String name, Set<String> members) {
        return null;
    }

    @Override
    public Group joinMember(String name, String member) {
        return null;
    }

    @Override
    public Group removeMember(String name, String member) {
        return groupMap.computeIfPresent(name, (key, value) -> {
            value.getMembers().remove(member);
            return value;
        });
    }

    @Override
    public Group removeGroup(String name) {
        return groupMap.remove(name);
    }

    @Override
    public Set<String> getMembers(String name) {
        return groupMap.getOrDefault(name, Group.EMPTY_GROUP).getMembers();
    }

    @Override
    public List<Channel> getMembersChannel(String name) {
        return getMembers(name).stream()
                .map(member -> SessionFactory.getSession().getChannel(member))
                .filter(Objects::isNull)
                .collect(Collectors.toList());
    }
}