package com.example.order_assess;

import android.content.Intent;

import com.example.mvp.IView;
import com.example.order_assess.adapter.OrderAssessAdapter;

public interface OrderAssessView extends IView {

    void loadRv(OrderAssessAdapter adapter);

    void takePhoto(Intent intent);

    void photoAlbum(Intent intent);

    void hideAdd();

    void showAdd();

    void showRv();

    void hideRv();
}
