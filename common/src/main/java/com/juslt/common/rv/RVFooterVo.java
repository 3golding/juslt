package com.juslt.common.rv;


import android.text.TextUtils;

public class RVFooterVo {

    public static final int NORMAL = 1;
    public static final int LOADING = 2;
    public static final int NO_MORE = 3;

    private String statusNormal;
    private String statusNoMore;

    public String title;
    public int status;

    public RVFooterVo() {
        this(null,null);
    }

    public RVFooterVo(String statusNormal, String statusNoMore) {
        this.status = NORMAL;
        this.statusNormal = statusNormal;
        this.statusNoMore = statusNoMore;

        if(TextUtils.isEmpty(statusNormal)){
            this.statusNormal = "加载更多";
        }
        if(TextUtils.isEmpty(statusNoMore)){
            this.statusNoMore = "没有更多";
        }
    }

    public void update(boolean hasMore){
        if(hasMore){
            status = NORMAL;
            title = statusNormal;
        }else {
            status = NO_MORE;
            title = statusNoMore;
        }
    }

}
