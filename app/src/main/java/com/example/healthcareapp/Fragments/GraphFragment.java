package com.example.healthcareapp.Fragments;

import static android.content.ContentValues.TAG;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.healthcareapp.AddWaterActivity;
import com.example.healthcareapp.Model.bmiInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import com.example.healthcareapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class GraphFragment extends Fragment {
    String uid = FirebaseAuth.getInstance().getUid();

    GraphView graph;

    private Spinner spinnerDateRange, spinnerMeasurement;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        //Spinner
        spinnerMeasurement = (Spinner)view.findViewById(R.id.spinnerMeasurement);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(), R.array.selectMeasurement, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMeasurement.setAdapter(adapter);

        spinnerDateRange = (Spinner)view.findViewById(R.id.spinnerDateRange);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(view.getContext(), R.array.selectDateRange, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDateRange.setAdapter(adapter2);

        //Line Graph
        graph = view.findViewById(R.id.graphView);


        getEntries();
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getContext(), new SimpleDateFormat("dd/MM")));

        return view;
    }

    private void getEntries() {

        Query query = FirebaseDatabase.getInstance().getReference("bmiDiary").child(uid).orderByChild("time");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                DataPoint[] dataPoints = new DataPoint[7];
                ArrayList<bmiInfo> bmiInfos = new ArrayList<>();
                ArrayList<Date> dates = new ArrayList<>();

                Calendar calendar = Calendar.getInstance();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    bmiInfo bmiInfo = ds.getValue(bmiInfo.class);
                    bmiInfos.add(bmiInfo);
                }
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                int index = 0;
                for (int j = 6; j >= 0; j--){
                    ArrayList<bmiInfo> bmiList = new ArrayList<>();
                    calendar.set(Calendar.HOUR, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.add(Calendar.DATE, -j);
                    calendar.setTimeInMillis(calendar.getTimeInMillis());
                    Date d = null;
                    try {
                        d = df.parse(DateFormat.format("dd/MM/yyyy", calendar).toString());

                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    for(int i = 0; i < bmiInfos.size(); i++){

                        calendar.setTimeInMillis(bmiInfos.get(i).time);
                        Date date = null;
                        try {
                            date = df.parse(DateFormat.format("dd/MM/yyyy", calendar).toString());

                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        if (date.compareTo(d) <= 0) {
                            bmiList.add(bmiInfos.get(i));
                        }

                    }
                    if(bmiList.size() <= 0){
                        dataPoints[index] = new DataPoint(d, Integer.parseInt(bmiInfos.get(0).weight));


                    }
                    else{
                        dataPoints[index] = new DataPoint(d, Integer.parseInt(bmiList.get(bmiList.size()-1).weight));
                    }
                    index++;
                }
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
                graph.addSeries(series);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Error:" + error.getMessage());
            }
        });
    }

}
