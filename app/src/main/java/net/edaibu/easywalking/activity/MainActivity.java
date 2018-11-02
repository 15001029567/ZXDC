package net.edaibu.easywalking.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import net.edaibu.easywalking.R;
import net.edaibu.easywalking.fragment.MapFragment;

public class MainActivity extends BaseActivity{

    //地图的fragment
    private MapFragment mapFragment=new MapFragment();
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showFragment(mapFragment, true, R.id.fragment_map);
    }


    /**
     * 开启fragment
     * @param fg
     */
    private void showFragment(final Fragment fg, final boolean b, final int containerViewId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (b) {
            if (!fg.isAdded()) {
                fragmentTransaction.add(containerViewId, fg);
            }
        } else {
            if (fg.isAdded()) {
                fragmentTransaction.remove(fg);
            }
        }
        fragmentTransaction.commitAllowingStateLoss();
    }
}
