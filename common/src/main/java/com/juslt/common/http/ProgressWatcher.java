package com.juslt.common.http;

/**
 * Created by chenqian on 2015/12/9.
 */
public interface ProgressWatcher {

    void onProgress(long current, long total);

}
