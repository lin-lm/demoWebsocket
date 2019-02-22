package com.javaxapi.demo.controller;

/**
 * @Author: Linlm
 * @Description:
 * @Date: Created in 2019/2/22 下午1:35
 */
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Author: Linlm
 * @Description: 消息发送类。服务端主动向客户端发送消息
 * @Date: Created in 2019/2/21 下午5:49
 */

@RestController
@RequestMapping("/websocketMessage")
public class SocketController {
    @Resource
    WebSocketServer webSocketServer;

    /**
     * 向所有人发送消息
     * @return
     */
    @RequestMapping("/sendAll")
    public String webSocketSendAll(
            @RequestParam("message") String message
    ){
        try {
            webSocketServer.sendMessageToAll(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "群发成功";
    }

    /**
     * 向某个人发送消息
     * @param sessionId
     * @return
     * @throws IOException
     */
    @RequestMapping("/sendOne")
    public String webSocketSendOne(
            @RequestParam("sessionId") String sessionId,
            @RequestParam("message") String message
    ) {
        try {
            webSocketServer.sendMessageToOne(sessionId,message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "发送成功，sessionid：" + sessionId;
    }
}

