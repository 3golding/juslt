package com.juslt.common.http;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;


/**
 * Created by chenqian on 2015/12/10.
 */
public class PostMultipartRequest<E> extends PostRequest {

    private static final String PREFIX = "--" ;
    private static final String LINEND = "\r\n" ;

    private String mUrl;
    private HashMap<String,String> mHeader;
    private HashMap<String,String> mParams;
    private HashMap<String,File> mFiles;
    private ProgressWatcher mProgressWatcher;
    private HttpResultProcessor mResultProcessor;

    public PostMultipartRequest(){
        mHeader = new HashMap<>();
        mParams = new HashMap<>();
        mFiles = new HashMap<>();
        mResultProcessor = new DefaultResultProcessor<String>();
    }

    @Override
    public PostMultipartRequest url(String url) {
        mUrl = url;
        return this;
    }

    @Override
    public PostMultipartRequest addHeader(String key, String value) {
        mHeader.put(key,value==null?"":value);
        return this;
    }

    @Override
    public PostMultipartRequest addHeaders(Map headers) {
        mHeader.putAll(headers);
        return this;
    }

    @Override
    public PostMultipartRequest addParam(String key, String value) {
        mParams.put(key,value==null?"":value);
        return this;
    }

    @Override
    public PostMultipartRequest addParams(Map params) {
        mParams.putAll(params);
        return this;
    }

    public PostMultipartRequest addParam(String key, File file){
        mFiles.put(key,file);
        return this;
    }

    public PostMultipartRequest setProgressWatcher(ProgressWatcher watcher){
        mProgressWatcher = watcher;
        return this;
    }

    @Override
    public PostMultipartRequest setResultProcessor(HttpResultProcessor processor) {
        mResultProcessor = processor;
        return this;
    }

    @Override
    public Object exec() {

        //组装请求
        HttpURLConnection conn = null;
        DataOutputStream outStream = null;
        try {

            String BOUNDARY = "BOUNDARY-" + java.util.UUID.randomUUID().toString();
            conn = (HttpURLConnection)new URL(mUrl).openConnection();

            conn.setRequestProperty (HttpConst.CONTENT_TYPE , HttpConst.CONTENT_TYPE_MULTIPART + ";" + HttpConst.BOUNDARY + "=" + BOUNDARY ) ;

            //header
            for (Map.Entry<String,String> entry : mHeader.entrySet()){
                conn.setRequestProperty(entry.getKey(),entry.getValue());
            }

            conn.setRequestMethod("POST");
            conn.setReadTimeout(HttpConst.READ_TIMEOUT);
            conn.setConnectTimeout(HttpConst.CONN_TIMEOUT);
            conn.setUseCaches(false);
            conn.setDoInput(true);
            //分1k区块上传
            conn.setChunkedStreamingMode(-1);

//            //主要是这句，用来禁掉缓存，不过需要将上传数据的大小传进去
//            conn.setFixedLengthStreamingMode((int) (before.length + simpleUploadTask.fileSize + after.length));


            // 首先组拼文本类型的参数
            StringBuffer paramStr = new StringBuffer();
            for (HashMap.Entry<String,String > entry : mParams.entrySet()){
                paramStr.append (PREFIX);
                paramStr.append (BOUNDARY);
                paramStr.append (LINEND);
                paramStr.append ("Content-Disposition: form-data; name=\"" + entry.getKey ( ) + "\"" + LINEND );
                paramStr.append (LINEND);
                paramStr.append (entry.getValue());
                paramStr.append (LINEND);
            }

            outStream = new DataOutputStream(conn.getOutputStream()) ;
            outStream.write (paramStr.toString().getBytes());

            // 发送文件数据
            if (mFiles != null && !mFiles.isEmpty()) {
                //计算文件大小
                long current = 0;
                long total = 0;
                for (HashMap.Entry<String, File> file : mFiles.entrySet()) {
                    total += file.getValue().length();
                }
                //写入文件
                for (HashMap.Entry<String, File> file : mFiles.entrySet()) {
                    StringBuilder paramFile = new StringBuilder();
                    paramFile.append(PREFIX);
                    paramFile.append(BOUNDARY);
                    paramFile.append(LINEND);
                    paramFile.append("Content-Disposition: form-data; name=\"" + file.getKey() + "\"; filename=\"" + file.getValue().getName() + "\"" + LINEND);
                    paramFile.append("Content-Type: application/octet-stream" + LINEND);
                    paramFile.append(LINEND);
                    outStream.write(paramFile.toString().getBytes());

                    InputStream is = new FileInputStream(file.getValue());
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = is.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                        current += len;
                        if(mProgressWatcher != null){
                            mProgressWatcher.onProgress(current,total);
                        }
                    }
                    is.close();
                    outStream.write(LINEND.getBytes());
                    }

                // 请求结束标志
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
                outStream.write(end_data);
                outStream.flush();
            }

            // 得到响应码
            int res = conn.getResponseCode();
            String resMessage = conn.getResponseMessage();
            BufferedReader br;
            StringBuffer sb = new StringBuffer();
            if(res == 200){


//                byte[] rawbytes = new byte[1000];
//                InputStream rawInstream = conn.getInputStream();
//                int pos = 0;
//                int rawbytesRead = rawInstream.read(rawbytes);
//                while ( rawbytesRead > 0 ) {
//                    pos += rawbytesRead;
//                    rawbytesRead = rawInstream.read(rawbytes, pos, rawbytes.length - pos);
//                }
//
//                Log.e("zl","rawbytesRead: "+rawbytesRead);
//
//
//                try {
//                    // ByInflater(rawbytes, rawbytesRead);
//                    String picPath = ByGZIPInputStream(rawbytes, pos);
//                    return picPath;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }


                InputStream inStream = conn.getInputStream();
                br = new BufferedReader(new InputStreamReader(inStream,HttpConst.UTF8));
                String line;
                while((line = br.readLine())!=null){
                    sb.append(line);
                }
                Log.d("===","sb:"+sb.toString());

                //返回对应类型数据
                return mResultProcessor.process(sb.toString());
            }else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(outStream!=null){
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(conn != null){
                conn.disconnect();
            }
        }
        return null;
    }
    public String  ByGZIPInputStream(byte[] bytes, int len) throws IOException {
        GZIPInputStream  gis = new GZIPInputStream(new ByteArrayInputStream(bytes, 0, len));
        byte[] gzipbytes = new byte[1000];
        int gzipbytesread = gis.read(gzipbytes);
        Log.e("zl","gzipbytesread: "+gzipbytesread);
        String res = new String(gzipbytes,0,gzipbytesread);
        Log.e("zl","gzipres: "+res);
        return res;
    }


