package net.edaibu.easywalking.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.edaibu.easywalking.bean.BikeBean;

/**
 * 预约车辆的fragment
 */
public class BespokeFragment extends BaseFragment {

    //预约对象
    private BikeBean bikeBean;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    public void setBikeBean(BikeBean bikeBean){
        this.bikeBean=bikeBean;
    }
}
