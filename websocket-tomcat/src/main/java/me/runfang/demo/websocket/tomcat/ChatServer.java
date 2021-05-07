package me.runfang.demo.websocket.tomcat;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 聊天服务端
 */
@ServerEndpoint("/chat/{username}")
public class ChatServer {

    private static ConcurrentHashMap<Session, String> sessions = new ConcurrentHashMap<>();
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) {
        System.out.println(username + "已登录");
        int count = onlineCount.incrementAndGet();
        System.out.println("当前人数：" + count);
        sessions.put(session,username);
    }

    @OnError
    public void onError(Session session, Throwable e) {
        System.out.println("连接错误.");
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("连接关闭." + sessions.get(session));
        sessions.remove(session);
        int count = onlineCount.decrementAndGet();
        System.out.println("当前人数：" + count);
    }

    @OnMessage
    public void onMessage(String msg, Session session) throws IOException {
        String text = String.format("%s说:%s", sessions.get(session), msg);
        System.out.println("收到消息." + text);
        // 向所有人群发
        for (Session s: sessions.keySet()) {
            s.getBasicRemote().sendText(text);
        }
    }
}
