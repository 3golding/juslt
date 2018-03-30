package com.juslt.common.http;

import java.util.Map;

/**
 * Created by chenqian on 2015/12/10.
 */
public interface Request<T extends Request,E>{

    T url(String url);

    T addHeader(String key, String value);

    T addHeaders(Map<String, String> headers);

    T addParam(String key, String value);

    T addParams(Map<String, String> params);

    T setResultProcessor(HttpResultProcessor processor);

    E exec();

}
