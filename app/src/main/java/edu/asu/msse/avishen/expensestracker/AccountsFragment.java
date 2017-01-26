package edu.asu.msse.avishen.expensestracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2015 Avijit Singh Vishen.
 * <p/>
 * License to anyone who may want to rebuild and execute the application.
 * Right to Use given to TA and Instructor to build and evaluate the package
 * <p/>
 * Purpose: This fragment sets up the account listview by querying the database
 *
 * @version April 24 , 2015
 * @author: Avijit Vishen avijit.vishen@asu.edu
 * Software Engineering, CIDSE, Arizona State University, Polytechnic Campus
 */
public class AccountsFragment extends Fragment {

    List<String> acc;
    List<Double> bal;

    // newInstance constructor for creating fragment with arguments
    public static AccountsFragment newInstance() {
        AccountsFragment fragmentSecond = new AccountsFragment();

        return fragmentSecond;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.accounts, container, false);
        final ListView listView = (ListView) view.findViewById(R.id.listView);


        ExpenseDB db = new ExpenseDB(getActivity().getApplicationContext());
        acc =  new ArrayList<>();
        bal = new ArrayList<>();


        try {
            final SQLiteDatabase crsDB = db.openDB();
            Cursor cur = crsDB.rawQuery("select name,balance from account;", null);

            while (cur.moveToNext()) {
                acc.add(cur.getString(0));
                android.util.Log.w(this.getClass().getSimpleName(), "getting account " + cur.getString(0)
                );
                bal.add(cur.getDouble(1));
            }






        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(), "Exception getting waypoints info: " +
                    ex.getMessage());
        }



        class IconicAdapter extends ArrayAdapter<String> {
            IconicAdapter() {
                super(getActivity(), R.layout.row, R.id.label,acc);
            }
            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View row=super.getView(position, convertView, parent);

                TextView size=(TextView)row.findViewById(R.id.balanceAmount);
                size.setText(bal.get(position).toString());
                return(row);
            }
        }

        // Assign adapter to ListView
        listView.setAdapter(new IconicAdapter());
        //final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(),BalanceUpdate.class);
                intent.putExtra("accName",itemValue);
                startActivity(intent);
                //setContentView(R.layout.activity_update_balance);


                // Show Alert


            }

        });
        ImageButton btn = (ImageButton) view.findViewById(R.id.add_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AddAccount.class);
                startActivity(intent);
            }
        });



//        TextView tvLabel = (TextView) view.findViewById(R.id.tv);
//        tvLabel.setText(page + " -- " + title);

        return view;
    }

}

