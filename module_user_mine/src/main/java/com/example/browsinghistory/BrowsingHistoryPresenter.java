package com.example.browsinghistory;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.browsinghistory.adapter.BrowsingHistoryParentAdapter;
import com.example.browsinghistory.bean.BrowsingHistoryParentBean;
import com.example.module_user_mine.R;
import com.example.mvp.BasePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cuihaohao on 2019/5/27
 * Describe:
 */
public class BrowsingHistoryPresenter extends BasePresenter<BrowsingHistoryView> {

    public BrowsingHistoryPresenter(Context context) {
        super(context);
    }

    @Override
    protected void onViewDestroy() {

    }

    public void browsingHistoryRec(RecyclerView browsingHistoryRec){
        List<BrowsingHistoryParentBean> list = new ArrayList<>();
        list.add(new BrowsingHistoryParentBean(false,"5月27日"));
        list.add(new BrowsingHistoryParentBean(false,"5月20日"));
        list.add(new BrowsingHistoryParentBean(false,"5月17日"));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        browsingHistoryRec.setLayoutManager(linearLayoutManager);
        BrowsingHistoryParentAdapter browsingHistoryParentAdapter = new BrowsingHistoryParentAdapter(mContext, list, R.layout.item_browsing_history_parent);
        browsingHistoryRec.setAdapter(browsingHistoryParentAdapter);
    }
}