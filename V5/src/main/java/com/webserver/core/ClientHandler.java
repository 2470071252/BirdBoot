package com.webserver.core;

import com.webserver.http.HttpServletRequest;

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
            String path = request.getUri();

            // 2.处理请求

            // 3.发送相应
            File root = new File(ClientHandler.class.getClassLoader()
                    .getResource(".").toURI());
            File staticDir = new File(root, "static");
            File index = new File(staticDir, path);

            // 1 发送状态行
            println("HTTP/1.1 200 OK");

            // 2 发送响应头
            println("Content-Type: text/html");
            println("Content-Length: " + index.length());

            /*
            单独发送空行表示响应头结束
            相当于
            out.write(13);
            out.write(10);
             */
            println("");

            // 3 发送相应正文
            OutputStream out = socket.getOutputStream();
            FileInputStream fis = new FileInputStream(index);
            byte[] buf = new byte[1024 * 10];
            int len;
            while ((len = fis.read(buf)) != -1) {
                out.write(buf,0,len);
            }


        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void println(String line) throws IOException {
        OutputStream out = socket.getOutputStream();
        byte[] data;
        data = line.getBytes(ISO_8859_1);
        out.write(data);
        out.write(13);
        out.write(10);
    }
}
