package com.juslt.common.http;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by chenqian on 2015/12/9.
 */
public abstract class HttpResultProcessor<T>{

    public abstract T process(String result);

    public Class getGenericClass() {
        Type type = getClass().getGenericSuperclass();

        Object genericClass = ((ParameterizedType) type).getActualTypeArguments()[0];
        if (genericClass instanceof ParameterizedType) { // 处理多级泛型
            return (Class) ((ParameterizedType) genericClass).getRawType();

        } else if (genericClass instanceof GenericArrayType) { // 处理数组泛型
            return (Class) ((GenericArrayType) genericClass).getGenericComponentType();

        } else {
            return (Class) genericClass;
        }
    }
}
