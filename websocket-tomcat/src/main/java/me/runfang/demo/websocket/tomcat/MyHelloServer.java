package me.runfang.demo.websocket.tomcat;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/hello")
public class MyHelloServer {
    @OnMessage
    public void processingGreeting(String message, Session session) {
        System.out.println("服务端接收到：" + message);
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("创建连接.");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("连接出错. 原因：" + throwable.getMessage());
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("连接关闭. 原因：" + closeReason.getReasonPhrase());
    }
}
