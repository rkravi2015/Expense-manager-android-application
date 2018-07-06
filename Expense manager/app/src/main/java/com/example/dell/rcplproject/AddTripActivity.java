package com.example.dell.rcplproject;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddTripActivity extends AppCompatActivity {
    EditText e1,e2,e3,e4,e5;
    String startpoint,endpoint,str;
    double budget,left;
    int flag,count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        e1 = (EditText)findViewById(R.id.editText1);
        e2 = (EditText)findViewById(R.id.editText2);
        e3 = (EditText)findViewById(R.id.editText3);
        e4 = (EditText)findViewById(R.id.editText4);
        e5 = (EditText)findViewById(R.id.editText5);

        SQLiteDatabase db=openOrCreateDatabase("rcpl_db", Context.MODE_APPEND,null);//both read nd write with help of append
        db.execSQL("Create table if not exists trip(_id INTEGER PRIMARY KEY AUTOINCREMENT,destination varchar," +
                "source varchar,startdate varchar,enddate varchar,budgetapproved double,budgetleft double)");
        db.close();

    }
    public boolean onCreateOptionsMenu(Menu menu)//OptionMenuLayoutInflater
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.myoption, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.item1:
                int tripId=-1;
                Intent intent = new Intent(this,ViewTripListActivity.class);
                intent.putExtra("tripId",tripId);
                startActivity(intent);
                break;
            case R.id.item2:
                SharedPreferences preferences = getSharedPreferences("DemoFile", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                Intent i = new Intent(this,MainMenuActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }//OptionMenuLayout item click
    public void openCalender(View v)
    {
        MyDateChooser ref = new MyDateChooser();
        Date d = new Date();
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(d);
        int y = gc.get(Calendar.YEAR);
        int mt = gc.get(Calendar.MONTH);
        int dt = gc.get(Calendar.DATE);
        DatePickerDialog dialog = new DatePickerDialog(this,ref,y,mt,dt);
        dialog.show();
        switch(v.getId())//to check which date user is selecting (starting or ending)
        {
            case R.id.editText3:
                flag = 1;
                break;
            case R.id.editText4:
                flag = 0;
                break;
        }
    }
    class MyDateChooser implements DatePickerDialog.OnDateSetListener
    {

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int date) {
            str = year + "-" + (month +1)  + "-" + date;
            if (flag == 1)
                e3.setText(str);
            else
                e4.setText(str);
        }
    }
    public void add(View v)
    {
        count=0;
        startpoint= e1.getText().toString();
        endpoint = e2.getText().toString();
        try{budget = Double.parseDouble(e5.getText().toString());
        left=budget;}catch(Exception ae){}
        if(startpoint.equals(null)|| endpoint.equals(null) || budget==0.0 )
            Toast.makeText(this, "Please Enter All Data", Toast.LENGTH_SHORT).show();
        else {
            String s1=e3.getText().toString();
            String s2=e4.getText().toString();
            SQLiteDatabase db=openOrCreateDatabase("rcpl_db",Context.MODE_APPEND,null);
            String q="insert into trip(destination,source,startdate,enddate,budgetapproved,budgetleft) values("+"'"+endpoint+"'"+","+"'"+startpoint+"'"+","+"'"+s1+"'"+","+"'"+s2+"'"+","+"'"+budget+"'"+","+"'"+left+"'"+")";
            db.execSQL(q);
            Toast.makeText(this, "Record Inserted", Toast.LENGTH_SHORT).show();
            db.close();
            Intent i = new Intent(this, ProjectActivity.class);
            i.putExtra("Startingpoint", startpoint);
            i.putExtra("Startingdate", s1);
            i.putExtra("Count",count);
            startActivity(i);
        }
    }

    public void clear(View v)
    {
        e1.setText("");
        e2.setText("");
        e3.setText("");
        e4.setText("");
        e5.setText("");
    }

}
