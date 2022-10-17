package com.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

/**
 * 响应对象
 * 当前类的每一个实例用于表示Http协议的一个相应
 * 每个响应有三部分构成：
 * 状态行，响应头，响应正文
 */
public class HttpServletResponse {
    private Socket socket;
    // 状态行相关信息
    private int statusCode = 200;       // 状态代码
    private String statusReason = "OK"; // 状态描述

    // 响应头相关信息

    // 响应正文相关信息
    private File contentFile;           // 响应正文对应的实体文件

    public HttpServletResponse(Socket socket) {
        this.socket = socket;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public void setContentFile(File contentFile) {
        this.contentFile = contentFile;
    }

    /**
     * 将当前响应对象内容按照标准的响应格式发送给客户端
     */
    public void response() throws IOException {
        // 1 发送状态行
        sendStatusLine();
        // 2 发送响应头
        sendHeaders();
        // 3 发送响应内容
        sendContent();

    }

    private void sendStatusLine() throws IOException{
        println("HTTP/1.1"+" "+statusCode+" "+statusReason);
    }

    private  void sendHeaders() throws IOException{
        println("Content-Type: text/html");
        println("Content-Length: " + contentFile.length());
    }

    private void sendContent() throws IOException{
             /*
            单独发送空行表示响应头结束
            相当于
            out.write(13);
            out.write(10);
             */
        println("");
        // 3 发送相应正文
        OutputStream out = socket.getOutputStream();
        FileInputStream fis = new FileInputStream(contentFile);
        byte[] buf = new byte[1024 * 10];
        int len;
        while ((len = fis.read(buf)) != -1) {
            out.write(buf, 0, len);
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
