package com.example.yasin.taksmssender.Fragment;


import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.yasin.taksmssender.R;
import com.example.yasin.taksmssender.db.Contracts.DateInformation.DateEntry;
import com.example.yasin.taksmssender.db.Contracts.SmsCounterContract.CounterEntry;
import com.example.yasin.taksmssender.db.SQLiteOpenHelper;
import com.example.yasin.taksmssender.db.SQLiteOpenHelperTak;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmsMonitorFragment extends Fragment {


    AppCompatSpinner spinner;
    SQLiteDatabase database;
    SQLiteOpenHelperTak helperTak;
    Cursor counterCursor;
    Cursor dateCursor;
    String year;
    String month;
    int simcardCount = 0;
    int wifiCount = 0 ;
    public SmsMonitorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sms_monitor, container, false);
        final PieChart pieChart = (PieChart) view.findViewById(R.id.piechart);
        spinner = view.findViewById(R.id.spinnerYearSmsMonitor);
        AppCompatSpinner spinnerMonths = view.findViewById(R.id.spinnerMonthSmsMonitor);
        helperTak = new SQLiteOpenHelperTak(getContext());
        database = helperTak.getReadableDatabase();

        List<String> list = new ArrayList<>();
        list.add("1397");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                view.getContext(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        List<String> list1 = new ArrayList<>();
        list1.add("فروردین");
        list1.add("اردیبهشت");
        list1.add("خرداد");
        list1.add("تیر");
        list1.add("مرداد");
        list1.add("شهریور");
        list1.add("مهر");
        list1.add("آبان");
        list1.add("آذر");
        list1.add("دی");
        list1.add("بهمن");
        list1.add("اسفند");

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                view.getContext(), android.R.layout.simple_spinner_item, list1);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonths.setAdapter(adapter2);


        pieChart.setUsePercentValues(true);

        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        yvalues.add(new Entry(simcardCount, 0));
        yvalues.add(new Entry(wifiCount, 1));


        PieDataSet dataSet = new PieDataSet(yvalues, "Election Results");
        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add("سیم کارت");
        xVals.add("wifi");

        final PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());

        pieChart.setData(data);

        data.setValueTextSize(13f);
        data.setValueTextColor(Color.DKGRAY);

        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
//        pieChart.setDescription("SimCard :" +simCounter + "  Wifi : " + wifiCounter);


        //spinner for showing years
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                year = selectedItem;
                Toast.makeText(getContext(), ""+selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerMonths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                switch (position){
                    case 0:
                        getData(pieChart , "01" , year);
                        break;
                    case 1 :
                        getData(pieChart , "02" , year);
                        break;
                    case 2 :
                        getData(pieChart , "03" , year);
                        break;
                    case 3:
                        getData(pieChart , "04" , year);
                        break;
                    case 4:
                        getData(pieChart , "05" , year);
                        break;
                    case 5:
                        getData(pieChart , "06" , year);
                        break;
                    case 6:
                        getData(pieChart , "07" , year);
                        break;
                    case 7:
                        getData(pieChart , "08" , year);
                        break;
                    case 8:
                        getData(pieChart , "09" , year);
                        break;
                    case 9:
                        getData(pieChart , "10" , year);
                        break;
                    case 10:
                        getData(pieChart , "11" , year);
                        break;
                    case 11:
                        getData(pieChart , "12",year);
                        break;



                }
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        return view;

    }

    public void getData (PieChart pieChart , String selectedItem , String year){
        month = selectedItem;
        dateCursor = database.rawQuery("SELECT " + DateEntry.ID + " FROM " + DateEntry.TABLE_NAME_DATE + " WHERE " + DateEntry.COLUMN_YEAR + "=? and "+ DateEntry.COLUMN_MONTH + "=?",new String[]{year+"" , month+""} );
        if (dateCursor.getCount() >0){
            int[] dateId = new int[dateCursor.getCount()];
            int i = 0;
            while (dateCursor.moveToNext()){
                dateId[i] = dateCursor.getInt(dateCursor.getColumnIndex(DateEntry.ID));
                i++;
            }
            for (int j=0 ; j< dateId.length ; j++){
                counterCursor = database.rawQuery("SELECT "+ CounterEntry.COLUMN_SIMCARD_COUNTER +"," + CounterEntry.COLUMN_WIFI_COUNTER +" FROM " + CounterEntry.TABLE_NAME_COUNTER + " WHERE " + CounterEntry.COLUMN_DATE + "=?",new String[]{dateId[j]+""});
                if (counterCursor != null){
                    try {
                        simcardCount = simcardCount + counterCursor.getInt(counterCursor.getColumnIndex(CounterEntry.COLUMN_SIMCARD_COUNTER));
                        wifiCount = wifiCount + counterCursor.getInt(counterCursor.getColumnIndex(CounterEntry.COLUMN_WIFI_COUNTER));
                    }catch (Exception e){
                        Log.d("Error" , e.getMessage());
                    }

                }
            }
        }
        pieChart.setUsePercentValues(true);

        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        yvalues.add(new Entry(simcardCount, 0));
        yvalues.add(new Entry(wifiCount, 1));


        PieDataSet dataSet = new PieDataSet(yvalues, "Election Results");
        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add("سیم کارت");
        xVals.add("wifi");

        final PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());

        pieChart.setData(data);

        data.setValueTextSize(13f);
        data.setValueTextColor(Color.DKGRAY);

        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

    }
}
