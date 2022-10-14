package com.webserver.core;

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
            // 1解析请求行
            String line = readLine();
            System.out.println("请求行内容："+line);

            // 请求行相关信息
            String method;  // 请求方式
            String uri; // 抽象路径
            String protocol;    // 协议版本
            // 将请求行内容按照空格拆分问三个部分，分别初始化三个变量
            String[] strings = line.split("\\s");
            method = strings[0];
            uri = strings[1];
            protocol = strings[2];
            System.out.println("method:"+method);
            System.out.println("uri:"+uri);
            System.out.println("protocol:"+protocol);

            // 读取消息头
            Map<String,String> headers = new HashMap<>();
            while (true){
                line = readLine();
                if (line.isEmpty()){
                    break;
                }
                System.out.println("消息头内容："+line);
                String[] data = line.split(":\\s");
                headers.put(data[0],data[1]);
            }
            System.out.println("Headers:"+headers);

            // 2.处理请求

            // 3.发送响应

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // HTTP协议要求交互完毕要断开连接
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private String readLine() throws IOException{
        InputStream is = socket.getInputStream();
        // 实现读取一行字符串的操作，便于我们那读取请求行和消息头
        int d;
        StringBuilder builder = new StringBuilder();
        // pre表示上次读取的字符，cur表示本次读取的字符
        char pre = 'a', cur = 'a';
        while ((d = is.read()) != -1) {
            cur = (char) d; // 本次读取到的字符
            if (pre == 13 && cur == 10) { // 判读是否连续读到了回车+换行
                break;
            }
            builder.append(cur);
            pre = cur;    // 进入下次循环前，将本次读取的字符记作上次读取的字符
        }
        return builder.toString().trim();  // 去除末尾空白符并返回
    }

}
