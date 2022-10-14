package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * 当前线程任务负责与指定的客户端进行交互
 * 这里的交互规则要遵从HTTP协议的交换要求，以客户端进行“一问一答”的交互规则
 *
 * 交互过程分三步：
 * 1.解析请求
 * 2.处理请求
 * 3.发送响应
 */
public class ClientHandler implements Runnable{
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // 1.解析请求
            InputStream is = socket.getInputStream();
            int d;
            while ((d=is.read())!=-1){
                char c = (char)d;
                System.out.print(c);
            }

            // 2.处理请求

            // 3.发送响应

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            // HTTP协议要求交互完毕要断开连接
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
