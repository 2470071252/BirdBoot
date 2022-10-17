package com.webserver.core;

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
            String path = request.getUri();
            File root = new File(ClientHandler.class.getClassLoader()
                    .getResource(".").toURI()
            );
            File staticDir = new File(root, "static");
            File index = new File(staticDir, path);

            /*
            如果在static文件夹中找不到对应的文返回404异常页面
            isFile():
            true if and only if the file denoted by this abstract pathname exists and is a normal file;
            false otherwise
             */
            if (index.isFile()){
                response.setContentFile(index);
            }else {
                response.setStatusCode(404);
                response.setStatusReason("NotFound");
                index = new File(staticDir,"404.html");
                response.setContentFile(index);
            }

            // 3.发送响应
            response.response();

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

}
