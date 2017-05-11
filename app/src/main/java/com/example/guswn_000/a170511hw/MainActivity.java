package com.example.guswn_000.a170511hw;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    ListView listView;
    LinearLayout linear1, linear2;
    DatePicker datePicker;
    ArrayList<String> diarylist = new ArrayList<>();
    ArrayAdapter<String> adapter;
    Button btnsave;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.listview);
        linear1 = (LinearLayout)findViewById(R.id.linear1);
        linear2 = (LinearLayout)findViewById(R.id.linear2);
        datePicker = (DatePicker)findViewById(R.id.datepicker);
        btnsave = (Button)findViewById(R.id.btnsave);

        //어댑터 달기
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,diarylist);
        listView.setAdapter(adapter);
    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.makediary:
                linear1.setVisibility(View.INVISIBLE);
                linear2.setVisibility(View.VISIBLE);
                break;

            case R.id.btnsave:
                if(btnsave.getText().toString().equals("수정"))
                {

                }
                else
                {

                }
                break;
            case R.id.btncancel:

                break;
        }




    }
}
