package com.manman.chat.server;

import com.manman.chat.handler.*;
import com.manman.chat.protocol.MessageCodecSharable;
import com.manman.chat.protocol.ProcotolFrameDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatServer {
    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();  // 线程安全
        LoginRequestMessageHandler LOGIN_HANDLER = new LoginRequestMessageHandler();   // 登录处理器
        ChatRequestMessageHandler CHAT_HANDLER = new ChatRequestMessageHandler();   // 聊天处理器
        GroupCreateRequestMessageHandler GROUP_CREATE_HANDLER = new GroupCreateRequestMessageHandler();  // 创建群聊处理器
        GroupJoinRequestMessageHandler GROUP_JOIN_HANDLER = new GroupJoinRequestMessageHandler();  // 加入群聊处理器
        GroupMembersRequestMessageHandler GROUP_MEMBERS_HANDLER = new GroupMembersRequestMessageHandler();  // 群聊成员处理器
        GroupQuitRequestMessageHandler GROUP_QUIT_HANDLER = new GroupQuitRequestMessageHandler();  // 退出群聊处理器
        GroupChatRequestMessageHandler GROUP_CHAT_HANDLER = new GroupChatRequestMessageHandler();  // 群里消息处理器
        QuitHandler QUIT_HANDLER = new QuitHandler();   // 断开处理器

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(boss, worker);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new ProcotolFrameDecoder());
                    socketChannel.pipeline().addLast(LOGGING_HANDLER);
                    socketChannel.pipeline().addLast(MESSAGE_CODEC);
                    // 用来判断是不是 读空闲事件过程 或 写空闲时间过程
                    // 5s 内如果没有收到 channel 的数据，会触发一个 IdleState#READ_IDLE 事件
                    socketChannel.pipeline().addLast(new IdleStateHandler(5, 0, 0));
                    socketChannel.pipeline().addLast(new ChannelDuplexHandler() {
                        // ChannelDuplexHandler 可以同时作为入站和出站处理器
                        // 用来触发特殊事件
                        @Override
                        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                            IdleStateEvent event = (IdleStateEvent) evt;
                            if (event.state() == IdleState.READER_IDLE) {
                                log.debug("已经 5s 没有读到数据");
                            }
                        }
                    });
                    socketChannel.pipeline().addLast(LOGIN_HANDLER);
                    socketChannel.pipeline().addLast(CHAT_HANDLER);
                    socketChannel.pipeline().addLast(GROUP_CREATE_HANDLER);
                    socketChannel.pipeline().addLast(GROUP_JOIN_HANDLER);
                    socketChannel.pipeline().addLast(GROUP_MEMBERS_HANDLER);
                    socketChannel.pipeline().addLast(GROUP_QUIT_HANDLER);
                    socketChannel.pipeline().addLast(GROUP_CHAT_HANDLER);
                    socketChannel.pipeline().addLast(QUIT_HANDLER);
                }
            });
            ChannelFuture future = serverBootstrap.bind(8080).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("server error", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}

