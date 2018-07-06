package com.example.dell.rcplproject;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LowBudgetActivity extends AppCompatActivity {
    EditText e1;
    int tripId;
    double budgetApproved=0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_low_budget);
        e1 = (EditText)findViewById(R.id.editText1);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        tripId = b.getInt("Title");
    }
    public void clicking(View v)
    {

        switch(v.getId())
        {
            case R.id.button1:
                budgetApproved = Integer.parseInt(e1.getText().toString());
                SQLiteDatabase db=openOrCreateDatabase("rcpl_db", Context.MODE_APPEND,null);
                String query= " update trip set budgetapproved =" + budgetApproved  + " where _id =" + tripId;
                db.execSQL(query);
                break;
            case  R.id.button2:
                break;
        }
        Intent i = new Intent(this,ProjectActivity.class);
        i.putExtra("NewBudget",budgetApproved);
        startActivity(i);
    }
}
