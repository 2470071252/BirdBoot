package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求对象
 * 该类的每一个试了用于表示Http协议规定的请求内容，每个请求由三部分构成
 * 请求行，消息头，消息正文
 */
public class HttpServletRequest {
    // 请求行的相关信息
    private String method;  // 请求方式
    private String uri; // 抽象路径
    private String protocol;    // 协议版本
    // 消息头的相关信息
    private Map<String,String> headers = new HashMap<>();

    private Socket socket;
    /**
     * 构造器用于初始化请求对象，初始化的过程就是读取浏览器发送过来的请求
     */
    public HttpServletRequest(Socket socket) throws IOException, EmptyRequestException {
        this.socket = socket;
        // 1解析请求行
        parseRequestLine();
        // 2.解析消息头
        parseHeaders();
        // 3.解析消息正文
        parseContent();
        // HTTP协议要求交互完毕要断开连接
//        socket.close();

    }
    // 解析请求请求行
    private void parseRequestLine() throws IOException, EmptyRequestException {
        // 1解析请求行
        String line = readLine();

        if (line.isEmpty()) {  // 如果请求行为空字符串则本次为空请求
            throw new EmptyRequestException();
        }

        // 将请求行内容按照空格拆分问三个部分，分别初始化三个变量
        String[] strings = line.split("\\s");
        method = strings[0];
        uri = strings[1];
        protocol = strings[2];
    }
    // 解析消息头
    private void parseHeaders() throws IOException{
        while (true){
            String line = readLine();
            if (line.isEmpty()){
                break;
            }
            String[] data = line.split(":\\s");
            headers.put(data[0],data[1]);
        }

    }
    // 解析消息正文
    private void parseContent(){

    }

    private String readLine() throws IOException {
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

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getProtocol() {
        return protocol;
    }

    /**
     * 根据消息头的名字获取该消息头对应的值
     * @param name 消息头名字
     * @return  对应消息头的值
     */
    public String getHeader(String name) {
        return headers.get(name);
    }

}
