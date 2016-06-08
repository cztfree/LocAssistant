package com.cztfree.locassistant.Activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.cztfree.locassistant.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity" ;

    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragments = new ArrayList<Fragment>();

    /**
     * 底部四个按钮
     */
    private LinearLayout mTabBtnSatellite;
    private LinearLayout mTabBtnMap;
    private LinearLayout mTabBtnCommunication;
    private LinearLayout mTabBtnCantacts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initView();

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
        {

            @Override
            public int getCount()
            {
                return mFragments.size();
            }

            @Override
            public Fragment getItem(int position)
            {
                Log.v(TAG,""+position);
                return mFragments.get(position);
            }
        };

        mViewPager.setAdapter(mAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int currentIndex;

            @Override
            public void onPageSelected(int position) {
                resetTabBtn();
                switch (position) {
                    case 0:
                        ((ImageButton) mTabBtnSatellite.findViewById(R.id.btn_satellite))
                                .setImageResource(R.drawable.btn_satellite_on);
                        break;
                    case 1:
                        ((ImageButton) mTabBtnMap.findViewById(R.id.btn_map))
                                .setImageResource(R.drawable.btn_map_on);
                        break;
                    case 2:
                        ((ImageButton) mTabBtnCommunication.findViewById(R.id.btn_communication))
                                .setImageResource(R.drawable.btn_communication_on);
                        break;
                    case 3:
                        ((ImageButton) mTabBtnCantacts.findViewById(R.id.btn_cantacts))
                                .setImageResource(R.drawable.btn_cantacts_on);
                        break;
                }

                currentIndex = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

    }
    protected void resetTabBtn()
    {
        ((ImageButton) mTabBtnSatellite.findViewById(R.id.btn_satellite))
                .setImageResource(R.drawable.btn_satellite_off);
        ((ImageButton) mTabBtnMap.findViewById(R.id.btn_map))
                .setImageResource(R.drawable.btn_map_off);
        ((ImageButton) mTabBtnCommunication.findViewById(R.id.btn_communication))
                .setImageResource(R.drawable.btn_communication_off);
        ((ImageButton) mTabBtnCantacts.findViewById(R.id.btn_cantacts))
                .setImageResource(R.drawable.btn_cantacts_off);
    }

    private void initView()
    {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mTabBtnSatellite = (LinearLayout) findViewById(R.id.tab_satellite);
        mTabBtnMap = (LinearLayout) findViewById(R.id.tab_map);
        mTabBtnCommunication = (LinearLayout) findViewById(R.id.tab_communication);
        mTabBtnCantacts = (LinearLayout) findViewById(R.id.tab_cantacts);

        SatelliteFragment tabSatellite = new SatelliteFragment();
        MapFragment tabMap = new MapFragment();
        CommunicationFragment tabCommunication = new CommunicationFragment();
        CantactsFragment tabCantacts = new CantactsFragment();
        mFragments.add(tabSatellite);
        mFragments.add(tabMap);
        mFragments.add(tabCommunication);
        mFragments.add(tabCantacts);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
