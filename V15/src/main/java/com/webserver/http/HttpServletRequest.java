package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

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

    private String requestURI;  // uri中的请求部分，即："?"左侧内容
    private String queryString; // uri中的参数部分，即："?"右侧内容
    private Map<String,String> parameters = new HashMap<>();

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
        System.out.println("请求行内容:"+line);
        // 将请求行内容按照空格拆分问三个部分，分别初始化三个变量
        String[] strings = line.split("\\s");
        method = strings[0];
        uri = strings[1];
        parseURI(); // 进一步解析uri
        protocol = strings[2];
        System.out.println("method:"+method);//method:GET
        System.out.println("uri:"+uri);//uri:/index.html
        System.out.println("protocol:"+protocol);//protocol:HTTP/1.1
    }
    // 进一步解析uri
    /*
    private void parseURI(){
        String[] split = uri.split("\\?");
        if (split.length==1) {
            uri = split[0];
            return;
        }
        requestURI = split[0];
        queryString = split[1];
        String[] split1 = queryString.split("\\&");
        for ( String string : split1 ) {
            String[] s = string.split("=");
            parameters.put(s[0],s.length>1?s[1]:"");

            // String[] s = string.split("=",2);
            //  parameters.put(s[0],s[1];
        }

        System.out.println("requestURI: "+requestURI);
        System.out.println("queryString: "+queryString);
        System.out.println("parameters: "+parameters);
    }
    */
    private void parseURI(){
        /*
            uri有两种情况:
            1:不含有参数的
              例如: /index.html
              直接将uri的值赋值给requestURI即可.

            2:含有参数的
              例如:/regUser?username=fancq&password=&nickname=chuanqi&age=22
              将uri中"?"左侧的请求部分赋值给requestURI
              将uri中"?"右侧的参数部分赋值给queryString
              将参数部分首先按照"&"拆分出每一组参数，再将每一组参数按照"="拆分为参数名与参数值
              并将参数名作为key，参数值作为value存入到parameters中。

              如果表单某个输入框没有输入信息，那么存入parameters时对应的值应当保存为空字符串
         */
        String[] data = uri.split("\\?");
        requestURI = data[0];
        if(data.length>1){
            queryString = data[1];
            parseParameters(queryString);

        }

        //测试提交reg.html注册表单后，这里的打桩应该看到:
        System.out.println("requestURI:"+requestURI);//requestURI:/regUser
        System.out.println("queryString:"+queryString);//username=fancq&password=&nickname=chuanqi&age=22
        System.out.println("parameters:"+parameters);//parameters:{username=xxx, xxx=xxx,...}
    }

    private void parseParameters(String line){
        try {
            line = URLDecoder.decode(line,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //将参数部分按照"&"拆分出每一组参数
        String[] data = line.split("&");
        //遍历每一组参数
        for(String para : data){
            //para:username=zhangsan
//                String[] paras = para.split("=");
//                parameters.put(paras[0], paras.length>1?paras[1]:"");
            String[] paras = para.split("=",2);
            parameters.put(paras[0], paras[1]);
        }
    }

    // 解析消息头
    private void parseHeaders() throws IOException{
        while (true){
            String line = readLine();
            if (line.isEmpty()){
                break;
            }
            System.out.println("消息头:" + line);
            String[] data = line.split(":\\s");
            headers.put(data[0],data[1]);
        }
        System.out.println("headers:"+headers);
    }
    // 解析消息正文
    private void parseContent() throws IOException {
        // 判断消息头中是否含有说明长度的头：Content-Length
        if (headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            System.out.println("正文长度为："+contentLength);
            InputStream in = socket.getInputStream();
            byte[] contentData = new byte[contentLength];
            in.read(contentData);

            // 根据消息头Content-Type来解析正文
            String contentType = headers.get("Content-Type");
            if ("application/x-www-form-urlencoded".equals(contentType)){
                String line = URLDecoder.decode(new String(contentData, ISO_8859_1),"UTF-8");
                System.out.println("正文内容："+line);
                parseParameters(line);
            }
//            else if (){} // 后期可以继续判断扩展判断其他类型进行处理，比如附件上传
        }

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

    public String getRequestURI() {
        return requestURI;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getParameter(String name) {
        return parameters.get(name);
    }
}
