package com.webserver.test;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;

public class ContentTypeDemo {
    public static void main(String[] args) {
        MimetypesFileTypeMap mime = new MimetypesFileTypeMap();
        File file = new File("lu.css");
        String contentType = mime.getContentType(file);
        System.out.println(contentType);
    }
}
