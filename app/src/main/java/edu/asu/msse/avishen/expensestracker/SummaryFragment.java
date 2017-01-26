package edu.asu.msse.avishen.expensestracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2015 Avijit Singh Vishen.
 * <p/>
 * License to anyone who may want to rebuild and execute the application.
 * Right to Use given to TA and Instructor to build and evaluate the package
 * <p/>
 * Purpose: This fragmant shows the summary page of the app
 *
 * @version April 24 , 2015
 * @author: Avijit Vishen avijit.vishen@asu.edu
 * Software Engineering, CIDSE, Arizona State University, Polytechnic Campus
 */
public class SummaryFragment extends Fragment {

    Spinner spinner;
    ExpenseDB db;
    String curMonth,curCat;
    SQLiteDatabase crsDB;

    // newInstance constructor for creating fragment with arguments
    public static SummaryFragment newInstance() {
        SummaryFragment fragmentFirst = new SummaryFragment();

        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        page=1;
//        title="page 1";
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.summary, container, false);
        curMonth="January";
        curCat="Other";
//        DateFormatSymbols symbols = new DateFormatSymbols();
//        String[] monthNames = symbols.getMonths();


        //ArrayAdapter<CharSequence> catAdapter = ArrayAdapter.createFromResource(this.getActivity(),
        //        R.array.categories, android.R.layout.simple_spinner_item);

        //spinner.setAdapter(catAdapter);
        db = new ExpenseDB(getActivity().getApplicationContext());
        List<String> categories = new ArrayList<>();
        List<String> months = new ArrayList<>();

