package com.example.dell.rcplproject;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;

public class ExpenseActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{
    ListView lv1;
    String tid;
    int tripId,a,add=0,z=1;
    byte[] img,img1;
    Bitmap b1;
    View v;
    TextView id,amount,category,particular,date;
    ImageView iv;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        add=0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        lv1=(ListView)findViewById(R.id.listview1);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        tripId = b.getInt("tripId");
        db = openOrCreateDatabase("rcpl_db", Context.MODE_APPEND,null);
        Cursor cursor=db.rawQuery("select * from expense where _tripid = "+tripId,null);
        CursorAdapter ca=new MyCursorAdapter(this,cursor,0);
        lv1.setAdapter(ca);
        lv1.setOnItemClickListener(this);
        Cursor cu = db.rawQuery("SELECT * FROM counting",null);//To get Row No. of the item which is clicked
        while(z<tripId){
            cu.moveToNext();
            add = add + cu.getInt(1);
            z++;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        a=add + i+1;
        String query = "SELECT * FROM expense WHERE _id = '" + a + "'AND _tripid = "+tripId;
        Cursor cur = db.rawQuery(query,null);
        while(cur.moveToNext())
        {
            img1 = cur.getBlob(6);
            Intent intent = new Intent(ExpenseActivity.this, ViewReceiptImageActivity.class);
            intent.putExtra("img", img1);
            startActivity(intent);
        }

    }

    class MyCursorAdapter extends CursorAdapter {

        public MyCursorAdapter(Context context, Cursor c, int flags)
        {
            super(context, c, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup)
        {
                LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflator.inflate(R.layout.expensetemplate, viewGroup, false);
                return v;

        }

        @Override
        public void bindView(View view, final Context context, Cursor cursor) {

                id = (TextView) view.findViewById(R.id.textView);
                amount = (TextView) view.findViewById(R.id.textView2);
                category = (TextView) view.findViewById(R.id.textView3);
                particular = (TextView) view.findViewById(R.id.textView4);
                date = (TextView) view.findViewById(R.id.textView5);
                iv = (ImageView) view.findViewById(R.id.imageView1);
                iv.setImageResource(android.R.drawable.ic_menu_report_image);

                tid = cursor.getString(cursor.getColumnIndex("_tripid"));
                String amnt = cursor.getString(cursor.getColumnIndex("amount"));
                String categry = cursor.getString(cursor.getColumnIndex("category"));
                String p = cursor.getString(cursor.getColumnIndex("particular"));
                String d = cursor.getString(cursor.getColumnIndex("date"));

                id.append(tid);
                amount.append(amnt);
                category.append(categry);
                particular.append(p);
                date.append(d);
                iv.setImageBitmap(b1);

        }
    }

}
