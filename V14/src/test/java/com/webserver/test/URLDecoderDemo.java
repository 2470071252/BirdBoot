package com.webserver.test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * URLDecoder是用于将%XX这样的内容还原为对应文字的API
 *
 */
public class URLDecoderDemo {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String line = "/regUser?username=%E5%BC%A0&password=aaa123&nickname=nick1&age=12";
        line = URLDecoder.decode(line,"UTF-8");
        System.out.println(line);
    }
}
