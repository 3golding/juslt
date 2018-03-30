package com.juslt.common.http;

/**
 * Created by chenqian on 2015/12/9.
 */
public class HttpConst {

    public static final int READ_TIMEOUT = 5*1000;
    public static final int CONN_TIMEOUT = 5*1000;


    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_TYPE_NORMAL = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_MULTIPART = "multipart/form-data";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String BOUNDARY= "boundary";

    public static final String UTF8 = "UTF-8";

    public static final String CONTENT_LENGTH = "Content-Length";
}
