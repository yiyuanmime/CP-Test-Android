package com.cp.user.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cp.user.CPApplication;
import com.cp.user.R;
import com.cp.user.adapter.MenuAdapter;
import com.cp.user.model.HistoryAddress;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yi on 08/07/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private MenuAdapter menuAdapter;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.left_drawer)
    ListView mDrawerList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        ButterKnife.bind(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        drawerMenu();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void drawerMenu() {

        List<HistoryAddress> list = CPApplication.getDevicePreference().historyAddress();

        if (list == null) list = new ArrayList<>();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        menuAdapter = new MenuAdapter(
                this,
                R.layout.drawer_list_item,
                list);
        mDrawerList.setAdapter(menuAdapter);
        mDrawerList.setOnItemClickListener(new BaseActivity.DrawerItemClickListener());

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HistoryAddress clickedItem = (HistoryAddress) parent.getItemAtPosition(position);
            updateMap(clickedItem);
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }
    }

    public void refreshMenu(List<HistoryAddress> list) {
        menuAdapter.populate(list);
    }

    protected abstract int getLayoutResourceId();

    protected abstract void initMapProvider();

    protected abstract void updateMap(HistoryAddress historyAddress);

}
