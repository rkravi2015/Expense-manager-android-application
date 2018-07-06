package com.example.dell.rcplproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainMenuActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        SharedPreferences sp1 = getSharedPreferences("DemoFile",0);
        String s = sp1.getString("Status1","Create new trip");
        if(s.equals("Aut"))
        {
            Intent i = new Intent(this,ProjectActivity.class);
            startActivity(i);
        }
    }
    public void newtrip(View v)
    {

        SharedPreferences sp = getSharedPreferences("DemoFile",0);
        SharedPreferences.Editor editor1 = sp.edit();
        editor1.putString("Status1","Aut");
        editor1.commit();
        Intent i = new Intent(this,AddTripActivity.class);
        startActivity(i);
    }
    public void view(View v)
    {
        Intent i = new Intent(this,ViewTripActivity.class);
        startActivity(i);
    }
}


