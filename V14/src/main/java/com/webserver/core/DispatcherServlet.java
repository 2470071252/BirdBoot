package com.webserver.core;

import com.webserver.contorller.UserController;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.net.URISyntaxException;

/**
 * 该类实际上是SpringMVC框架提供的类，用于接手Tomcat处理一次Http交互
 * 处理请求环节的工作，也是Tomcat和SpringMVC框架整合中关键的一个类
 */
public class DispatcherServlet {
    private static DispatcherServlet instance = new DispatcherServlet();

    private static File root;
    private static File staticDir;
    static {
        try {
            root = new File(ClientHandler.class.getClassLoader()
                    .getResource(".").toURI() );
            staticDir = new File(root, "static");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private DispatcherServlet() {
    }

    public  static DispatcherServlet getInstance(){
        return instance;
    }

    public void service(HttpServletRequest request, HttpServletResponse response){
        String path = request.getRequestURI();
        if("/regUser".equals(path)){
            UserController userController = new UserController();
            userController.reg(request, response);

        }else {
            File index = new File(staticDir, path);
            /*
            如果在static文件夹中找不到对应的文返回404异常页面
            isFile():
            true if and only if the file denoted by this abstract pathname exists and is a normal file;
            false otherwise
             */
            if (index.isFile()) {
                response.setContentFile(index);
            } else {
                response.setStatusCode(404);
                response.setStatusReason("NotFound");
                index = new File(staticDir, "404.html");
                response.setContentFile(index);
            }
            response.addHeader("Server", "BirdWebServer");
        }
    }
}
