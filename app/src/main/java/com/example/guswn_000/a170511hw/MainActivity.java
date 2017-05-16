package com.example.guswn_000.a170511hw;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import static java.util.Calendar.YEAR;

public class MainActivity extends AppCompatActivity
{
    ListView listView;
    LinearLayout linear1, linear2;
    DatePicker datePicker;
    ArrayList<String> titlelist = new ArrayList<>();
    ArrayAdapter<String> adapter;
    Button btnsave;
    String date , selectedtitle;
    EditText editText;
    TextView tvcount;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("다이어리");
        init();
        //퍼미션 체크
        checkpermission();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //불러오기
                btnsave.setText("수정");
                linear1.setVisibility(View.GONE);
                linear2.setVisibility(View.VISIBLE);
                read(getExternalPath() + "diary/" + titlelist.get(position) + ".txt");
                selectedtitle = titlelist.get(position);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id)
            {
                //삭제
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("삭제 확인")
                        .setMessage("정말로 삭제하시겠습니까?")
                        .setNegativeButton("아니오",null)
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                remove(getExternalPath() + "diary/" + titlelist.get(position) + ".txt");
                                filelist();
                            }
                        })
                        .show();
                return true;
            }
        });

    }

    public void init()
    {
        listView = (ListView)findViewById(R.id.listview);
        linear1 = (LinearLayout)findViewById(R.id.linear1);
        linear2 = (LinearLayout)findViewById(R.id.linear2);
        datePicker = (DatePicker)findViewById(R.id.datepicker);
        btnsave = (Button)findViewById(R.id.btnsave);
        editText = (EditText)findViewById(R.id.et);
        tvcount = (TextView)findViewById(R.id.tvCount);

        //데이트픽커
//        date = datePicker.getYear() +"-"+(datePicker.getMonth()+1)+"-"+datePicker.getDayOfMonth()+".memo";
//        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
//            @Override
//            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                date = year+"-"+ (monthOfYear+1) +"-"+dayOfMonth+".memo";
//            }
//        });


        //어댑터 달기
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,titlelist);
        listView.setAdapter(adapter);

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

        filelist();


    }

    public void setfilename()
    {
        Date pickeddate = new Date(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
        date = dateFormat.format(pickeddate) + ".memo";
    }

    public void filelist()
    {
        File[] files = new File(getExternalPath() + "diary").listFiles();
        titlelist.clear();
        if(files != null)
        {
            for (File f : files)
            {
                titlelist.add(f.getName().substring(0,13));
            }
        }

        sort();
        tvcount.setText("등록된 메모 개수 : " + titlelist.size() );
        adapter.notifyDataSetChanged();
    }

    Comparator<String> comparator = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    };

    public void sort() {  Collections.sort(titlelist,comparator);  }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.makediary:
                linear1.setVisibility(View.GONE);
                linear2.setVisibility(View.VISIBLE);
                break;

            case R.id.btnsave:
                setfilename();
                if(btnsave.getText().toString().equals("수정")) //아이템클릭했을때,이름같은거 불러왔을때 수정으로 바꿔줘야함
                {
                    if(titlelist.contains(date))
                    {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                        dlg.setTitle("같은 날짜에 등록된 일기가 이미 존재합니다." +
                                "날짜를 수정하십시오")
                                .setPositiveButton("확인",null)
                                .show();
                        return;
                    }
                    remove(getExternalPath() + "diary/" + selectedtitle + ".txt"); //선택했던 이름의 파일 삭제
                    write(getExternalPath() + "diary/" + date + ".txt", editText.getText().toString());

                    btnsave.setText("저장");
                    linear2.setVisibility(View.GONE);
                    linear1.setVisibility(View.VISIBLE);
                }
                else //저장일때
                {
                    if (titlelist.contains(date))
                    {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                        dlg.setMessage("같은 날짜에 등록된 일기가 이미 존재합니다." +
                                "불러오시겠습니까?")
                                .setNegativeButton("아니오",null)
                                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        btnsave.setText("수정");
                                        read(getExternalPath() + "diary/" + date + ".txt");
                                        linear1.setVisibility(View.GONE);
                                        linear2.setVisibility(View.VISIBLE);
                                    }
                                })
                                .show();
                        selectedtitle = date;
                        return;

                    }

                    Diary diary = new Diary(date,editText.getText().toString());
                    titlelist.add(diary.getTitle());
                    adapter.notifyDataSetChanged();
                    write(getExternalPath() + "diary/" + diary.getTitle() + ".txt" , diary.getContent());
                    editText.setText(null);
                    linear2.setVisibility(View.GONE);
                    linear1.setVisibility(View.VISIBLE);

                }
                resetdatepicker();
                filelist();

                break;
            case R.id.btncancel:
                resetdatepicker();
                editText.setText(null);
                linear2.setVisibility(View.GONE);
                linear1.setVisibility(View.VISIBLE);
                btnsave.setText("저장");
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

    public void remove(String path)
    {
        File file = new File(path);
        file.delete();
    }

    public void write(String path, String content)
    {
        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path,true));
            bw.write(content);
            bw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Toast.makeText(this,e.getMessage() + "쓰기실패욧~"+ getFilesDir(),Toast.LENGTH_SHORT).show();
        }
    }

    public void read(String path)
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String readStr = "";
            String str = null;
            while ((str = br.readLine()) != null)
            {
                readStr += str + "\n";
            }
            br.close();
            editText.setText(readStr);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "File not found" , Toast.LENGTH_SHORT).show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
