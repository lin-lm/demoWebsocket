package com.javaxapi.demo.controller;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Linlm
 * @Description: websocket服务器端
 *  @ServerEndPoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端，
 *  注解的值将被用于监听用户连接的终端访问URL地址，客户端可以通过这个URL连接到websocket服务器端
 * @Date: Created in 2019/2/21 下午5:49
 */
@ServerEndpoint("/websocket/{clientIp}")
@Component
@Data
public class WebSocketServer {
    private static int onlineCount=0;

    //用来存放每个客户端对应的WebSocketServer对象
    private static Map<String, WebSocketServer> webSocketMap = new HashMap<>();

    //某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //客户端的ip地址
    private String clientIp;

    /**
     * springboot内置tomcat的话，需要配一下这个。。如果没有这个对象，无法连接到websocket
     * 别问为什么。。很坑。。。
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        //这个对象说一下，貌似只有服务器是tomcat的时候才需要配置,具体我没有研究
        return new ServerEndpointExporter();
    }

    @OnOpen
    public void onOpen(@PathParam("clientIp")String clientIp, Session session){
        this.session=session;
        this.clientIp=clientIp;

        System.out.println("sessionid为："+session.getId());
        webSocketMap.put(session.getId(), this);
        addOnlineCount();
        System.out.println("有新连接"+clientIp+"加入！当前在线人数为"+getOnlineCount());
    }

    @OnClose
    public void onClose(){
        webSocketMap.remove(session.getId());
        subOnlineCount();
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    @OnMessage
    public void onMessage(@PathParam("clientIp")String clientIp, String message){
        System.out.println("收到客户端： " + clientIp +" 发来的消息："+message);
//        群发消息
        for (String sessionid : webSocketMap.keySet()){
            WebSocketServer item = webSocketMap.get(sessionid);
            try {
                if(!sessionid.equals(this.session.getId())) {
                    item.sendMessageToOne(sessionid, "广播：收到客户端【 " + clientIp + "】发来的消息：" + message);
                }
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
     * 用于发送给客户端消息（群发）
     * @param message
     */
    public void sendMessageToAll(String message) throws IOException {
        //遍历客户端
        for (String sessionid : webSocketMap.keySet()) {
            System.out.println("websocket广播消息：" + message);
            System.out.println("当前sessionid为：" + sessionid);
            WebSocketServer item = webSocketMap.get(sessionid);
            try {
                //服务器主动推送
                new Thread(new Runnable(){
                    @Override
                    public void run(){
                        try {
                            item.session.getBasicRemote().sendText(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } catch (Exception e) {
               throw e;
            }
        }
    }

    /**
     * 用于发送给指定客户端消息
     *
     * @param message
     */
    public void sendMessageToOne(String sessionId, String message) throws IOException {
        WebSocketServer webSocket = webSocketMap.get(sessionId);
        if (webSocket == null) {
            System.out.println("没有找到你指定ID的会话：" + sessionId);
            return;
        }

        webSocket.session.getBasicRemote().sendText(message);
    }

    /**
     * @Author: Linlm
     * @Description: 获取当前的连接数
     * @Date: 2019/2/22 上午11:25
     */
    public static synchronized int getOnlineCount(){
        return onlineCount;
    }
    
    /**
     * @Author: Linlm
     * @Description: 有新的用户连接时,连接数自加1
     * @Date: 2019/2/22 上午11:25
     */
    public static synchronized void addOnlineCount(){
        WebSocketServer.onlineCount++;
    }
    
    /**
     * @Author: Linlm
     * @Description: 断开连接时,连接数自减1
     * @Date: 2019/2/22 上午11:26
     */
    public static synchronized void subOnlineCount(){
        WebSocketServer.onlineCount--;
    }
}