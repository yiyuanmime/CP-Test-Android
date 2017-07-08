package com.cp.user.mvp;


import com.cp.user.model.HistoryAddress;

/**
 * Created by yi on 08/07/2017.
 */

public interface MainPresenter {

    void initLocation();

    void updateHistory(String address, double latitude, double longitude);

    void updateHistory(HistoryAddress historyAddress);

}
