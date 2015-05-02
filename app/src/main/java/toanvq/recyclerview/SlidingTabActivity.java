package toanvq.recyclerview;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import toanvq.recyclerview.slidingtab.SlidingTabLayout;


public class SlidingTabActivity extends AppCompatActivity {
    private final String[] TITLES = {"Categories", "Home", "Top Paid", "Top Free", "Top Grossing", "Top New Paid",
            "Top New Free", "Trending"};
    List<Integer> color = new ArrayList<>();
    private SlidingTabAdapter adapter;
    private SlidingTabLayout tabbar;
    private int current_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_tab);

        tabbar = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        adapter = new SlidingTabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabbar.setDistributeEvenly(true);
        tabbar.setViewPager(viewPager);

        tabbar.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.WHITE;
            }
        });
//        getSupportActionBar().setElevation(0);

        color.add(R.color.bg_1);
        color.add(R.color.bg_2);
        color.add(R.color.bg_3);
        color.add(R.color.bg_4);
        color.add(R.color.bg_5);
        color.add(R.color.bg_6);

        current_color = color.get(0);
        setStyleColor(current_color);

        tabbar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.i("TabScroll", "Position " + position + ", offset " + positionOffset + ", offsetPixel " + positionOffsetPixels + ", TabX " + tabbar.getScrollX());
//                if (positionOffset > 0f && position < (tab_count - 1)) {
//                    current_color = Utils.blendColors(color.get((position + 1) % 6), color.get(position % 6), positionOffset);
//                }
//                Log.i("Color", "run " + current_color);
//                setStyleColor(current_color);
            }

            @Override
            public void onPageSelected(int position) {
                current_color = color.get(position % 6);
                setStyleColor(current_color);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sliding_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class SlidingTabAdapter extends FragmentPagerAdapter {

        public SlidingTabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return NewsListFragment.newInstance("" + position, "2");
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }

    private void setStyleColor(int c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Utils.getTintColor(getResources().getColor(c)));
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(c)));
        tabbar.setBackgroundColor(getResources().getColor(c));
    }
}
