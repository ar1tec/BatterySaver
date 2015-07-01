package pradeet.batterysaver.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import pradeet.batterysaver.Fragment1;
import pradeet.batterysaver.Fragment2;
import pradeet.batterysaver.Fragment3;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    public Fragment getItem(int num) {
        Fragment fragment = null;
        switch(num){
            case 0:
                fragment = new Fragment1();
                break;
            case 1:
                fragment = new Fragment2();
                break;
        }
        return fragment;
    }
    @Override
    public int getCount() {
        return 2;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        String title;
        if(position == 0){
            title = "Battery";
        } else {
            title = "About";
        }
        return title;
    }
}
