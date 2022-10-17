package com.webserver.core;

import com.webserver.http.HttpServletRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 当前线程任务负责与指定的客户端进行交互
 * 这里的交互规则要遵从HTTP协议的交换要求，以客户端进行“一问一答”的交互规则
 * <p>
 * 交互过程分三步：
 * 1.解析请求
 * 2.处理请求
 * 3.发送响应
 */
public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // 1.解析请求
            HttpServletRequest request = new HttpServletRequest(socket);

            String path = request.getUri();
            System.out.println(path);






        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
