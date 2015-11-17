package org.coursera.capstone.t1dteensclient.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.coursera.capstone.t1dteensclient.Constants;
import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.Utils;
import org.coursera.capstone.t1dteensclient.entities.Answer;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;

public class ChartsFragment extends Fragment implements OnChartValueSelectedListener {

    private static final int DAY = 0;
    private static final int WEEK = 1;
    private static final int TWOWEEKS = 2;
    private static final int MONTH = 3;
    private static final String PERIOD = "period";
    private static final String ANSWERS = "answers";
    private static final long NOW = System.currentTimeMillis();
    private static HashMap<Integer, String> moodMap;
    static {
        moodMap = new HashMap<>();
        moodMap.put(1, "really bad");
        moodMap.put(2, "really bad");
        moodMap.put(3, "sick");
        moodMap.put(4, "sick");
        moodMap.put(5, "so-so");
        moodMap.put(6, "so-so");
        moodMap.put(7, "good");
        moodMap.put(8, "good");
        moodMap.put(9, "excellent");
        moodMap.put(10, "excellent");
    }

    LineChart mChart;
    int period;
    ArrayList<String> xVals;
    TextView mSugarLast, mSugarAvr, mFellLast, mFeelAvr;
    ImageView mSugarTrend,mFeelTrend;

    public ChartsFragment() {
    }

    public static ChartsFragment newInstance(int sectionNumber, ArrayList<Answer> answers) {
        ChartsFragment fragment = new ChartsFragment();
        Bundle args = new Bundle();
        args.putInt(PERIOD, sectionNumber);
        args.putParcelableArrayList(ANSWERS, answers);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_charts, container, false);

        initChart(rootView);
        initTextViews(rootView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        ArrayList<Answer> answers = getArguments().getParcelableArrayList(ANSWERS);

        period = getArguments().getInt(PERIOD);
        long timeInThePast = 0;

        if (answers != null && answers.size() > 0) {

            ArrayList<Answer> useList = new ArrayList<>();
            switch (period) {

                case DAY:
                    timeInThePast = NOW - Constants.DAY_IN_MILLIS;
                    break;
                case WEEK:
                    timeInThePast = NOW - Constants.DAY_IN_MILLIS * 7;
                    break;
                case TWOWEEKS:
                    timeInThePast = NOW - Constants.DAY_IN_MILLIS * 14;
                    break;
                case MONTH:
                    timeInThePast = NOW - Constants.DAY_IN_MILLIS * 30;
                    break;
            }
            for (Answer answer : answers) {
                if (answer.getTimestamp().getTime() > timeInThePast){
                    useList.add(answer);
                }
            }

            mChart.setData(generateChartData(useList));
            mChart.invalidate();
        }
    }

    private void initTextViews(View rootView){

        mSugarLast = (TextView) rootView.findViewById(R.id.sugarLast);
        mSugarAvr = (TextView) rootView.findViewById(R.id.sugarAvr);
        mSugarTrend = (ImageView) rootView.findViewById(R.id.sugarTrend);
        mFellLast = (TextView) rootView.findViewById(R.id.feelLast);
        mFeelAvr = (TextView) rootView.findViewById(R.id.feelAvr);
        mFeelTrend = (ImageView) rootView.findViewById(R.id.feelTrend);
    }

    private void initChart(View rootView) {

        mChart = (LineChart) rootView.findViewById(R.id.chart);
        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawGridBackground(false);
        mChart.setDescription("");
        mChart.setDrawBorders(false);

        mChart.getAxisLeft().setDrawAxisLine(false);
        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.getAxisRight().setDrawAxisLine(false);
        mChart.getAxisRight().setDrawGridLines(false);
        mChart.getXAxis().setDrawAxisLine(false);
        mChart.getXAxis().setDrawGridLines(false);

        Legend legend = mChart.getLegend();
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
        legend.setTextSize(16);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setDrawLabels(false);
        xAxis.setLabelRotationAngle(270);
        xAxis.setTextSize(12);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisLineWidth(2);

        YAxis leftAxis = mChart.getAxisLeft();
//        leftAxis.setDrawGridLines(true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setDrawAxisLine(true);
        leftAxis.setAxisLineWidth(2);
        leftAxis.setTextSize(12);
        leftAxis.setAxisMaxValue(12);
        leftAxis.setAxisMinValue(2);
        leftAxis.setSpaceBottom(10);
        leftAxis.setSpaceTop(10);

        LimitLine ll1 = new LimitLine(10, "max after meals");
        LimitLine ll2 = new LimitLine(7, "max before meals");

        ll1.setTextSize(16);
        ll2.setTextSize(16);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);

        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);


        YAxis rightAxis = mChart.getAxisRight();