    public void ByInflater (byte[] bytes, int len) throws IOException, DataFormatException {
        Inflater decompresser = new Inflater();
        decompresser.setInput(bytes, 0, len);
        byte[] result = new byte[1000];
        int InflaterResultLength = decompresser.inflate(result);
        decompresser.end();

        Log.e("zl","InflaterResultLength: "+InflaterResultLength);

    }



    public Object execWithContentType() {

        // 组装请求
        HttpURLConnection conn = null;
        DataOutputStream outStream = null;
        try {

            String BOUNDARY = "BOUNDARY-" + java.util.UUID.randomUUID().toString();
            conn = (HttpURLConnection)new URL(mUrl).openConnection();

            conn.setRequestProperty (HttpConst.CONTENT_TYPE , HttpConst.CONTENT_TYPE_MULTIPART + ";" + HttpConst.BOUNDARY + "=" + BOUNDARY ) ;

            //header
            for (Map.Entry<String,String> entry : mHeader.entrySet()){
                conn.setRequestProperty(entry.getKey(),entry.getValue());
            }

            conn.setRequestMethod("POST");
            conn.setReadTimeout(HttpConst.READ_TIMEOUT);
            conn.setConnectTimeout(HttpConst.CONN_TIMEOUT);
            conn.setUseCaches(false);
            conn.setDoInput(true);
            //分1k区块上传
            // conn.setChunkedStreamingMode(-1);

//            //主要是这句，用来禁掉缓存，不过需要将上传数据的大小传进去
//            conn.setFixedLengthStreamingMode((int) (before.length + simpleUploadTask.fileSize + after.length));


            // 首先组拼文本类型的参数
            ByteBuffer paramBB = ByteBuffer.allocate(1024*1024);
            for (HashMap.Entry<String,String > entry : mParams.entrySet()){
                paramBB.put(PREFIX.getBytes());
                paramBB.put(BOUNDARY.getBytes());
                paramBB.put(LINEND.getBytes());
                paramBB.put(("Content-Disposition: form-data; name=\"" + entry.getKey ( ) + "\"" + LINEND).getBytes());
                paramBB.put(LINEND.getBytes());
                paramBB.put(entry.getValue().getBytes());
                paramBB.put(LINEND.getBytes());
            }


            // 发送文件数据
            if (mFiles != null && !mFiles.isEmpty()) {
                //计算文件大小
                long current = 0;
                long total = 0;
                for (HashMap.Entry<String, File> file : mFiles.entrySet()) {
                    total += file.getValue().length();
                }
                // L.d("MultipartRequest","上传文件大小="+total/1024+"K");
                Log.e("gubo0117","上传文件大小="+total);
                //写入文件
                for (HashMap.Entry<String, File> file : mFiles.entrySet()) {
                    // StringBuilder paramFile = new StringBuilder();
                    paramBB.put(PREFIX.getBytes());
                    paramBB.put(BOUNDARY.getBytes());
                    paramBB.put(LINEND.getBytes());
                    paramBB.put( ("Content-Disposition: form-data; name=\"" + file.getKey() + "\"; filename=\"" + file.getValue().getName() + "\"" + LINEND).getBytes() );
                    paramBB.put( ("Content-Type: application/octet-stream" + LINEND).getBytes() );
                    paramBB.put(LINEND.getBytes());

//                    paramFile.append(PREFIX);
//                    paramFile.append(BOUNDARY);
//                    paramFile.append(LINEND);
//                    paramFile.append("Content-Disposition: form-data; name=\"" + file.getKey() + "\"; filename=\"" + file.getValue().getName() + "\"" + LINEND);
//                    paramFile.append("Content-Type: application/octet-stream" + LINEND);
//                    paramFile.append(LINEND);
//                    outStream.write(paramFile.toString().getBytes());

                    InputStream is = new FileInputStream(file.getValue());
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = is.read(buffer)) != -1) {
                        // outStream.write(buffer, 0, len);
                        paramBB.put(buffer, 0, len);

                        current += len;
                        if(mProgressWatcher != null){
                            mProgressWatcher.onProgress(current,total);
                        }
                    }
                    is.close();
                    // outStream.write(LINEND.getBytes());
                    paramBB.put(LINEND.getBytes());
                }

                // 请求结束标志
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
                // outStream.write(end_data);
                paramBB.put(end_data);
                // outStream.flush();
            }
            int contentLength = paramBB.position();
            conn.setRequestProperty(HttpConst.CONTENT_LENGTH, "" + contentLength);

