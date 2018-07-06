package com.example.dell.rcplproject;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.rcplproject.R;


import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ViewTripActivity extends AppCompatActivity {
    EditText et1,et2;
    TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip);
        et2=(EditText)findViewById(R.id.editText2);
        et1=(EditText)findViewById(R.id.editText1);
        //t1=(TextView)findViewById(R.id.textView);
    }
    public void popup(View v){
        MyDateChooser ref=new MyDateChooser();
        Date d=new Date();
        GregorianCalendar gc=new GregorianCalendar();
        gc.setTime(d);
        int y=gc.get(Calendar.YEAR);
        int mt=gc.get(Calendar.MONTH);
        int dt=gc.get(Calendar.DATE);
        DatePickerDialog dialog=new DatePickerDialog(this,ref,y,mt,dt);
        dialog.show();



    }
    class MyDateChooser implements DatePickerDialog.OnDateSetListener
    {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String str=year+"-"+(month +1)+"-"+dayOfMonth;
            et2.setText(str);
        }
    }
    public void clear(View v){
        et2.setText(null);
        et1.setText(null);
    }
    public void viewlist(View v){
        int tripId=-1;
        Intent i = new Intent(this,ViewTripListActivity.class);
        i.putExtra("tripId",tripId);
        startActivity(i);

    }
    public void done(View v){
        String startpoint = et1.getText().toString();
        String date = et2.getText().toString();
        if(startpoint.equals("")||date.equals(""))
        {
            Toast.makeText(this, "Please Fill all data", Toast.LENGTH_SHORT).show();
        }
        else
        {
            int tripId=-1;
            SQLiteDatabase db=openOrCreateDatabase("rcpl_db", Context.MODE_APPEND,null);
            Cursor cursor=db.rawQuery("select * from trip where source = '" + startpoint +"' AND startdate = '" +date +"'",null);
            if(cursor.moveToNext()){
                tripId= cursor.getInt(cursor.getColumnIndex("_id"));
                Intent i = new Intent(this,ExpenseActivity.class);
                i.putExtra("tripId",tripId);
                startActivity(i);

            }
            else
            {
                Toast.makeText(this, "No Such Trip Exist. Please Enter StartPoint in exact case as entered while adding trip", Toast.LENGTH_LONG).show();
            }

        }

    }
}
