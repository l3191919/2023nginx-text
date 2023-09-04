package com.atguigu.springcloud.service.util;

import java.util.Base64;

public class Base64Util {

    public static String getBase64Encoder(String  string){
        //加密
        String encodeToString =Base64.getEncoder().encodeToString(string.getBytes());

        return encodeToString;
    }
    public static String getBase64Decoder(String encodeToString){
        //解码
        byte[] decode = Base64.getDecoder().decode(encodeToString);
        return new String(decode);
    }



}