            outStream = new DataOutputStream(conn.getOutputStream()) ;
            outStream.write (paramBB.array(), 0, paramBB.position());
            outStream.flush();
            outStream.close();
            // 得到响应码
            int res = conn.getResponseCode();
            String resMessage = conn.getResponseMessage();
            BufferedReader br;
            StringBuffer sb = new StringBuffer();
            if(res == 200){


                byte[] rawbytes = new byte[1000];
                InputStream rawInstream = conn.getInputStream();
                int pos = 0;
                int rawbytesRead = rawInstream.read(rawbytes);
                while ( rawbytesRead > 0 ) {
                    pos += rawbytesRead;
                    rawbytesRead = rawInstream.read(rawbytes, pos, rawbytes.length - pos);
                }

                Log.e("zl","rawbytesRead: "+rawbytesRead);


                try {
                    // ByInflater(rawbytes, rawbytesRead);
                    String picPath = ByGZIPInputStream(rawbytes, pos);
                    picPath = picPath.replace("/upload/upload", "/upload"); // http://my.game9z.com/app/upload.php 上传图片服务的bug，为了不跟他耽误时间，我们自己处理一下。
                    Log.e("gubo0117", "picPath =" + picPath);
                    return picPath;
                } catch (Exception e) {
                    e.printStackTrace();
                }


                InputStream inStream = conn.getInputStream();
                br = new BufferedReader(new InputStreamReader(inStream,HttpConst.UTF8));
                String line;
                while((line = br.readLine())!=null){
                    sb.append(line);
                }
                //返回对应类型数据
                return mResultProcessor.process(sb.toString());
            }else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(outStream!=null){
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(conn != null){
                conn.disconnect();
            }
        }
        return null;
    }



}
