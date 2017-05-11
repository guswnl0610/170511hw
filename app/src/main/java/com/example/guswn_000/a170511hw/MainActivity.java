package com.example.guswn_000.a170511hw;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import static java.util.Calendar.YEAR;

public class MainActivity extends AppCompatActivity
{
    ListView listView;
    LinearLayout linear1, linear2;
    DatePicker datePicker;
    ArrayList<String> titlelist = new ArrayList<>();
    ArrayAdapter<String> adapter;
    Button btnsave;
    String date;
    EditText editText;


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
        editText = (EditText)findViewById(R.id.et);

        //퍼미션 체크
        checkpermission();

        //데이트픽커
        date = datePicker.getYear() +"-"+(datePicker.getMonth()+1)+"-"+datePicker.getDayOfMonth()+".memo";
        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date = year+"-"+ (monthOfYear+1) +"-"+dayOfMonth+".memo";
            }
        });


        //어댑터 달기
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,titlelist);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //불러오기
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                //삭제
                return true;
            }
        });

        //디렉토리 만들기
        String path = getExternalPath();
        File file = new File(path + "diary");
        file.mkdir();
        String mkdirerrormsg = "디렉토리 생성";
        if(file.isDirectory() == false)
        {
            mkdirerrormsg = "디렉토리 생성 오류";
        }
        Toast.makeText(this,mkdirerrormsg,Toast.LENGTH_SHORT).show();

    }


    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.makediary:
                linear1.setVisibility(View.GONE);
                linear2.setVisibility(View.VISIBLE);
                break;

            case R.id.btnsave:
                if(btnsave.getText().toString().equals("수정")) //아이템클릭했을때 수정으로 바꿔줘야함
                {

                    linear2.setVisibility(View.GONE);
                    linear1.setVisibility(View.VISIBLE);
                }
                else //저장일때
                {
                    Diary diary = new Diary(date,editText.getText().toString());
                    titlelist.add(diary.getTitle());
                    adapter.notifyDataSetChanged();

                    try
                    {
                        BufferedWriter bw = new BufferedWriter(new FileWriter(getExternalPath() + "diary/" + diary.getTitle()+".txt",true));
                        bw.write(diary.getContent());
                        bw.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                        Toast.makeText(this,e.getMessage() + ":"+ getFilesDir(),Toast.LENGTH_SHORT).show();
                    }
                    resetdatepicker();
                    editText.setText(null);
                    linear2.setVisibility(View.GONE);
                    linear1.setVisibility(View.VISIBLE);

                }
                break;
            case R.id.btncancel:
                linear2.setVisibility(View.GONE);
                linear1.setVisibility(View.VISIBLE);
                break;
        }
    }

    public String getExternalPath()
    {
        String sdPath = "";
        String ext = Environment.getExternalStorageState();
        if(ext.equals(Environment.MEDIA_MOUNTED))
        {
            sdPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
            //sdPath = "mnt/sdcard/";
        }
        else
        {
            sdPath = getFilesDir() + "";
        }
        Toast.makeText(getApplicationContext(),sdPath,Toast.LENGTH_SHORT).show();
        return sdPath;
    }



    public void checkpermission()
    {

        int permissioninfo = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissioninfo == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,
                    "SDCard 쓰기 권한 있음",Toast.LENGTH_SHORT).show();
        }
        else {

            //사실 이프문 안써도되는데
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                Toast.makeText(this,
                        "어플리케이션 설정에서 저장소 사용 권한을 허용해주세요",Toast.LENGTH_SHORT).show();

                //이밑에꺼 해야 권한허용 대화상자가 다시뜸
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        100);
            }
            else{
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        100);  // 이 100은 리퀘스트코드다
            }
        }
    }

    public void resetdatepicker()
    {
        Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        datePicker.updateDate(yy,mm,dd);
    }


}
