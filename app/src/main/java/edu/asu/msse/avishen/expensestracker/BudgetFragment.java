package edu.asu.msse.avishen.expensestracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormatSymbols;

/**
 * Copyright 2015 Avijit Singh Vishen.
 * <p/>
 * License to anyone who may want to rebuild and execute the application.
 * Right to Use given to TA and Instructor to build and evaluate the package
 * <p/>
 * Purpose: This fragment adds a new budget to the database
 *
 * @version April 24 , 2015
 * @author: Avijit Vishen avijit.vishen@asu.edu
 * Software Engineering, CIDSE, Arizona State University, Polytechnic Campus
 */
public class BudgetFragment extends Fragment {


    // newInstance constructor for creating fragment with arguments
    public static BudgetFragment newInstance() {
        BudgetFragment fragmentSecond = new BudgetFragment();

        return fragmentSecond;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.budget, container, false);
        final Spinner monthspinner = (Spinner) view.findViewById(R.id.monthspinner);

        DateFormatSymbols symbols = new DateFormatSymbols();
        String[] monthNames = symbols.getMonths();
        ArrayAdapter<String> monthsAdapter = new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_spinner_item,monthNames);
        monthsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthspinner.setAdapter(monthsAdapter);
        final Spinner catspinner = (Spinner) view.findViewById(R.id.catspinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catspinner.setAdapter(adapter);


        Button btn = (Button) view.findViewById(R.id.add_expense);
        btn.setOnClickListener(new View.OnClickListener(){
            EditText runningET;
            @Override
            public void onClick(View v) {
                try {
                    ExpenseDB db = new ExpenseDB(getActivity().getApplicationContext());

                    android.util.Log.d(this.getClass().getSimpleName(), "add button clicked");
                    ExpenseDB dbase = new ExpenseDB(getActivity().getApplicationContext());
                    final SQLiteDatabase crsDB = dbase.openDB();
                    String month = monthspinner.getSelectedItem().toString();
                    String cat = catspinner.getSelectedItem().toString();

                    runningET = (EditText) view.findViewById(R.id.budgetAmt);
                    String amt = runningET.getText().toString();

                    Cursor inner = crsDB.rawQuery("select monthid from month where name=?;", new String[]{month});
                    String monthid="",id="";
                    inner.moveToFirst();
                    monthid = String.valueOf(inner.getInt(0));
                    System.out.println("month is ***************************************"+monthid);

                    inner = crsDB.rawQuery("select catid from category where name=?;",new String[]{cat});
                    inner.moveToFirst();
                    id = String.valueOf(inner.getInt(0));
                    System.out.println("cat is is ***************************************"+id);


                    crsDB.execSQL("insert into budget values(?,?,?);", new String[]{amt, monthid.toString(), id.toString()});

                    runningET.setText("");
                    monthspinner.setSelection(0);
                    catspinner.setSelection(0);

                } catch (Exception ex) {
                    android.util.Log.w(this.getClass().getSimpleName(), "Exception inserting values: " +
                            ex.getMessage());
                }


            }});
        btn = (Button) view.findViewById(R.id.cancel);
        btn.setOnClickListener(new View.OnClickListener(){
            EditText runningET;
            @Override
            public void onClick(View v) {
                runningET = (EditText) view.findViewById(R.id.budgetAmt);
                runningET.setText("");

            }});
        return view;
    }
}
