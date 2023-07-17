package com.example.healthcareapp.Fragments;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.healthcareapp.Model.bmiInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.example.healthcareapp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HeightGraphFragment extends Fragment {
    String uid = FirebaseAuth.getInstance().getUid();

    LineChart lineChart;
    LineData lineData;

    LineDataSet lineDataSet;
    ArrayList <Date> datesOneWeek, datesOneMonth;
    ArrayList<String> monthsOneYear;
    private Spinner spinnerDateRange;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_height_graph, container, false);
        spinnerDateRange = (Spinner)view.findViewById(R.id.spinnerDateRange);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(), R.array.selectDateRange, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDateRange.setAdapter(adapter);

        //lineChart
        lineChart = view.findViewById(R.id.lineChart);
        getEntriesHeightOneWeek();

        //spn change value
        spinnerDateRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(spinnerDateRange.getSelectedItemPosition()){
                    case 0:
                        getEntriesHeightOneWeek();
                        break;
                    case 1:
                        getEntriesHeightOneMonth();
                        break;
                    case 2:
                        getEntriesHeightOneYear();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }
    //get entries

    private void getEntriesHeightOneWeek() {
        getDateOneWeek();
        Query query = FirebaseDatabase.getInstance().getReference("bmiDiary").child(uid).orderByChild("time");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                ArrayList<Entry> entries = new ArrayList();
                ArrayList<bmiInfo> bmiInfos = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    bmiInfo bmiInfo = ds.getValue(bmiInfo.class);
                    bmiInfos.add(bmiInfo);
                }
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                for (int j = 0; j <= 6; j++){
                    Calendar calendar = Calendar.getInstance();
                    ArrayList<bmiInfo> bmiList = new ArrayList<>();

                    for(int i = 0; i < bmiInfos.size(); i++){

                        calendar.setTimeInMillis(bmiInfos.get(i).time);
                        Date date = null;
                        try {
                            date = df.parse(DateFormat.format("dd/MM/yyyy", calendar).toString());

                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        if (date.compareTo(datesOneWeek.get(j)) <= 0) {
                            bmiList.add(bmiInfos.get(i));
                        }

                    }
                    if(bmiList.size() <= 0){
                        entries.add(new Entry(j, Integer.parseInt(bmiInfos.get(0).height)));


                    }
                    else{
                        entries.add(new Entry(j, Integer.parseInt(bmiList.get(bmiList.size()-1).height)));

                    }

                }
                showChartForWeek(entries, datesOneWeek, "Height");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Error:" + error.getMessage());
            }
        });
    }
    private void getEntriesHeightOneMonth() {
        getDateOneMonth();
        Query query = FirebaseDatabase.getInstance().getReference("bmiDiary").child(uid).orderByChild("time");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                ArrayList<bmiInfo> bmiInfos = new ArrayList<>();
                ArrayList<Entry> entries = new ArrayList();;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    bmiInfo bmiInfo = ds.getValue(bmiInfo.class);
                    bmiInfos.add(bmiInfo);
                }
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                for (int j = 0; j < datesOneMonth.size(); j++){
                    Calendar calendar = Calendar.getInstance();
                    ArrayList<bmiInfo> bmiList = new ArrayList<>();
                    for(int i = 0; i < bmiInfos.size(); i++){

                        calendar.setTimeInMillis(bmiInfos.get(i).time);
                        Date date = null;
                        try {
                            date = df.parse(DateFormat.format("dd/MM/yyyy", calendar).toString());

                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        if (date.compareTo(datesOneMonth.get(j)) <= 0) {
                            bmiList.add(bmiInfos.get(i));
                        }

                    }
                    if(bmiList.size() <= 0){
                        entries.add(new Entry(j, Integer.parseInt(bmiInfos.get(0).height)));
                    }
                    else{
                        entries.add(new Entry(j, Integer.parseInt(bmiList.get(bmiList.size()-1).height)));
                    }
                }
                showChartForMonth(entries, datesOneMonth, "Height");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Error:" + error.getMessage());
            }
        });
    }
    private void getEntriesHeightOneYear() {
        getMonthOneYear();
        Query query = FirebaseDatabase.getInstance().getReference("bmiDiary").child(uid).orderByChild("time");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                ArrayList<bmiInfo> bmiInfos = new ArrayList<>();
                ArrayList<Entry> entries = new ArrayList();;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    bmiInfo bmiInfo = ds.getValue(bmiInfo.class);
                    bmiInfos.add(bmiInfo);
                }
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                for (int j = 0; j < monthsOneYear.size(); j++){
                    Calendar calendar = Calendar.getInstance();
                    ArrayList<bmiInfo> bmiList = new ArrayList<>();
                    for(int i = 0; i < bmiInfos.size(); i++){

                        calendar.setTimeInMillis(bmiInfos.get(i).time);
                        String date = DateFormat.format("MM/yyyy", calendar).toString();
                        String[] stringDate = date.split("/");
                        String[] stringD = monthsOneYear.get(j).split("/");
                        if (stringDate[1].compareTo(stringD[1]) < 0 || (stringDate[1].compareTo(stringD[1]) == 0 && stringDate[0].compareTo(stringD[0]) <= 0)) {
                            bmiList.add(bmiInfos.get(i));
                        }

                    }
                    if(bmiList.size() <= 0){
                        entries.add(new Entry(j, Integer.parseInt(bmiInfos.get(0).height)));
                    }
                    else{
                        entries.add(new Entry(j, Integer.parseInt(bmiList.get(bmiList.size()-1).height)));
                    }
                }
                showChartForYear(entries, monthsOneYear, "Height");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Error:" + error.getMessage());
            }
        });
    }

    //show chart
    public void showChartForYear(ArrayList<Entry> entries, ArrayList<String> dates, String item){
        lineDataSet = new LineDataSet(entries, item);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setValueTextColor(Color.rgb(73, 190, 37));
        lineData = new LineData(lineDataSet);
        lineChart.clear();
        lineChart.setData(lineData);
        lineChart.getDescription().setText("");
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setTextColor(Color.rgb(128, 168, 232));
        lineChart.getAxisLeft().setTextColor(Color.rgb(128, 168, 232));
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        SimpleDateFormat df = new SimpleDateFormat("MM/yyyy");
        lineChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int index = (int) value;
                if (dates.size() > index && index >= 0)
                    return dates.get(index);
                return "";
            }
        });
    }
    public void showChartForMonth(ArrayList<Entry> entries, ArrayList<Date> dates, String item){
        lineDataSet = new LineDataSet(entries, item);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setValueTextSize(0f);
        lineDataSet.setDrawHighlightIndicators(false);
        lineData = new LineData(lineDataSet);
        lineChart.clear();
        lineChart.notifyDataSetChanged();
        lineChart.setData(lineData);
        lineChart.getDescription().setText("");
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setTextColor(Color.rgb(128, 168, 232));
        lineChart.getAxisLeft().setTextColor(Color.rgb(128, 168, 232));
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM");
        lineChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int index = (int) value;
                if (dates.size() > index && index >= 0)

                    return df.format(dates.get(index));
                return "";
            }
        });
    }
    public void showChartForWeek(ArrayList<Entry> entries, ArrayList<Date> dates, String item){
        lineDataSet = new LineDataSet(entries, item);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setValueTextColor(Color.rgb(73, 190, 37));
        lineData = new LineData(lineDataSet);
        lineChart.clear();
        lineChart.notifyDataSetChanged();
        lineChart.setData(lineData);
        lineChart.getDescription().setText("");
        lineChart.getXAxis().setTextColor(Color.rgb(128, 168, 232));
        lineChart.getAxisLeft().setTextColor(Color.rgb(128, 168, 232));
        lineChart.getAxisRight().setEnabled(false);

        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM");
        lineChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int index = (int) value;
                if (dates.size() > index && index >= 0)

                    return df.format(dates.get(index));
                return "";
            }
        });
    }

    //get data for chart
    public void getDateOneWeek(){
        datesOneWeek = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        for (int j = 0; j <= 6; j++){
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.add(Calendar.DATE, -(6-j));
            calendar.setTimeInMillis(calendar.getTimeInMillis());
            Date d = null;
            try {
                d = df.parse(DateFormat.format("dd/MM/yyyy", calendar).toString());
                datesOneWeek.add(d);

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void getDateOneMonth(){
        datesOneMonth = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        Long diffMilliseconds = new Date().getTime() - c.getTime().getTime();
        int diffDays = (int) Math.round((double) diffMilliseconds/(60*1000*60*24));

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        for (int j = 0; j <= diffDays; j++) {
            Calendar calendar = Calendar.getInstance();
            ArrayList<bmiInfo> bmiList = new ArrayList<>();
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.add(Calendar.DATE, -(diffDays - j));
            calendar.setTimeInMillis(calendar.getTimeInMillis());
            Date d = null;
            try {
                d = df.parse(DateFormat.format("dd/MM/yyyy", calendar).toString());
                datesOneMonth.add(d);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void getMonthOneYear(){
        monthsOneYear = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -1);
        c.setTime(c.getTime());
        int fromYear = c.get(Calendar.YEAR);
        int fromMonth = c.get(Calendar.MONTH);
        int fromDay = c.get(Calendar.DAY_OF_MONTH);
        c.setTime(Calendar.getInstance().getTime());
        int toYear = c.get(Calendar.YEAR);
        int toMonth = c.get(Calendar.MONTH);
        int toDay = c.get(Calendar.YEAR);
        int diffMonths = 12 * (toYear - fromYear) + (toMonth - fromMonth);

        for (int j = 0; j <= diffMonths; j++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.add(Calendar.MONTH, -(diffMonths - j));
            calendar.setTimeInMillis(calendar.getTimeInMillis());
            String d = DateFormat.format("MM/yyyy", calendar).toString();
            monthsOneYear.add(d);
        }
    }
}