package com.juslt.common.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by chenqian on 2015/12/10.
 */
public class PostFormRequest<E> extends PostRequest {

    private String mUrl;
    private HashMap<String,String> mHeader;
    private HashMap<String,String> mParams;
    private HttpResultProcessor mResultProcessor;

    public PostFormRequest(){
        mHeader = new HashMap<>();
        mParams = new HashMap<>();
        mResultProcessor = new DefaultResultProcessor<String>();
    }

    @Override
    public PostFormRequest url(String url) {
        mUrl = url;
        return this;
    }

    @Override
    public PostFormRequest addHeader(String key, String value) {
        mHeader.put(key,value==null?"":value);
        return this;
    }

    @Override
    public PostFormRequest addHeaders(Map headers) {
        mHeader.putAll(headers);
        return this;
    }

    @Override
    public PostFormRequest addParam(String key, String value) {
        mParams.put(key,value==null?"":value);
        return this;
    }

    @Override
    public PostFormRequest addParams(Map params) {
        mParams.putAll(params);
        return this;
    }

    @Override
    public PostFormRequest setResultProcessor(HttpResultProcessor processor) {
        mResultProcessor = processor;
        return this;
    }

    @Override
    public E exec() {

        //组装请求
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection)new URL(mUrl).openConnection();
            //header
            for (Map.Entry<String,String> entry : mHeader.entrySet()){
                conn.setRequestProperty(entry.getKey(),entry.getValue());
            }

            //body
            StringBuffer paramStr = new StringBuffer();
            for (Map.Entry<String,String> entry : mParams.entrySet()){
                if(paramStr.length()==0){
                    paramStr.append(entry.getKey()+"="+entry.getValue());
                }else {
                    paramStr.append("&"+entry.getKey()+"="+entry.getValue());
                }
            }

            conn.setRequestMethod("POST");
            conn.setReadTimeout(HttpConst.READ_TIMEOUT);
            conn.setConnectTimeout(HttpConst.CONN_TIMEOUT);
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(paramStr.toString().getBytes());
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
