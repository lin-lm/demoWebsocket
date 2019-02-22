package com.javaxapi.demo.controller;

/**
 * @Author: Linlm
 * @Description:
 * @Date: Created in 2019/2/21 下午5:49
 */

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ServerEndPoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端，
 * 注解的值将被用于监听用户连接的终端访问URL地址，客户端可以通过这个URL连接到websocket服务器端
 */
@ServerEndpoint("/websocket/{clientIp}")
@Component
@Data
public class WebSockTest {
    private static int onlineCount=0;
    private static CopyOnWriteArrayList<WebSockTest> webSocketSet=new CopyOnWriteArrayList<WebSockTest>();

    //某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //用来存放每个客户端对应的WebSocketServer对象
    private static ConcurrentHashMap<String,WebSockTest> webSocketMap = new ConcurrentHashMap<String, WebSockTest>();

    //客户端的ip地址
    private String clientIp;


    @OnOpen
    public void onOpen(@PathParam("clientIp")String clientIp, Session session){
        this.session=session;
        this.clientIp=clientIp;
        webSocketSet.add(this);//加入set中
        webSocketMap.put(clientIp,this);  //将当前对象放入map中
        addOnlineCount();
        System.out.println("有新连接"+clientIp+"加入！当前在线人数为"+getOnlineCount());
    }

    @OnClose
    public void onClose(){
        webSocketSet.remove(this);
        subOnlineCount();
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    @OnMessage
    public void onMessage(String message,Session session){
        System.out.println("来自客户端的消息："+message);
//        群发消息
        for (WebSockTest item:webSocketSet){
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    @OnError
    public void onError(Session session,Throwable throwable){
        System.out.println("发生错误！");
        throwable.printStackTrace();
    }


    //   下面是自定义的一些方法
    /**
     * @Author: Linlm
     * @Description: 给当前用户发送消息
     * @Date: 2019/2/22 上午11:20
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 给所有用户发消息
     * @param message
     */
    public static void sendMessageAll(final String message){
        //使用entrySet而不是用keySet的原因是,entrySet体现了map的映射关系,遍历获取数据更快。
        Set<Map.Entry<String, WebSocketServer>> entries = webSocketMap.entrySet();
        for (Map.Entry<String, WebSocketServer> entry : entries) {
            final WebSocketServer webSocketServer = entry.getValue();
            //这里使用线程来控制消息的发送,这样效率更高。
            new Thread(new Runnable() {
                public void run() {
                    webSocketServer.sendMessage(message);
                }
            }).start();
        }
    }

    public static synchronized int getOnlineCount(){
        return onlineCount;
    }
    public static synchronized void addOnlineCount(){
        WebSockTest.onlineCount++;
    }
    public static synchronized void subOnlineCount(){
        WebSockTest.onlineCount--;
    }
}