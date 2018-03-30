package com.juslt.common.rv;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.juslt.common.R;


public class MoreFooterHolder extends BaseViewHolder {

    TextView tvLoad;
    ProgressBar pb;

    public static MoreFooterHolder create(ViewGroup parent, BaseRvFooterAdapter adapter) {
        return new MoreFooterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.toolbox__h_footer_more, parent, false),adapter);
    }

    private RVFooterVo mFooterVo;

    public MoreFooterHolder(View itemView, BaseRvFooterAdapter adapter) {
        super(itemView,adapter);
        tvLoad = (TextView) itemView.findViewById(R.id.tv_load);
        pb = (ProgressBar) itemView.findViewById(R.id.pb);
    }

    @Override
    public void update(Object obj, int position) {
        mFooterVo = (RVFooterVo) obj;
        tvLoad.setText(mFooterVo.title);

        updateByStatus();

        tvLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFooterVo.status = RVFooterVo.LOADING;
                updateByStatus();
                mAdapter.loadMore();
            }
        });
    }

    private void updateByStatus() {
        tvLoad.setEnabled(false);
        if (mFooterVo.status == RVFooterVo.LOADING) {
            tvLoad.setVisibility(View.GONE);
            pb.setVisibility(View.VISIBLE);
        } else if(mFooterVo.status == RVFooterVo.NORMAL){
            tvLoad.setVisibility(View.VISIBLE);
            pb.setVisibility(View.GONE);
            tvLoad.setEnabled(true);
        }else {
            tvLoad.setVisibility(View.VISIBLE);
            pb.setVisibility(View.GONE);
        }
    }

    @Override
    public void reset() {

    }
}
