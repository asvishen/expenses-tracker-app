package edu.asu.msse.avishen.expensestracker;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

/**
 * Copyright 2015 Avijit Singh Vishen.
 * <p/>
 * License to anyone who may want to rebuild and execute the application.
 * Right to Use given to TA and Instructor to build and evaluate the package
 * <p/>
 * Purpose: This activity adds a new account to the existing accounts
 *
 * @version April 24 , 2015
 * @author: Avijit Vishen avijit.vishen@asu.edu
 * Software Engineering, CIDSE, Arizona State University, Polytechnic Campus
 */
public class AddAccount extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

          setContentView(R.layout.activity_add_account);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_account, menu);
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
    public void onAddAccount(View view){

        ExpenseDB db = new ExpenseDB(getApplicationContext());


        try {
            final SQLiteDatabase crsDB = db.openDB();
            EditText name = (EditText) findViewById(R.id.accname);
            String acc = name.getText().toString();
            EditText amt = (EditText) findViewById(R.id.balance);

            crsDB.execSQL("insert into account values(?,?,null) ", new String[]{acc, amt.getText().toString()});
            crsDB.close();
            db.close();
            android.util.Log.w("in add account","ADDED ACCOUNT *****************************************");

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("pos","3");
            startActivity(intent);

        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(), "Exception inserting account " +
                    ex.getMessage());
        }
    }
    public void onCancel(View view){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("pos","3");
        startActivity(intent);
    }
}
