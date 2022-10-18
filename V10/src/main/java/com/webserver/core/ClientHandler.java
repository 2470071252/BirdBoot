package com.webserver.core;

import com.webserver.http.EmptyRequestException;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import javax.print.URIException;
import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

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
            HttpServletResponse response = new HttpServletResponse(socket);

            // 2.处理请求
           DispatcherServlet.getInstance().service(request,response);

            // 3.发送响应
            response.response();

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        } catch (EmptyRequestException e) {

        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
