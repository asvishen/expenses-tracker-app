package edu.asu.msse.avishen.expensestracker;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Copyright 2015 Avijit Singh Vishen.
 * <p/>
 * License to anyone who may want to rebuild and execute the application.
 * Right to Use given to TA and Instructor to build and evaluate the package
 * <p/>
 * Purpose: This activity updates the balance of the selected account
 *
 * @version April 24 , 2015
 * @author: Avijit Vishen avijit.vishen@asu.edu
 * Software Engineering, CIDSE, Arizona State University, Polytechnic Campus
 */
public class BalanceUpdate extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_update);
        getSupportActionBar().hide();
        String acc = getIntent().getStringExtra("accName");
        android.util.Log.d("This is mad", "the out string got here is" + acc);
        TextView textView = (TextView) findViewById(R.id.AcctName);
        textView.setText(acc);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_balance_update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void onUpdate(View view){

        ExpenseDB db = new ExpenseDB(getApplicationContext());


        try {
            final SQLiteDatabase crsDB = db.openDB();
            TextView textView = (TextView) findViewById(R.id.AcctName);
            String acc = textView.getText().toString();
            EditText amt = (EditText) findViewById(R.id.newBal);

            crsDB.execSQL("update account set balance=? where name=?",new String[]{amt.getText().toString(),acc});

            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("pos","3");
            startActivity(intent);

        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(), "Exception getting waypoints info: " +
                    ex.getMessage());
        }




    }
    public void onCancelUpdate(View view){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("pos","3");
        startActivity(intent);
    }
}
