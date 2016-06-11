package com.cztfree.locassistant.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.cztfree.locassistant.R;
import com.cztfree.locassistant.UI.MyYAxisValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Iterator;

public class SatelliteFragment extends Fragment implements LocationListener{

    private final static String TAG = "SatellisteFragment  ";
    protected BarChart mChart;
    private Typeface mTf;
    private TextView tv;
    private LocationManager lm;
    private View rootView;
    private Activity rootActivity;

    private ArrayList<GpsSatellite> satelliteList ;

    protected String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };

    public SatelliteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_satellite, container, false);
        rootActivity = getActivity();

        tv = (TextView) rootView.findViewById(R.id.tv);

        Log.v(TAG, "onCreateView");
        initGPS();//初始化GPS
        initChart();//初始化GPS图表

        return rootView;
    }

    private void initGPS() {

        lm = (LocationManager) rootActivity.getSystemService(Context.LOCATION_SERVICE);

        GpsStatus.Listener mGpsStatusListener = new GpsStatus.Listener() {

            @Override
            public void onGpsStatusChanged(int event) {
                switch (event) {
                    // 第一次定位
                    case GpsStatus.GPS_EVENT_FIRST_FIX:
                        Log.i(TAG, "第一次定位");
                        break;
                    // 卫星状态改变
                    case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                        Log.i(TAG, "卫星状态改变");
                        // 获取当前状态
                        GpsStatus gpsStatus = lm.getGpsStatus(null);
                        // 获取卫星颗数的默认最大值
                        int maxSatellites = gpsStatus.getMaxSatellites();
                        // 创建一个迭代器保存所有卫星
                        Iterator<GpsSatellite> iters = gpsStatus.getSatellites()
                                .iterator();
                        int count = 0;
                        float max = 0.0f;
                        satelliteList = new ArrayList<GpsSatellite>(maxSatellites);
                        Log.v(TAG, ""+satelliteList.size());
                        while (iters.hasNext() && count <= maxSatellites) {
                            GpsSatellite s = iters.next();
                            satelliteList.add(count,s);
                            count++;

                            if(max < s.getSnr()){
                                max = s.getSnr();
                            }

                        }
                        Log.v(TAG, ""+maxSatellites+"   "+count);

                        if(count != 0){
                            Log.v(TAG, "更新表格");
                            setData(count, max);
                            mChart.invalidate();
                        }

                        System.out.println("搜索到：" + count + "颗卫星");
                        break;
                    // 定位启动
                    case GpsStatus.GPS_EVENT_STARTED:
                        Log.i(TAG, "定位启动");
                        break;
                    // 定位结束
                    case GpsStatus.GPS_EVENT_STOPPED:
                        Log.i(TAG, "定位结束");
                        break;
                }
            }
        };
//        lm.addNmeaListener(this);
         lm.addGpsStatusListener(mGpsStatusListener);
        if (ActivityCompat.checkSelfPermission(rootActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(rootActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
    }

    private void initChart() {
        //表格设置
        mChart = (BarChart) rootView.findViewById(R.id.chart1);
//        mChart.setOnChartValueSelectedListener(rootActivity);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setDescription("");
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        mTf = Typeface.createFromAsset(rootActivity.getAssets(), "OpenSans-Regular.ttf");

        //X轴设置
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(true);
        xAxis.setSpaceBetweenLabels(1);

        //左Y轴设置
        YAxisValueFormatter custom = new MyYAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(10f);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        //右Y轴设置
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setTypeface(mTf);
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(10f);
        rightAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        //图例设置
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });

        //setData(12, 50);
    }

    private void setData(int count, float range) {

        ArrayList<String> xVals = new ArrayList<String>(count);
        for (int i = 0; i < count; i++) {
//            xVals.add(mMonths[i % 12]);
            xVals.add(i,""+ satelliteList.get(i).getPrn());
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        int[] colors = new int[count];

        for (int i = 0; i < count; i++) {
            float mult = (range + 1);
//            float val = (float) (Math.random() * mult);
            yVals1.add(i,new BarEntry(satelliteList.get(i).getSnr(), i));

            if(satelliteList.get(i).usedInFix()){
                colors[i] = Color.rgb(00,255,00);
            }else{
                colors[i] = Color.rgb(100,100,100);
            }
            Log.v(TAG,""+i+"  "+colors[i]);
        }


        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            Log.v(TAG, "if  ");
            set1 = (BarDataSet)mChart.getData().getDataSetByIndex(0);
            set1.setYVals(yVals1);
            set1.setColors(colors);
            mChart.getData().setXVals(xVals);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            Log.v(TAG, "else  ");
            set1 = new BarDataSet(yVals1, "DataSet");
            set1.setBarSpacePercent(35f);
//            set1.setColors(ColorTemplate.MATERIAL_COLORS);
            set1.setColors(colors);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(xVals, dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(mTf);

            mChart.setData(data);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
