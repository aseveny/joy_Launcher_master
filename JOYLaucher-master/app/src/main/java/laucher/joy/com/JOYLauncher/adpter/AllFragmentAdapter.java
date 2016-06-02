package laucher.joy.com.JOYLauncher.adpter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by xugz on 15-12-17.
 */
public class AllFragmentAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> list;

    public AllFragmentAdapter(FragmentManager fm) {
        super(fm);
    }
     public AllFragmentAdapter(FragmentManager fm, ArrayList<Fragment> list){
        super(fm);
        this.list = list;
    }
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