//        rightAxis.setDrawGridLines(true);
        rightAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        rightAxis.setDrawAxisLine(true);
        rightAxis.setAxisLineWidth(2);
        rightAxis.setTextSize(12);
        rightAxis.setAxisMaxValue(12);
        rightAxis.setAxisMinValue(0);
        rightAxis.setStartAtZero(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);
        mChart.setOnChartValueSelectedListener(this);

        CustomMarkerView mv = new CustomMarkerView(getActivity(), R.layout.marker_view);
        mChart.setMarkerView(mv);

    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {    }

    @Override
    public void onNothingSelected() {    }


    public class CustomMarkerView extends MarkerView {

        private TextView timestamp, value, value2;

        public CustomMarkerView (Context context, int layoutResource) {
            super(context, layoutResource);

            timestamp = (TextView) findViewById(R.id.timestamp);
            value = (TextView) findViewById(R.id.value);
            value2 = (TextView) findViewById(R.id.value2);
        }

        @Override
        public void refreshContent(Entry e, Highlight highlight) {

            ViewData data = (ViewData) e.getData();
            timestamp.setText(xVals.get(e.getXIndex()));
            value.setText(data.getValue());
            value2.setText(data.getWhen());
        }

        @Override
        public int getXOffset(float xpos) {

            return -(getWidth() / 2);
        }

        @Override
        public int getYOffset(float ypos) {

            return -(getHeight() + 10);
        }
    }

    private LineData generateChartData(ArrayList<Answer> answers) {

        ArrayList<Entry> sugarLevelValues = new ArrayList<>();
        ArrayList<Entry> moodValues = new ArrayList<>();
        xVals = new ArrayList<>();

        int i = 0;
        int j = 0;
        int e = 0;

        for (Answer answer : answers) {
            if (answer.getQuestionId() == 1) {

                sugarLevelValues.add(new Entry((float) answer.getValue(), i,
                        new ViewData(String.valueOf(answer.getText() + " mmol/l"), answers.get(e+2).getText())));
                xVals.add(new DateTime(answer.getTimestamp()).toString(Utils.getDateTimePattern(getActivity())));
                i++;
            } else if (answer.getQuestionId() == 2) {
                moodValues.add(new Entry((float) answer.getValue(), j,
                        new ViewData(String.valueOf("feeling " + answer.getText()), answers.get(e-4).getText())));
                j++;
            }
            e++;
        }

        LineDataSet setQuestion1 = new LineDataSet(sugarLevelValues, "Blood sugar");
        setQuestion1.setAxisDependency(YAxis.AxisDependency.LEFT);
        setQuestion1.setColor(Color.BLUE);
        setQuestion1.setLineWidth(4);
        setQuestion1.setCircleColor(Color.BLUE);
        setQuestion1.setDrawValues(true);

        LineDataSet setQuestion2 = new LineDataSet(moodValues, "Self-feeling");
        setQuestion2.setAxisDependency(YAxis.AxisDependency.RIGHT);
        setQuestion2.setColor(Color.RED);
        setQuestion2.setLineWidth(4);
        setQuestion2.setCircleColor(Color.RED);
        setQuestion2.setDrawValues(false);

        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        dataSets.add(setQuestion1);
        dataSets.add(setQuestion2);

        // statistics for the period

        ViewData data;

        float sugarAvr = 0;
        data = (ViewData) sugarLevelValues.get(sugarLevelValues.size()-1).getData();
        mSugarLast.setText(data.getValue());

        for (Entry value : sugarLevelValues) {
            sugarAvr += value.getVal();
        }

        sugarAvr = sugarAvr/sugarLevelValues.size();
        mSugarAvr.setText(String.format("%.1f", sugarAvr) + " mmol/l");

        float moodAvr = 0;
        data = (ViewData) moodValues.get(moodValues.size()-1).getData();
        mFellLast.setText(data.getValue().split(" ")[1]);

        for (Entry value : moodValues) {
            moodAvr += value.getVal();
        }
        moodAvr = moodAvr/moodValues.size();
        String moodAvrStr = moodMap.get(Math.round(moodAvr));
        mFeelAvr.setText(String.valueOf(moodAvrStr));

        if (sugarAvr < 6)
            mSugarTrend.setImageResource(R.drawable.good);
        else if (sugarAvr < 8)
            mSugarTrend.setImageResource(R.drawable.neutral);
        else
            mSugarTrend.setImageResource(R.drawable.bad);

        if (moodAvr < 6)
            mFeelTrend.setImageResource(R.drawable.bad);
        else
            mFeelTrend.setImageResource(R.drawable.good);

        return new LineData(xVals, dataSets);
    }

    private class ViewData {

        String value;
        String when;

        public ViewData(String value, String when) {
            this.value = value;
            this.when = when;
        }

        public String getValue() {
            return value;
        }

        public String getWhen() {
            return when;
        }
    }

}
