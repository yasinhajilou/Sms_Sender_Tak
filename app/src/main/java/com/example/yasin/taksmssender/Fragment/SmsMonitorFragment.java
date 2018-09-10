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
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmsMonitorFragment extends Fragment {


    String allIds;
    AppCompatSpinner spinnerYears, spinnerMonths;
    SQLiteDatabase database;
    SQLiteOpenHelperTak helperTak;
    PieChart pieChart;
    ArrayList<String> xVals;
    ArrayList<Entry> yvalues;
    PieDataSet dataSet;
    PieData data;
    View view;
    String[] timeId;
    String[] dateId;
    String[] year;
    String[] month;
    String[] day;

    public SmsMonitorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sms_monitor, container, false);
        spinnerYears = view.findViewById(R.id.spinnerYearSmsMonitor);
        spinnerMonths = view.findViewById(R.id.spinnerMonthSmsMonitor);
        pieChart = (PieChart) view.findViewById(R.id.piechart);


        getSmsCounterInf();

        setUpMonthSpinner();
        setUpYearSpinner();

        //spinner for showing years
        spinnerYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
//                Toast.makeText(getContext(), "" + selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //spinner for showing months
        spinnerMonths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                switch (position) {
                    case 0: {
                        try {
                            Cursor datesCursor = getSmsDates("1397", "01");
                            if (datesCursor != null) {
                                String[] datesId = new String[datesCursor.getCount()];
                                int counter = 0;
                                while (datesCursor.moveToNext()) {
                                    datesId[counter] = datesCursor.getString(datesCursor.getColumnIndex(DateEntry.ID));
                                    counter++;
                                }
                                int[] counts = getSmsCounts(datesId);
                                if (counts.length == 1) {
                                    setUpPieChart(0, 0);
                                } else {
                                    setUpPieChart(counts[1], counts[0]);
                                }
                            } else {
                                setUpPieChart(0, 0);
                            }
                        } catch (Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    case 1: {
                        try {
                            Cursor datesCursor = getSmsDates("1397", "02");
                            if (datesCursor != null) {
                                String[] datesId = new String[datesCursor.getCount()];
                                int counter = 0;
                                while (datesCursor.moveToNext()) {
                                    datesId[counter] = datesCursor.getString(datesCursor.getColumnIndex(DateEntry.ID));
                                    counter++;
                                }
                                int[] counts = getSmsCounts(datesId);
                                if (counts.length == 1) {
                                    setUpPieChart(0, 0);
                                } else {
                                    setUpPieChart(counts[1], counts[0]);
                                }
                            } else {
                                setUpPieChart(0, 0);
                            }
                        } catch (Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    case 2: {
                        try {
                            Cursor datesCursor = getSmsDates("1397", "03");
                            if (datesCursor != null) {
                                String[] datesId = new String[datesCursor.getCount()];
                                int counter = 0;
                                while (datesCursor.moveToNext()) {
                                    datesId[counter] = datesCursor.getString(datesCursor.getColumnIndex(DateEntry.ID));
                                    counter++;
                                }
                                int[] counts = getSmsCounts(datesId);
                                if (counts.length == 1) {
                                    setUpPieChart(0, 0);
                                } else {
                                    setUpPieChart(counts[1], counts[0]);
                                }
                            } else {
                                setUpPieChart(0, 0);
                            }
                        } catch (Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    case 3: {
                        try {
                            Cursor datesCursor = getSmsDates("1397", "04");
                            if (datesCursor != null) {
                                String[] datesId = new String[datesCursor.getCount()];
                                int counter = 0;
                                while (datesCursor.moveToNext()) {
                                    datesId[counter] = datesCursor.getString(datesCursor.getColumnIndex(DateEntry.ID));
                                    counter++;
                                }
                                int[] counts = getSmsCounts(datesId);
                                if (counts.length == 1) {
                                    setUpPieChart(0, 0);
                                } else {
                                    setUpPieChart(counts[1], counts[0]);
                                }
                            } else {
                                setUpPieChart(0, 0);
                            }
                        } catch (Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    case 4: {
                        try {
                            Cursor datesCursor = getSmsDates("1397", "05");
                            if (datesCursor != null) {
                                String[] datesId = new String[datesCursor.getCount()];
                                int counter = 0;
                                while (datesCursor.moveToNext()) {
                                    datesId[counter] = datesCursor.getString(datesCursor.getColumnIndex(DateEntry.ID));
                                    counter++;
                                }
                                int[] counts = getSmsCounts(datesId);
                                if (counts.length == 1) {
                                    setUpPieChart(0, 0);
                                } else {
                                    setUpPieChart(counts[1], counts[0]);
                                }
                            } else {
                                setUpPieChart(0, 0);
                            }
                        } catch (Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    case 5: {
                        try {
                            Cursor datesCursor = getSmsDates("1397", "06");
                            if (datesCursor != null) {
                                String[] datesId = new String[datesCursor.getCount()];
                                int counter = 0;
                                while (datesCursor.moveToNext()) {
                                    datesId[counter] = datesCursor.getString(datesCursor.getColumnIndex(DateEntry.ID));
                                    counter++;
                                }
                                int[] counts = getSmsCounts(datesId);
                                if (counts.length == 1) {
                                    setUpPieChart(0, 0);
                                } else {
                                    setUpPieChart(counts[1], counts[0]);
                                }
                            } else {
                                setUpPieChart(0, 0);
                            }
                        } catch (Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    case 6:{
                        try {
                            Cursor datesCursor = getSmsDates("1397", "07");
                            if (datesCursor != null) {
                                String[] datesId = new String[datesCursor.getCount()];
                                int counter = 0;
                                while (datesCursor.moveToNext()) {
                                    datesId[counter] = datesCursor.getString(datesCursor.getColumnIndex(DateEntry.ID));
                                    counter++;
                                }
                                int[] counts = getSmsCounts(datesId);
                                if (counts.length == 1) {
                                    setUpPieChart(0, 0);
                                } else {
                                    setUpPieChart(counts[1], counts[0]);
                                }
                            } else {
                                setUpPieChart(0, 0);
                            }
                        }catch (Exception e){
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    case 7:{
                        try {
                            Cursor datesCursor = getSmsDates("1397", "08");
                            if (datesCursor != null) {
                                String[] datesId = new String[datesCursor.getCount()];
                                int counter = 0;
                                while (datesCursor.moveToNext()) {
                                    datesId[counter] = datesCursor.getString(datesCursor.getColumnIndex(DateEntry.ID));
                                    counter++;
                                }
                                int[] counts = getSmsCounts(datesId);
                                if (counts.length == 1) {
                                    setUpPieChart(0, 0);
                                } else {
                                    setUpPieChart(counts[1], counts[0]);
                                }
                            } else {
                                setUpPieChart(0, 0);
                            }
                        }catch (Exception e){
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    case 8:{
                        try {
                            Cursor datesCursor = getSmsDates("1397", "09");
                            if (datesCursor != null) {
                                String[] datesId = new String[datesCursor.getCount()];
                                int counter = 0;
                                while (datesCursor.moveToNext()) {
                                    datesId[counter] = datesCursor.getString(datesCursor.getColumnIndex(DateEntry.ID));
                                    counter++;
                                }
                                int[] counts = getSmsCounts(datesId);
                                if (counts.length == 1) {
                                    setUpPieChart(0, 0);
                                } else {
                                    setUpPieChart(counts[1], counts[0]);
                                }
                            } else {
                                setUpPieChart(0, 0);
                            }
                        }catch (Exception e){
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    case 9:{
                        try {
                            Cursor datesCursor = getSmsDates("1397", "10");
                            if (datesCursor != null) {
                                String[] datesId = new String[datesCursor.getCount()];
                                int counter = 0;
                                while (datesCursor.moveToNext()) {
                                    datesId[counter] = datesCursor.getString(datesCursor.getColumnIndex(DateEntry.ID));
                                    counter++;
                                }
                                int[] counts = getSmsCounts(datesId);
                                if (counts.length == 1) {
                                    setUpPieChart(0, 0);
                                } else {
                                    setUpPieChart(counts[1], counts[0]);
                                }
                            } else {
                                setUpPieChart(0, 0);
                            }
                        }catch (Exception e){
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    case 10:{
                        try {
                            Cursor datesCursor = getSmsDates("1397", "11");
                            if (datesCursor != null) {
                                String[] datesId = new String[datesCursor.getCount()];
                                int counter = 0;
                                while (datesCursor.moveToNext()) {
                                    datesId[counter] = datesCursor.getString(datesCursor.getColumnIndex(DateEntry.ID));
                                    counter++;
                                }
                                int[] counts = getSmsCounts(datesId);
                                if (counts.length == 1) {
                                    setUpPieChart(0, 0);
                                } else {
                                    setUpPieChart(counts[1], counts[0]);
                                }
                            } else {
                                setUpPieChart(0, 0);
                            }
                        }catch (Exception e){
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    case 11:{
                        try {
                            Cursor datesCursor = getSmsDates("1397", "12");
                            if (datesCursor != null) {
                                String[] datesId = new String[datesCursor.getCount()];
                                int counter = 0;
                                while (datesCursor.moveToNext()) {
                                    datesId[counter] = datesCursor.getString(datesCursor.getColumnIndex(DateEntry.ID));
                                    counter++;
                                }
                                int[] counts = getSmsCounts(datesId);
                                if (counts.length == 1) {
                                    setUpPieChart(0, 0);
                                } else {
                                    setUpPieChart(counts[1], counts[0]);
                                }
                            } else {
                                setUpPieChart(0, 0);
                            }
                        }catch (Exception e){
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return view;

    }


    private Cursor getSmsDates(String year, String month) {
        try {
            helperTak = new SQLiteOpenHelperTak(getContext());
            database = helperTak.getReadableDatabase();
            if (allIds != null) {
                String SQL = "SELECT * FROM " + DateEntry.TABLE_NAME_DATE + " WHERE " + DateEntry.ID + " IN " + allIds + " AND " + DateEntry.COLUMN_YEAR + " ==?" + " AND " + DateEntry.COLUMN_MONTH + "==?";
                Cursor cursor = database.rawQuery(SQL, new String[]{year, month});
                if (cursor.getCount() > 0) {
                    return cursor;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public int[] getSmsCounts(String[] dateIds) {
        helperTak = new SQLiteOpenHelperTak(getContext());
        database = helperTak.getReadableDatabase();
        String arrayIds = Arrays.toString(dateIds);
        String targetIds = arrayIds.replace('[', '(').replace(']', ')');
        String SQL = "SELECT * FROM " + CounterEntry.TABLE_NAME_COUNTER + " WHERE " + CounterEntry.COLUMN_DATE + " IN " + targetIds;
        Cursor counterCursor = database.rawQuery(SQL, new String[]{});
        if (counterCursor.getCount() > 0) {
            int[] counts = new int[2];
            counts[0] = 0;
            counts[1] = 0;
            while (counterCursor.moveToNext()) {
                counts[1] = counts[1] + counterCursor.getInt(counterCursor.getColumnIndex(CounterEntry.COLUMN_WIFI_COUNTER));
                counts[0] = counts[0] + counterCursor.getInt(counterCursor.getColumnIndex(CounterEntry.COLUMN_SIMCARD_COUNTER));
            }
            counterCursor.close();
            return counts;
        } else {
            return new int[]{0};
        }
    }

    // get all of the sms counter records for finding date Ids
    public void getSmsCounterInf() {
        helperTak = new SQLiteOpenHelperTak(getContext());
        database = helperTak.getReadableDatabase();
        String SQL = "SELECT * FROM " + CounterEntry.TABLE_NAME_COUNTER;
        Cursor counterCursor = database.rawQuery(SQL, new String[]{});
        if (counterCursor.getCount() > 0) {
            dateId = new String[counterCursor.getCount()];
            timeId = new String[counterCursor.getCount()];

            int counter = 0;
            while (counterCursor.moveToNext()) {
                dateId[counter] = counterCursor.getString(counterCursor.getColumnIndex(CounterEntry.COLUMN_DATE));
//                timeId[counter] = counterCursor.getString(counterCursor.getColumnIndex(CounterEntry.COLUMN_TIME));
                counter++;
            }
            counterCursor.close();
        }
        String arrayIds = Arrays.toString(dateId) + "";
        if (!arrayIds.isEmpty()) {
            allIds = arrayIds.replace('[', '(').replace(']', ')');
        } else {
            allIds = "(0)";
        }
    }

    public void setUpYearSpinner() {
        //set up spinners
        List<String> list = new ArrayList<>();
        list.add("1397");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                view.getContext(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYears.setAdapter(adapter);
    }

    public void setUpMonthSpinner() {
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

    }

    public void setUpPieChart(int wifi, int simCard) {
        yvalues = new ArrayList<Entry>();
        yvalues.add(new Entry(wifi, 0));
        yvalues.add(new Entry(simCard, 1));


        dataSet = new PieDataSet(yvalues, "");
        xVals = new ArrayList<String>();

        xVals.add("WIFI");
        xVals.add("SIM card");


        data = new PieData(xVals, dataSet);

        data.setValueFormatter(new DefaultValueFormatter(1));

        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        pieChart.setData(data);
        pieChart.setDescription("hello" + "\n" + "by");
        pieChart.animateXY(1400, 1400);
    }
}
