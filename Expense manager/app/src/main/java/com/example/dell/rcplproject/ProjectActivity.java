package com.example.dell.rcplproject;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ProjectActivity extends AppCompatActivity
{
    private int PICK_IMAGE_REQUEST = 1;
    EditText et1,et2,et3;
    TextView tv1,tv2;
    String str1,str2,str3,str4,startdate="a",startpoint = "b",selected;
    int tripid,tcount=0,j=0;
    Spinner s1;
    Double i=0.00,a,budgetapproved,budgetleft,additionalBudget=0.0;
    String []expense={"Select","Travelling","Meal","Lodging","Misc."};
    byte[] galleryInByte;
    ImageView imageView;
    Bitmap bitmap,nullbitmap;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        SQLiteDatabase db=openOrCreateDatabase("rcpl_db", Context.MODE_APPEND,null);//both read nd write with help of append
        db.execSQL("Create table if not exists expense(_id INTEGER PRIMARY KEY AUTOINCREMENT,_tripid INTEGER,category varchar,particular varchar,amount double,date varchar,pic blob)");
        db.execSQL("Create table if not exists counting(_id INTEGER PRIMARY KEY AUTOINCREMENT,count INTEGER)");
        db.close();//left
        getid();
        et1=(EditText)findViewById(R.id.editText1);
        et2=(EditText)findViewById(R.id.editText2);
        et3=(EditText)findViewById(R.id.editText3);
        s1=(Spinner)findViewById(R.id.spinner1);
        tv1=(TextView)findViewById(R.id.textView1);
        tv2 = (TextView)findViewById(R.id.textView2);
        imageView =(ImageView)findViewById(R.id.imageView);
        imageView.setImageResource(android.R.drawable.ic_menu_report_image);

        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,expense);
        //how spinner viewed
        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        try{ startpoint=bundle.getString("Startingpoint");
            startdate=bundle.getString("Startingdate");
            tcount = bundle.getInt("Count");
            }catch (Exception e){}
        setbudget();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //how drop down  items viewed

        s1.setAdapter(adapter);

        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l)
            {
                str4=expense[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });//event handler of adpatter
    }
    public void setbudget(){
        SQLiteDatabase db=openOrCreateDatabase("rcpl_db",MODE_APPEND,null);
        try{Intent i = getIntent();
            additionalBudget = i.getDoubleExtra("NewBudget",0.0);}catch (Exception e){}
        Cursor cursor=db.rawQuery("select * from trip",null);//get budget
        while(cursor.moveToNext())
        {
            int s1=cursor.getInt(cursor.getColumnIndex("_id"));
            if(s1==tripid){
                String s2=cursor.getString(cursor.getColumnIndex("budgetapproved"));
                try{budgetapproved = Double.parseDouble(s2);}catch (Exception e){}
                budgetapproved = additionalBudget + budgetapproved;
                String s3=cursor.getString(cursor.getColumnIndex("budgetleft"));
                try{budgetleft = Double.parseDouble(s3);}catch (Exception ae){}
                budgetleft = budgetleft + additionalBudget;
                tv1.append(" " + budgetapproved);
                tv2.append(" " + budgetleft);

            }
        }
        String q= " update trip set budgetapproved ='" + budgetapproved +  "' where _id =" + tripid; //append additional budget
        db.execSQL(q);
        String qu= " update trip set budgetleft ='" + budgetleft +  "' where _id =" + tripid; //append additional budget
        db.execSQL(qu);
    }

    public boolean onCreateOptionsMenu(Menu menu)//Top Right Corner buttons Exit and old trip
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.myoption, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                int tripId=-1;
                Intent intent = new Intent(this,ViewTripListActivity.class);
                intent.putExtra("tripId",tripId);
                startActivity(intent);
                break;
            case R.id.item2:
                j++;
                SharedPreferences preferences = getSharedPreferences("DemoFile", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                SQLiteDatabase db=openOrCreateDatabase("rcpl_db", Context.MODE_APPEND,null);
                String q = "insert into counting(count)values("+tcount+")";
                db.execSQL(q);
                Intent i = new Intent(this, MainMenuActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void opencalender(View v) //how calender open
    {
        MyDateChooser ref=new MyDateChooser();
        Date d=new Date();
        GregorianCalendar gc=new GregorianCalendar();//helping class to get different part of calender
        gc.setTime(d);
        int y=gc.get(Calendar.YEAR);
        int m=gc.get(Calendar.MONTH);
        int dt=gc.get(Calendar.DATE);

        DatePickerDialog dialog=new DatePickerDialog(this, ref ,y, m , dt);//Calender view
        dialog.show();

    }
    class MyDateChooser implements DatePickerDialog.OnDateSetListener //what to do when calendar open
    {
        public void onDateSet(DatePicker datePicker, int year, int month, int date)
        {
            str2 =year+"-"+(month+1)+"-"+date;// the month start from 0 thats why +1
            et2.setText(str2);

        }
    }
    public void getid(){
        SQLiteDatabase db=openOrCreateDatabase("rcpl_db",MODE_APPEND,null);
        Cursor cursor=db.rawQuery("select * from trip",null);
        while(cursor.moveToNext())
        {
            tripid=cursor.getInt(cursor.getColumnIndex("_id"));
        }
        db.close();
    }
    public void add(View v)
    {
        str1=et1.getText().toString();
        str3=et3.getText().toString();
        try{i=Double.parseDouble(str1);}catch (Exception e){}
        str2=et2.getText().toString();
        String p=str4;
        if(str2.equals(null)|| str3.equals(null) || i==0.00 ||str4.equals("Select"))
            Toast.makeText(this, "Please Enter All Data", Toast.LENGTH_SHORT).show();
        else
        {

            budgetleft = budgetleft - i;
            if(budgetleft<=(0.25 * budgetapproved)){//notify when budget left is less than 75%
                int count =0;
                NotificationCompat.Builder nbuilder = new NotificationCompat.Builder(this);
                nbuilder.setSmallIcon(android.R.drawable.stat_notify_error);
                String str = "Less than 25% of the Budget is left";
                String str2 = "Click to add budget";
                nbuilder.setContentTitle(str);
                nbuilder.setContentText(str2);
                nbuilder.setDefaults(Notification.DEFAULT_ALL);
                nbuilder.setAutoCancel(true);
                TaskStackBuilder sb = TaskStackBuilder.create(this);
                sb.addParentStack(this);
                Intent ri = new Intent(this,LowBudgetActivity.class);
                ri.putExtra("Title ",tripid);
                sb.addNextIntent(ri);
                PendingIntent result = sb.getPendingIntent((int)System.currentTimeMillis(),PendingIntent.FLAG_UPDATE_CURRENT);
                nbuilder.setContentIntent(result);
                Notification n = nbuilder.build();
                NotificationManager nmanager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                nmanager.notify(++count,n);
            }
            SQLiteDatabase db=openOrCreateDatabase("rcpl_db",Context.MODE_APPEND,null);
            String query= " update trip set budgetleft =" + budgetleft  + " where _id =" + tripid; //append remaning budget
            db.execSQL(query);
            tv2.setText("Budget Left: "+String.valueOf(budgetleft));
            ContentValues cv = new ContentValues();//inseting values in database "Trip" table
            cv.put("category", p);
            cv.put("amount",  i);
            cv.put("particular",   str3);
            cv.put("date",   str2);
            cv.put("_tripid",   tripid);
            try{if(!galleryInByte.equals(null)){
            cv.put("pic",   galleryInByte);
            }
            else
            cv.put("pic",android.R.drawable.ic_menu_report_image);}catch (Exception ae){
            }
            db.insert("expense", null, cv );
            tcount++;
            Toast.makeText(this, "Data Inserted", Toast.LENGTH_SHORT).show();
            db.close();
            cleartext();//In order to clear all layouts once added to database
        }
    }

    public void camera(View v){
        final String []colors={"Gallery","Camera"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("select any one:");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selected=colors[which];
                switch(which){
                    case 0:
                        gallery();
                        break;
                    case 1:
                        openCamera();

                        break;
                }
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }
    public void gallery() {
        Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    } //get image from gallery

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        super.onActivityResult(requestCode, resultCode, data);
        if(selected.equals("Gallery")){
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

                Uri uri = data.getData();

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);


                    imageView = (ImageView) findViewById(R.id.imageView);
                    imageView.setImageBitmap(bitmap);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    nullbitmap = bitmap;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                    galleryInByte = stream.toByteArray();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else{

            try{Bundle bundle=data.getExtras();
            bitmap=(Bitmap)bundle.get("data");
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                nullbitmap = bitmap;
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            galleryInByte = stream.toByteArray();

        }catch(Exception e){
                Toast.makeText(this, "Please Click Image of Bill", Toast.LENGTH_SHORT).show();
            }}
    } //Result method of camera/gallery to return picture and set it
    public void openCamera()
    {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,0);
    } //to Open the camera
    public void cleartext()
    {
        et1.setText("");
        et3.setText("");
        et2.setText("");
        imageView.setImageResource(android.R.drawable.ic_menu_report_image);
        s1.setSelection(0);
        galleryInByte = null;


    }//to clear texts after adding data
    public void clear(View v)
    {
        et1.setText("");
        et3.setText("");
        et2.setText("");
        imageView.setImageResource(android.R.drawable.ic_menu_report_image);
        s1.setSelection(0);


    }//Button clear



    public void view(View v) {
        Intent i1 = new Intent(ProjectActivity.this, ExpenseActivity.class);
        i1.putExtra("tripId",tripid);
        startActivity(i1);
    } //Button to view expenses of this trip
}



