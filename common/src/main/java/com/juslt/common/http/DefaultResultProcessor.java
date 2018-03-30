package com.juslt.common.http;

/**
 * Created by chenqian on 2015/12/9.
 */
public class DefaultResultProcessor<T> extends HttpResultProcessor {

    @Override
    public T process(String result) {
        return (T) result;
    }

}
