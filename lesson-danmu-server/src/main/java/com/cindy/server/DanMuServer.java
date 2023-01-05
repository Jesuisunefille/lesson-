package com.cindy.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cindy
 */
@Slf4j
@ServerEndpoint("/api/v1/danmu-server/{id}")
@Component
public class DanMuServer {

    /**
     * 用于存储每个WS客户端的Session实例
     * ConcurrentHashMap保证线程安全，static保证实例唯一
     */
    private static final Map<String, Session> CLIENTS = new ConcurrentHashMap<>();

    /**
     * 当WS客户端上线时触发：将WS客户端的标识信息及其session实例存入Map
     *
     * @param id WS客户端唯一标识
     * @param session  每个WS客户端连入WS服务端时，都会生成独有的session对象
     */
    @OnOpen
    public void onOpen(@PathParam("id") String id, Session session) {
        CLIENTS.put(id, session);
        log.info("{} 号用户上线了", id);
    }

    /**
     * 当WS客户端下线时触发：从Map中移除该WS客户端和及其session实例
     *
     * @param id WS客户端唯一标识
     * @param session  每个WS客户端连入WS服务端时，都会生成独有的session对象
     */
    @OnClose
    public void onClose(@PathParam("id") String id, Session session) {
        CLIENTS.remove(id);
        log.info("{} 号用户下线了", id);
    }

    /**
     * 当WS客户端连接WS服务端异常时触发
     *
     * @param id WS客户端唯一标识
     * @param e        异常实例
     * @param session  每个WS客户端连入WS服务端时，都会生成独有的session对象
     */
    @OnError
    public void onError(@PathParam("id") String id, Throwable e, Session session) {
        log.error("{} 号用户连接或者通信异常", id, e);
    }

    /**
     * 当WS服务端接收到消息的时候触发：对Map中的全部在线的WS客户端群发消息
     *
     * @param id WS客户端唯一标识
     * @param msg      消息内容
     * @param session  每个WS客户端连入WS服务端时，都会生成独有的session对象
     *               1  zhang    你好
     */
    @OnMessage
    public void onMessage(@PathParam("id") String id, String msg, Session session) {
        log.info("{} 号用户发来消息：{}", id, msg);
        CLIENTS.keySet().forEach(key ->
                CLIENTS.get(key).getAsyncRemote().sendText(msg));
    }
}