package com.example.dell.rcplproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewTripListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{
    ListView lv;
    String selected,tid;
    int a,flag=-1;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Cursor cursor;

        setContentView(R.layout.activity_view_trip_list);
        lv=(ListView)findViewById(R.id.listview1);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        int tripId = b.getInt("tripId");
        SQLiteDatabase db=openOrCreateDatabase("rcpl_db", Context.MODE_APPEND,null);
        if(tripId==-1){ //clicked to view all list
            flag = 0;
        }
        else //clicked on manual search
        {
            flag=1;
        }
        cursor=db.rawQuery("select * from trip",null);
        CursorAdapter ca=new MyCursorAdapter(this,cursor,0);
        lv.setAdapter(ca);
        lv.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        a=i+1;
        Intent i1=new Intent(ViewTripListActivity.this,ExpenseActivity.class);
        i1.putExtra("tripId",(a));
        startActivity(i1);
    }

    class MyCursorAdapter extends CursorAdapter {

        public MyCursorAdapter(Context context, Cursor c, int flags)
        {
            super(context, c, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup)
        {
            LayoutInflater inflator=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v=inflator.inflate(R.layout.listtemplate,viewGroup,false);
            return v;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor)
        {
            TextView id=(TextView)view.findViewById(R.id.textView1);
            TextView spoint=(TextView)view.findViewById(R.id.textView0);
            TextView epoint=(TextView)view.findViewById(R.id.textView3);
            TextView sdate=(TextView)view.findViewById(R.id.textView4);
            TextView abudget=(TextView)view.findViewById(R.id.textView8);
            TextView bbudget = (TextView)view.findViewById(R.id.textView5);

            tid=cursor.getString(cursor.getColumnIndex("_id"));
            String Startpoint=cursor.getString(cursor.getColumnIndex("source"));
            String endpoint=cursor.getString(cursor.getColumnIndex("destination"));
            String startdate=cursor.getString(cursor.getColumnIndex("startdate"));
            String appbudget=cursor.getString(cursor.getColumnIndex("budgetapproved"));
            String balbudget=cursor.getString(cursor.getColumnIndex("budgetleft"));



            id.setText("Trip Id: "+tid);
            spoint.setText("Start Point : "+Startpoint);
            epoint.setText("End Point : "+endpoint);
            sdate.setText("Start Date : "+startdate);
            abudget.setText("Approved Budget: "+appbudget);
            bbudget.setText("Balance Budget: "+balbudget);


        }



    }

}
