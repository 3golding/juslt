package com.juslt.common.rv;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import java.util.ArrayList;


public abstract class BaseRvFooterAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int FOOTER = -1;

    public UIEventInterface uiEventInterface;
    protected RVFooterVo mMoreFooterVo;
    protected ArrayList<Object> dataList;
    protected LoadMore mLoadMore;
    protected RecyclerView mRv;
    protected boolean autoLoad;

    public BaseRvFooterAdapter(UIEventInterface uiEventInterface, LoadMore loadMore) {
        this.dataList = new ArrayList<>();
        this.uiEventInterface = uiEventInterface;
        mLoadMore = loadMore;
        mMoreFooterVo = new RVFooterVo();
    }

    public void autoLoadMore(boolean auto) {
        autoLoad = auto;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mRv = recyclerView;
        if (mRv.getLayoutManager() == null) {
            throw new IllegalArgumentException("Init layoutManager first");
        }

        //处理gridLayoutManager
//        if (mRv.getLayoutManager() instanceof GridLayoutManager) {
//            ((GridLayoutManager) mRv.getLayoutManager()).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                @Override
//                public int getSpanSize(int position) {
//                    if (mLoadMore != null && mRv.getAdapter().getItemCount() - 1 == position) {
//                        return ((GridLayoutManager) mRv.getLayoutManager()).getSpanCount();
//                    }
//                    return 1;
//                }
//            });
//        }

        configLoadMore();
    }

    //配置自动加载
    private void configLoadMore() {
        if (mRv == null) {
            return;
        }
        if (!autoLoad) {
            return;
        }
        mRv.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private int layoutManagerType = 0;
            private int[] lastPositions;
            private int lastVisibleItemPosition;
            private int currentScrollState = 0;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManagerType == 0) {
                    if (layoutManager instanceof LinearLayoutManager) {
                        layoutManagerType = 1;
                    } else if (layoutManager instanceof GridLayoutManager) {
                        layoutManagerType = 2;
                    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                        layoutManagerType = 3;
                    } else {
                        throw new RuntimeException("unknown LayoutManager");
                    }
                }

                switch (layoutManagerType) {
                    case 1:
                        lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                        break;
                    case 2:
                        lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                        break;
                    case 3:
                        StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                        if (lastPositions == null) {
                            lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                        }
                        staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                        lastVisibleItemPosition = findMax(lastPositions);
                        break;
                }

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                currentScrollState = newState;
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                if ((visibleItemCount > 0 && currentScrollState == RecyclerView.SCROLL_STATE_IDLE && (lastVisibleItemPosition) >= totalItemCount - 1)) {
                    //加载更多
                    if (mMoreFooterVo != null && mMoreFooterVo.status == RVFooterVo.NORMAL) {
                        mMoreFooterVo.status = RVFooterVo.LOADING;
                        notifyItemChanged(getItemCount() - 1);
                        mLoadMore.loadMoreEvent();
                    }
                }
            }


            private int findMax(int[] lastPositions) {
                int max = lastPositions[0];
                for (int value : lastPositions) {
                    if (value > max) {
                        max = value;
                    }
                }
                return max;
            }
        });
    }

    protected void addLoadMoreFooter(boolean hasMore) {
        if (mLoadMore != null && getItemCount() > 0) {
            mMoreFooterVo.update(hasMore);
            dataList.add(dataList.size(), mMoreFooterVo);
        }
    }

    public void update(Object obj) {
        update(obj, false);
    }

    public abstract void update(Object obj, boolean hasMore);

    //判断item类型，过滤了加载更多的item holder
    public int getItemViewTypeAfterFooter(int position) {
        return 0;
    }

    public abstract BaseViewHolder onCreateViewHolderAfterFooter(ViewGroup parent, int viewType);

    @Override
    public int getItemViewType(int position) {
        if (dataList.get(position) instanceof RVFooterVo) {
            return FOOTER;
        } else {
            return getItemViewTypeAfterFooter(position);
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOTER) {
            return MoreFooterHolder.create(parent, this);
        } else {
            return onCreateViewHolderAfterFooter(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.reset();
        holder.update(dataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void event(Object event, int position) {
        if (uiEventInterface != null) {
            uiEventInterface.event(event, position);
        }
    }

    //加载更多
    public void loadMore() {
        if (mLoadMore != null) {
            mLoadMore.loadMoreEvent();
        }
    }

    public void notifyByFooter(boolean hasMore) {
        addLoadMoreFooter(hasMore);
        notifyDataSetChanged();
    }

    public interface LoadMore {
        void loadMoreEvent();
    }

}
