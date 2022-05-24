package edu.zjut.androiddeveloper_ailaiziciqi.Calendar.CalendarImpl.search;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.zjut.androiddeveloper_ailaiziciqi.Calendar.CalendarImpl.add.SendSmsActivity;
import edu.zjut.androiddeveloper_ailaiziciqi.Calendar.CalendarImpl.smsdetail.SmsDetailsActivity;
import edu.zjut.androiddeveloper_ailaiziciqi.Calendar.R;
import edu.zjut.androiddeveloper_ailaiziciqi.Calendar.SmsReceiver;
import edu.zjut.androiddeveloper_ailaiziciqi.Calendar.model.Schedule;
import edu.zjut.androiddeveloper_ailaiziciqi.Calendar.model.ScheduleWithCheck;
import edu.zjut.androiddeveloper_ailaiziciqi.Calendar.model.SmsSearchInformation;

public class SmsSearchActivity extends AppCompatActivity {
    private ImageView back; //返回按钮

    private SmsSearchAdapter smsSearchAdapter;

    private ListView lv_show;

    private FloatingActionButton iv_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_search);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // 将自己绑定至SmsReceiver
        SmsReceiver.setSmsSearchActivity(this);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        iv_add = findViewById(R.id.iv_add);
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SmsSearchActivity.this, SendSmsActivity.class);
                startActivity(intent);
            }
        });

        lv_show = findViewById(R.id.lv_show);
        lv_show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // i 代表位置
                SmsSearchInformation s = (SmsSearchInformation)adapterView.getAdapter().getItem(i);

                if(s.getSmsScheduleList() == null || s.getSmsScheduleList().size() == 0) {
                    Toast.makeText(SmsSearchActivity.this, "未查询到日程", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.w("id", s.getId()+"");
                Intent intent = new Intent(SmsSearchActivity.this, SmsDetailsActivity.class);
                intent.putExtra("id", s.getId());
                startActivity(intent);
            }
        });
        updateListView();
    }

    public void updateListView() {
        List<SmsSearchInformation> list = new ArrayList<>();

        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        Schedule schedule1 = new Schedule(localDate, localTime, localTime, "0", "五月十二", "这是一些日程");
        Schedule schedule2 = new Schedule(localDate, localTime, localTime, "1", "五月十二", "这是一些日程");

        List<Schedule> listS = new ArrayList<>();
        listS.add(schedule1);
        listS.add(schedule2);
        SmsSearchInformation s = new SmsSearchInformation(1,"18811678671", localDate, listS);

        list.add(s);
        smsSearchAdapter = new SmsSearchAdapter(list, SmsSearchActivity.this);
        lv_show.setAdapter(smsSearchAdapter);
    }

    // 该函数为更新通知临时测试函数，连接数据库后应删除
    public void testUpdateListView() {
        List<SmsSearchInformation> list = new ArrayList<>();

        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        Schedule schedule1 = new Schedule(localDate, localTime, localTime, "0", "五月十二", "这是一些日程");
        Schedule schedule2 = new Schedule(localDate, localTime, localTime, "1", "五月十二", "这是一些日程");

        List<Schedule> listS = new ArrayList<>();
        listS.add(schedule1);
        listS.add(schedule2);
        SmsSearchInformation s = new SmsSearchInformation(1,"18811678671", localDate, listS);
        SmsSearchInformation s1 = new SmsSearchInformation(2,"4445", localDate, null);

        list.add(s);
        list.add(s1);
        smsSearchAdapter = new SmsSearchAdapter(list, SmsSearchActivity.this);
        lv_show.setAdapter(smsSearchAdapter);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 接触绑定
        SmsReceiver.setSmsSearchActivity(null);
    }
}