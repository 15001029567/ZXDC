package net.edaibu.easywalking.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.edaibu.easywalking.R;
import net.edaibu.easywalking.bean.BikeBean;

/**
 * 骑行界面的fragment
 */
public class CyclingFragment extends BaseFragment {

    //骑行对象
    private BikeBean bikeBean;
    private TextView tvTime, tvDistance,tvKa,tvBikeCode,tvMoney;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    View view=null;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cycling, container, false);
        tvTime = (TextView) view.findViewById(R.id.tv_am_qiche);
        tvDistance = (TextView) view.findViewById(R.id.tv_am_distance);
        tvKa=(TextView)view.findViewById(R.id.tv_fc_ka);
        tvMoney = (TextView) view.findViewById(R.id.tv_am_money);
        tvBikeCode=(TextView)view.findViewById(R.id.tv_fc_bikeCode);
        return view;
    }


    public void setBikeBean(BikeBean bikeBean){
        this.bikeBean=bikeBean;
    }
}
