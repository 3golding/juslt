package com.juslt.common.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenqian on 2015/12/10.
 */
public class GetRequest<E> implements Request{

    private String mUrl;
    private HashMap<String,String> mHeaders;
    private HashMap<String,String> mParams;
    private HttpResultProcessor mResultProcessor;

    public GetRequest() {
        mHeaders = new HashMap<>();
        mParams = new HashMap<>();
        mResultProcessor = new DefaultResultProcessor<String>();
    }

    @Override
    public GetRequest url(String url) {
        mUrl = url;
        return this;
    }

    @Override
    public GetRequest addHeader(String key, String value) {
        mHeaders.put(key, value);
        return this;
    }

    @Override
    public GetRequest addHeaders(Map headers) {
        mHeaders.putAll(headers);
        return this;
    }

    @Override
    public GetRequest addParam(String key, String value) {
        mParams.put(key,value==null?"":value);
        return this;
    }

    @Override
    public GetRequest addParams(Map params) {
        mParams.putAll(params);
        return this;
    }

    public GetRequest setResultProcessor(HttpResultProcessor processor){
        mResultProcessor = processor;
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E exec() {
        //组装请求
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection)new URL(HttpUtil.buildGETUrl(mUrl,mParams)).openConnection();
            conn.setRequestMethod("GET");

            for (Map.Entry<String,String> entry : mHeaders.entrySet()){
                mHeaders.put(entry.getKey(),entry.getValue());
            }

            conn.setReadTimeout(HttpConst.READ_TIMEOUT);
            conn.setConnectTimeout(HttpConst.CONN_TIMEOUT);
            conn.setUseCaches(false);
            conn.setDoInput(true);

            int res = conn.getResponseCode();
            BufferedReader br;
            StringBuffer sb = new StringBuffer();
            if(res == 200){
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(),HttpConst.UTF8));
                String line;
                while((line = br.readLine())!=null){
                    sb.append(line);
                }

                return (E)mResultProcessor.process(sb.toString());
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