        try {
            crsDB = db.openDB();
            Cursor cur = crsDB.rawQuery("select name from category;", null);
            while (cur.moveToNext()) {
                categories.add(cur.getString(0));
                android.util.Log.w(this.getClass().getSimpleName(), "getting category " + cur.getString(0)
                        );
            }
            crsDB.close();
            cur.close();
            db.close();
            spinner = (Spinner) view.findViewById(R.id.catspinner);
            ArrayAdapter<String> adapter  = new ArrayAdapter<>(this.getActivity(),
                    android.R.layout.simple_spinner_item, categories);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            //spinner.setSelection(0);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    //Toast.makeText(this, "derp", Toast.LENGTH_LONG).show();
                    curCat = parentView.getItemAtPosition(position).toString();

                    android.util.Log.d(this.getClass().getSimpleName(), "Selected category " + curCat);
//
//                    Spinner spinner = (Spinner) view.findViewById(R.id.monthspinner);
//                    TextView textView = (TextView)spinner.getSelectedView();
//                    //String month = textView.getText().toString();

                    try {
                        crsDB = db.openDB();
                        Cursor innerC =  crsDB.rawQuery("select amount from expenses,month,category where month.name =? and month.monthid=expenses.monthid and type=\'debit\' and category.name=? and expenses.catid=category.catid;",new String[]{curMonth,curCat});
                        Double total=0.0;
                        while (innerC.moveToNext()) {
                            total+=innerC.getDouble(0);
                            android.util.Log.d(this.getClass().getSimpleName(), "getting amount for category selected" + innerC.getDouble(0)
                            );
                        }
                        TextView reusableTV = (TextView) view.findViewById(R.id.totalexpAmount);
                        reusableTV.setText("$"+total.toString());

                        innerC = crsDB.rawQuery("select amount from budget,month,category where month.name =? and month.monthid=budget.monthid and category.name=? and category.catid=budget.catid",new String[]{curMonth,curCat});


                        reusableTV = (TextView) view.findViewById(R.id.budgetAmount);

                        Double budget=0.0;
                        while (innerC.moveToNext()) {
                            budget+=innerC.getDouble(0);
                            android.util.Log.d(this.getClass().getSimpleName(), "getting budget amount" + innerC.getDouble(0));
                        }

                        reusableTV.setText("$"+budget.toString());

                        Double left = budget-total;
                        TextView bdlbl = (TextView) view.findViewById(R.id.bugStatusLbl);
                        reusableTV = (TextView) view.findViewById(R.id.amountleft);
                        reusableTV.setText("$"+left.toString());

                        if(left<0){
                            left = left * -1;
                            bdlbl.setText("Over Budget");
                            reusableTV.setTextColor(Color.RED);
                            reusableTV.setText("-$" + left.toString());

                        }
                        else{
                            bdlbl.setText("Can Still Spend");
                            reusableTV.setTextColor(getResources().getColor(R.color.ForestGreen));
                        }

                        innerC.close();
                        crsDB.close();

                    }

                 catch (Exception ex) {
                    android.util.Log.w(this.getClass().getSimpleName(), "category on clicklistener " +
                            ex.getMessage());
                }






                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

            });

            spinner = (Spinner) view.findViewById(R.id.monthspinner);
            crsDB = db.openDB();
            cur = crsDB.rawQuery("select name from month;", null);
            while (cur.moveToNext()) {
                months.add(cur.getString(0));
                android.util.Log.w(this.getClass().getSimpleName(), "getting months " + cur.getString(0)
                );
            }

            Cursor current = crsDB.rawQuery("select amount from expenses,month where month.name =? and month.monthid=expenses.monthid and type=\'debit\';",new String[]{months.get(0)});
            Double total=0.0;
            while (current.moveToNext()) {
                total+=current.getDouble(0);
                android.util.Log.d(this.getClass().getSimpleName(), "getting expenses amount" + current.getDouble(0)
                );
            }


            TextView reusableTV = (TextView) view.findViewById(R.id.totalexpAmount);
            reusableTV.setText("$"+
                    total.toString());



            adapter = new ArrayAdapter<>(this.getActivity(),
                    android.R.layout.simple_spinner_item, months);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            //spinner.setSelection(0);


            current = crsDB.rawQuery("select amount from budget,month where month.monthid=1 and month.monthid=budget.monthid",null);

            reusableTV = (TextView) view.findViewById(R.id.budgetAmount);

            Double budget=0.0;
            while (current.moveToNext()) {
                budget+=current.getDouble(0);
                android.util.Log.d(this.getClass().getSimpleName(), "getting  budget amount" + current.getDouble(0));
            }

            reusableTV.setText("$"+budget.toString());

            Double left = budget-total;
            TextView bdlbl = (TextView) view.findViewById(R.id.bugStatusLbl);
            reusableTV = (TextView) view.findViewById(R.id.amountleft);

            reusableTV.setText(left.toString());

            if(left<0){
                left =left*-1;
                bdlbl.setText("Over Budget:");
                reusableTV.setTextColor(Color.RED);
                reusableTV.setText("-$"+left.toString());
            }
            else{
                reusableTV.setTextColor(getResources().getColor(R.color.ForestGreen));
                bdlbl.setText("Can Still Spend");
            }
            current = crsDB.rawQuery("select amount from expenses,month where month.name=? and month.monthid=expenses.monthid and type=\'credit\';",new String[]{months.get(0)});

            Double inflow =0.0;
            while (current.moveToNext()) {
                inflow+=current.getDouble(0);
                android.util.Log.d(this.getClass().getSimpleName(), "getting amount for inflow" + current.getDouble(0));
            }
            reusableTV =(TextView) view.findViewById(R.id.inflowAmount);
            reusableTV.setText("$"+inflow.toString());
            current = crsDB.rawQuery("select balance from account",null);
            inflow=0.0;
            while (current.moveToNext()) {
                inflow+=current.getDouble(0);
                android.util.Log.d(this.getClass().getSimpleName(), "getting amount for inflow" + current.getDouble(0));
            }
            reusableTV = (TextView) view.findViewById(R.id.accBalance);
            reusableTV.setText("$"+inflow.toString());



            cur.close();
            crsDB.close();
            db.close();

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    //Toast.makeText(this, "derp", Toast.LENGTH_LONG).show();

                    curMonth = parentView.getItemAtPosition(position).toString();
                    android.util.Log.d(this.getClass().getSimpleName(), "Selected Month:" + curMonth);


                    android.util.Log.d(this.getClass().getSimpleName(), "Selected category " + curCat);

                    try {
                        crsDB = db.openDB();
                        Cursor innerC =  crsDB.rawQuery("select amount from expenses,month,category where month.name =? and month.monthid=expenses.monthid and type=\'debit\' and category.name=? and expenses.catid=category.catid;",new String[]{curCat,curCat});
                        Double total=0.0;
                        while (innerC.moveToNext()) {
                            total+=innerC.getDouble(0);
                            android.util.Log.d(this.getClass().getSimpleName(), "getting amount for category selected" + innerC.getDouble(0)
                            );
                        }
                        TextView reusableTV = (TextView) view.findViewById(R.id.totalexpAmount);
                        reusableTV.setText("$"+total.toString());

                        innerC = crsDB.rawQuery("select amount from budget,month,category where month.name =? and month.monthid=budget.monthid and category.name=? and category.catid=budget.catid",new String[]{curMonth,curCat});


                        reusableTV = (TextView) view.findViewById(R.id.budgetAmount);

                        Double budget=0.0;
                        while (innerC.moveToNext()) {
                            budget+=innerC.getDouble(0);
                            android.util.Log.d(this.getClass().getSimpleName(), "getting budget amount" + innerC.getDouble(0));
                        }

                        reusableTV.setText("$"+budget.toString());

                        Double left = budget-total;
                        TextView bdlbl = (TextView) view.findViewById(R.id.bugStatusLbl);
                        reusableTV = (TextView) view.findViewById(R.id.amountleft);
                        reusableTV.setText("$"+left.toString());

                        if(left<0){
                            left = left*-1;
                            bdlbl.setText("Over Budget");
                            reusableTV.setTextColor(Color.RED);
                            reusableTV.setText("-$"+left.toString());

                        }
                        else{
                            bdlbl.setText("Can Still Spend");

                            reusableTV.setTextColor(getResources().getColor(R.color.ForestGreen));
                        }

                        innerC.close();
                        crsDB.close();
                    } catch (Exception ex) {
                        android.util.Log.w(this.getClass().getSimpleName(), "Exception getting db in monthonclicklistener " +
                                ex.getMessage());
                    }









                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

            });
            } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(), "Exception getting waypoints info: " +
                    ex.getMessage());
        }


        return view;
    }
    @Override
    public void onResume(){
        super.onResume();

        android.util.Log.d(this.getClass().getSimpleName(),"Resuming Summary Fragment");
    }


}
