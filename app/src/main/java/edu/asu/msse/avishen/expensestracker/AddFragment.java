package edu.asu.msse.avishen.expensestracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2015 Avijit Singh Vishen.
 * <p/>
 * License to anyone who may want to rebuild and execute the application.
 * Right to Use given to TA and Instructor to build and evaluate the package
 * <p/>
 * Purpose:
 * This fragment is responsible for adding new expense to the database
 *
 * @version April 24 , 2015
 * @author: Avijit Vishen avijit.vishen@asu.edu
 * Software Engineering, CIDSE, Arizona State University, Polytechnic Campus
 */
public class AddFragment extends Fragment {


    // newInstance constructor for creating fragment with arguments
    public static AddFragment newInstance() {
        AddFragment fragmentSecond = new AddFragment();

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
        final View view = inflater.inflate(R.layout.add_expense, container, false);
        final Spinner monthSpinner = (Spinner) view.findViewById(R.id.monthspinner);

        DateFormatSymbols symbols = new DateFormatSymbols();
        String[] monthNames = symbols.getMonths();
        ArrayAdapter<String> monthsAdapter = new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_spinner_item, monthNames);
        monthsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthsAdapter);


        final Spinner catspinner = (Spinner) view.findViewById(R.id.categoryspinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catspinner.setAdapter(adapter);


        final Spinner cbSpinner = (Spinner) view.findViewById(R.id.cdspinner);
        adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.creditdebit, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cbSpinner.setAdapter(adapter);
        cbSpinner.setSelection(1);


        ExpenseDB db = new ExpenseDB(getActivity().getApplicationContext());
        List<String> acc = new ArrayList<>();
        try {
            final SQLiteDatabase crsDB = db.openDB();
            Cursor cur = crsDB.rawQuery("select name from account;", null);
            while (cur.moveToNext()) {
                acc.add(cur.getString(0));
                android.util.Log.w(this.getClass().getSimpleName(), "getting account " + cur.getString(0)
                );
            }

            final Spinner acSpinner = (Spinner) view.findViewById(R.id.acc_spinner);
            monthsAdapter = new ArrayAdapter<>(this.getActivity(),
                    android.R.layout.simple_spinner_item, acc);
            monthsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            acSpinner.setAdapter(monthsAdapter);
            crsDB.close();
            cur.close();
            db.close();
        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(), "Exception in database query " +
                    ex.getMessage());
        }

        Button btn = (Button) view.findViewById(R.id.add_expense);
        btn.setOnClickListener(new View.OnClickListener() {
            TextView runningTV;

            @Override
            public void onClick(View v) {
                try {
                    ExpenseDB db = new ExpenseDB(getActivity().getApplicationContext());

                    android.util.Log.d(this.getClass().getSimpleName(), "add button clicked");
                    ExpenseDB dbase = new ExpenseDB(getActivity().getApplicationContext());
                    final SQLiteDatabase crsDB = dbase.openDB();

                    runningTV = (TextView) view.findViewById(R.id.description);
                    String description = runningTV.getText().toString();
                    runningTV = (TextView) view.findViewById(R.id.amount);
                    Double amount = Double.parseDouble(runningTV.getText().toString());
                    String creditDebit = cbSpinner.getSelectedItem().toString().toLowerCase();
                    String category = catspinner.getSelectedItem().toString();
                    String month = monthSpinner.getSelectedItem().toString();
                    System.out.print("this is month" + month);
                    final Spinner aSpinner = (Spinner) view.findViewById(R.id.acc_spinner);
                    String acc = aSpinner.getSelectedItem().toString();
                    Cursor inner = crsDB.rawQuery("select monthid from month where name=?;", new String[]{month});
                    String monthid = "", id = "";
                    inner.moveToFirst();
                    monthid = String.valueOf(inner.getInt(0));

                    inner = crsDB.rawQuery("select catid from category where name=?;", new String[]{category});
                    inner.moveToFirst();
                    id = String.valueOf(inner.getInt(0));

                    crsDB.execSQL("insert into expenses values(?,?,?,?,?,null);", new String[]{description, amount.toString(), creditDebit, monthid, id});
                    inner = crsDB.rawQuery("select balance from account where name = ?;", new String[]{acc});
                    Double curBal = 0.0;
                    inner.moveToFirst();
                    curBal = inner.getDouble(0);


                    if (creditDebit.equals("credit")) {
                        curBal = curBal + amount;
                    } else {
                        curBal = curBal - amount;

                    }
                    crsDB.execSQL("update account set balance=? where name=?", new String[]{curBal.toString(), acc});
                } catch (Exception ex) {
                    android.util.Log.w(this.getClass().getSimpleName(), "Exception inserting values: " +
                            ex.getMessage());
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("New Expense Added!!")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                runningTV = (TextView) view.findViewById(R.id.description);
                                runningTV.setText("");
                                runningTV = (TextView) view.findViewById(R.id.amount);
                                runningTV.setText("");

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

                //crsDB.execSQL("insert into ");

            }
        });


//        spinner = (Spinner) view.findViewById(R.id.acc_spinner);
//        adapter = ArrayAdapter.createFromResource(this.getActivity(),
//                R.array.accounts, android.R.layout.simple_spinner_item);

//        spinner.setAdapter(adapter);


        return view;
    }
}
