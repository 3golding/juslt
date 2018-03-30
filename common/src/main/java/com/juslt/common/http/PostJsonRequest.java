package com.juslt.common.http;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenqian on 2015/12/10.
 */
public class PostJsonRequest<E> extends PostRequest{

    private String mUrl;
    private JSONObject paramJson;
    private HashMap<String,String> mHeader;
    private HttpResultProcessor mResultProcessor;

    public PostJsonRequest(){
        mHeader = new HashMap<>();
        paramJson = new JSONObject();
        mResultProcessor = new DefaultResultProcessor<String>();
    }

    @Override
    public PostJsonRequest url(String url) {
        mUrl = url;
        return this;
    }

    @Override
    public PostJsonRequest addHeader(String key, String value) {
        mHeader.put(key,value==null?"":value);
        return this;
    }

    @Override
    public PostJsonRequest addHeaders(Map headers) {
        mHeader.putAll(headers);
        return this;
    }

    public PostJsonRequest setParam(JSONObject jsonObject) {
        paramJson = jsonObject;
        return this;
    }

    @Override
    public PostJsonRequest addParam(String key, String value) {
        try {
            paramJson.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    @Deprecated
    //此处此函数无效
    public Request addParams(Map params) {
        return null;
    }

    public PostJsonRequest addParam(String key, JSONObject value){
        try {
            paramJson.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public PostJsonRequest addParam(String key, List<JSONObject> value){
        try {
            paramJson.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public PostJsonRequest addParam(String key, JSONArray value){
        try {
            paramJson.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public PostJsonRequest setResultProcessor(HttpResultProcessor processor) {
        mResultProcessor = processor;
        return this;
    }

    @Override
    public E exec() {

        //组装请求
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection)new URL(mUrl).openConnection();

            conn.setRequestMethod("POST");
            conn.setReadTimeout(HttpConst.READ_TIMEOUT);
            conn.setConnectTimeout(HttpConst.CONN_TIMEOUT);
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setRequestProperty (HttpConst.CONTENT_TYPE , HttpConst.CONTENT_TYPE_JSON) ;
            for (Map.Entry<String,String> entry : mHeader.entrySet()){
                conn.setRequestProperty(entry.getKey(),entry.getValue());
            }

            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(paramJson.toString().getBytes());
            os.flush();
            os.close();

            // 得到响应码
            int res = conn.getResponseCode();
            BufferedReader br;
            StringBuffer sb = new StringBuffer();
            if(res == 200){
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(),HttpConst.UTF8));
                String line;
                while((line = br.readLine())!=null){
                    sb.append(line);
                }
                //返回对应类型数据
                return (E) mResultProcessor.process(sb.toString());
            }else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(conn != null){
                conn.disconnect();
            }
        }
        return null;
    }

}
