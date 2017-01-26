package edu.asu.msse.avishen.expensestracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;

/**
 * Copyright 2015 Avijit Singh Vishen.
 * <p/>
 * License to anyone who may want to rebuild and execute the application.
 * Right to Use given to TA and Instructor to build and evaluate the package
 * <p/>
 * Purpose: This acitivty sets up the tabs and the fragments
 * @version April 24 , 2015
 * @author: Avijit Vishen avijit.vishen@asu.edu
 * Software Engineering, CIDSE, Arizona State University, Polytechnic Campus
 */

public class MainActivity extends FragmentActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        String pos = getIntent().getStringExtra("pos");

        viewPager.setAdapter(new TabsFragmentPagerAdapter(getSupportFragmentManager(),
                getApplicationContext()));
        if(pos!=null){
            viewPager.setCurrentItem(Integer.parseInt(pos),true);
        }else {
            viewPager.setCurrentItem(0,true);
        }
        ExpenseDB db = new ExpenseDB(this);
        try {
            File ext = Environment.getExternalStorageDirectory();
            android.util.Log.w("MainActivity onCreate", "external storage directory is: " +
                    ext.getAbsolutePath());
            File extPub = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            android.util.Log.w("MainActivity onCreate", "pubic download directory is: " +
                    extPub.getAbsolutePath());
            File dataDir = Environment.getDataDirectory();
            android.util.Log.w("MainActivity onCreate", "my data directory is: " +
                    dataDir.getAbsolutePath());
            android.util.Log.w("MainActivity onCreate", "Context data dir is: " + this.getApplicationInfo().dataDir);
            android.util.Log.w("MainActivity onCreate", "Context files dir is: " + this.getFilesDir());
            db.copyDB();
            SQLiteDatabase crsDB = db.openDB();
            Cursor c = crsDB.rawQuery("SELECT * FROM category", null);
            String temp = "";
            while (c.moveToNext()) {
                String catName = c.getString(0);
                int catId = c.getInt(1);
                temp = temp + "\n Category:" + catName + "\tID:" + catId;
            }
            android.util.Log.w("onCreate read db", "found categories:" + temp);



            c.close();
            crsDB.close();
            db.close();

        } catch (java.sql.SQLException sqle) {
            android.util.Log.w("Caught SQLException:", sqle.getMessage());
        } catch (Exception ex) {
            android.util.Log.w("Caught Exception:", ex.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_summary, menu);
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


}
