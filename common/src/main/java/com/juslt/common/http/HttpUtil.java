package com.juslt.common.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by chenqian on 2015/12/9.
 */
public class HttpUtil {

    public static String buildGETUrl(String oldUrl, HashMap<String,String> params) throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        if(params != null && !params.isEmpty()){
            for (String key : params.keySet()){
                if(sb.length()==0){
                    sb.append("?" + URLEncoder.encode(key,HttpConst.UTF8)+ "=" + URLEncoder.encode(params.get(key),HttpConst.UTF8));
                }else {
                    sb.append("&" + URLEncoder.encode(key,HttpConst.UTF8) + "=" + URLEncoder.encode(params.get(key),HttpConst.UTF8));
                }
            }
        }
        sb.insert(0,oldUrl);
        return sb.toString();
    }

}
