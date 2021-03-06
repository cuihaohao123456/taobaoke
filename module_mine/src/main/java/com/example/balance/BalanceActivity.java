package com.example.balance;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.module_mine.R;
import com.example.module_mine.R2;
import com.example.mvp.BaseActivity;
import com.example.mvp.BaseFragmentActivity;
import com.example.order.adapter.OrderVPAdapter;

import butterknife.BindView;

@Route(path = "/mine/balance")
public class BalanceActivity extends BaseFragmentActivity<BalanceView, BalancePresenter> implements BalanceView {
    @BindView(R2.id.balance_back)
    ImageView balanceBack;
    @BindView(R2.id.balance_tixian)
    TextView balanceTixian;
    @BindView(R2.id.balance_total_money)
    TextView balanceTotalMoney;
    @BindView(R2.id.balance_tablayout)
    TabLayout balanceTablayout;
    @BindView(R2.id.balance_vp)
    ViewPager balanceVp;

    private String[] titleArr = {"收入", "支出"};

    @Override
    public int getLayoutId() {
        return R.layout.activity_balance;
    }

    @Override
    public void initData() {
        balanceTablayout.setupWithViewPager(balanceVp);
        balanceTablayout.addTab(balanceTablayout.newTab().setText(titleArr[0]));
        balanceTablayout.addTab(balanceTablayout.newTab().setText(titleArr[1]));
        presenter.initVP(getSupportFragmentManager(), titleArr);
        balanceVp.setOffscreenPageLimit(2);

        presenter.loadData();
    }

    @Override
    public void initClick() {
        balanceBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        balanceTixian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.jumpToCashout();
            }
        });
    }

    @Override
    public void updateVP(OrderVPAdapter adapter) {
        balanceVp.setAdapter(adapter);
    }

    @Override
    public BalanceView createView() {
        return this;
    }

    @Override
    public BalancePresenter createPresenter() {
        return new BalancePresenter(this);
    }
}
