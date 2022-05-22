package edu.zjut.androiddeveloper_ailaiziciqi.Calendar.CalendarImpl.mix;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.zjut.androiddeveloper_ailaiziciqi.Calendar.CalendarImpl.add.AddScheduleActivity;
import edu.zjut.androiddeveloper_ailaiziciqi.Calendar.CalendarImpl.search.SearchActivity;
import edu.zjut.androiddeveloper_ailaiziciqi.Calendar.DB.DbContact;
import edu.zjut.androiddeveloper_ailaiziciqi.Calendar.DailyCalendarActivity;
import edu.zjut.androiddeveloper_ailaiziciqi.Calendar.model.Schedule;
import edu.zjut.androiddeveloper_ailaiziciqi.Calendar.Event.ScheduleDetailsActivity;
import edu.zjut.androiddeveloper_ailaiziciqi.Calendar.OldmanActivity;
import edu.zjut.androiddeveloper_ailaiziciqi.calendarview.Calendar;
import edu.zjut.androiddeveloper_ailaiziciqi.calendarview.CalendarLayout;
import edu.zjut.androiddeveloper_ailaiziciqi.calendarview.CalendarView;
import edu.zjut.androiddeveloper_ailaiziciqi.Calendar.Event.ScheduleListAdapter;
import edu.zjut.androiddeveloper_ailaiziciqi.Calendar.R;
import edu.zjut.androiddeveloper_ailaiziciqi.Calendar.CalendarImpl.base.activity.BaseActivity;
import edu.zjut.androiddeveloper_ailaiziciqi.Calendar.CalendarImpl.group.GroupItemDecoration;
import edu.zjut.androiddeveloper_ailaiziciqi.Calendar.CalendarImpl.group.GroupRecyclerView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MixActivity extends BaseActivity implements
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener,
        DialogInterface.OnClickListener,
        View.OnClickListener {

    TextView mTextMonthDay;

    TextView mTextYear;

    TextView mTextLunar;

    TextView mTextCurrentDay;

    CalendarView mCalendarView;

    RelativeLayout mRelativeTool;
    private int mYear;
    CalendarLayout mCalendarLayout;
    GroupRecyclerView mRecyclerView;


    // 新增的属性
    private AlertDialog mMoreDialog;
    private AlertDialog mFuncDialog;
    private int dayClickCount;
    private static LocalDate dayClickRecord;
    private ScheduleListAdapter mScheduleListAdapter;
    private ImageView mOldManBtn;


    /*
      增加新日程按钮
     */
    private FloatingActionButton addButton;

    /*
      搜索按钮
     */
    private ImageView search;

    public static void show(Context context) {
        context.startActivity(new Intent(context, MixActivity.class));
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_mix;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        setStatusBarDarkMode();
        dayClickCount = 0;
        mTextMonthDay = findViewById(R.id.tv_month_day);
        mTextYear = findViewById(R.id.tv_year);
        mTextLunar = findViewById(R.id.tv_lunar);
        mRelativeTool = findViewById(R.id.rl_tool);
        mCalendarView = findViewById(R.id.calendarView);
        mTextCurrentDay = findViewById(R.id.tv_current_day);
        // TODO:测试完成后删去这个sector
        Schedule schedule1 = new Schedule("Play apex", LocalDate.now(), LocalTime.of(20, 0));
        Schedule schedule2 = new Schedule("Destroy Android studio", LocalDate.now(), LocalTime.of(21, 0));
        Schedule schedule3 = new Schedule("Tea with Jack Ma", LocalDate.now().plusDays(1), LocalTime.of(15, 0));
        Schedule schedule4 = new Schedule("Take a bath", LocalDate.now(), LocalTime.of(20, 0));
        Schedule schedule5 = new Schedule("Event no.1", LocalDate.now(), LocalTime.of(18, 0));
        Schedule schedule6 = new Schedule("Event no.2", LocalDate.now(), LocalTime.of(18, 0));
        Schedule schedule7 = new Schedule("Event no.3", LocalDate.now(), LocalTime.of(18, 0));
        Schedule schedule8 = new Schedule("Event no.4", LocalDate.now(), LocalTime.of(18, 0));
        Schedule schedule9 = new Schedule("Neutralize CB's Server", LocalDate.now().plusDays(2), LocalTime.of(4, 0));
        Schedule schedule10 = new Schedule("Neutralize CB's Website", LocalDate.now().plusDays(2), LocalTime.of(6, 0));
        Schedule.scheduleArrayList.add(schedule1);
        Schedule.scheduleArrayList.add(schedule2);
        Schedule.scheduleArrayList.add(schedule3);
        Schedule.scheduleArrayList.add(schedule4);
        Schedule.scheduleArrayList.add(schedule5);
        Schedule.scheduleArrayList.add(schedule6);
        Schedule.scheduleArrayList.add(schedule7);
        Schedule.scheduleArrayList.add(schedule8);
        Schedule.scheduleArrayList.add(schedule9);
        Schedule.scheduleArrayList.add(schedule10);
        // TODO:Sector ends here

        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCalendarLayout.isExpand()) {
                    mCalendarLayout.expand();
                    return;
                }
                mCalendarView.showYearSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));
            }
        });
        findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
            }
        });
        mCalendarLayout = findViewById(R.id.calendarLayout);
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mCalendarView.setOnClickCalendarPaddingListener(new CalendarView.OnClickCalendarPaddingListener() {
            @Override
            public void onClickCalendarPadding(float x, float y, boolean isMonthView,
                                               Calendar adjacentCalendar, Object obj) {
                Log.e("onClickCalendarPadding", "  --  " + x + "  " + y + "  " + obj + "  " + adjacentCalendar);
                Toast.makeText(MixActivity.this,
                        adjacentCalendar.getYear() + "年，第" + obj + "周",
                        Toast.LENGTH_SHORT).show();
            }
        });
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));

        // TODO: finish this button listener
        findViewById(R.id.iv_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMoreDialog == null) {
                    mMoreDialog = new AlertDialog.Builder(MixActivity.this)
                            .setTitle(R.string.list_dialog_title)
                            .setItems(R.array.list_dialog_items, MixActivity.this)
                            .create();
                }
                mMoreDialog.show();
            }
        });

        final DialogInterface.OnClickListener listener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                mCalendarLayout.expand();
                                break;
                            case 1:
                                boolean result = mCalendarLayout.shrink();
                                Log.e("shrink", " --  " + result);
                                break;
                            case 2:
                                mCalendarView.scrollToPre(false);
                                break;
                            case 3:
                                mCalendarView.scrollToNext(false);
                                break;
                            case 4:
                                mCalendarView.scrollToCurrent(true);
//                                mCalendarView.scrollToCalendar(2018, 12, 30);
                                break;
                            case 5:
                                mCalendarView.setRange(2018, 7, 1, 2019, 4, 28);
//                                mCalendarView.setRange(mCalendarView.getCurYear(), mCalendarView.getCurMonth(), 6,
//                                        mCalendarView.getCurYear(), mCalendarView.getCurMonth(), 23);
                                break;
                            case 6:
                                Log.e("scheme", "  " + mCalendarView.getSelectedCalendar().getScheme() + "  --  "
                                        + mCalendarView.getSelectedCalendar().isCurrentDay());
                                List<Calendar> weekCalendars = mCalendarView.getCurrentWeekCalendars();
                                for (Calendar calendar : weekCalendars) {
                                    Log.e("onWeekChange", calendar.toString() + "  --  " + calendar.getScheme());
                                }
                                new AlertDialog.Builder(MixActivity.this)
                                        .setMessage(String.format("Calendar Range: \n%s —— %s",
                                                mCalendarView.getMinRangeCalendar(),
                                                mCalendarView.getMaxRangeCalendar()))
                                        .show();
                                break;
                        }
                    }
                };


        findViewById(R.id.iv_func).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFuncDialog == null) {
                    mFuncDialog = new AlertDialog.Builder(MixActivity.this)
                            .setTitle(R.string.func_dialog_title)
                            .setItems(R.array.func_dialog_items, listener)
                            .create();
                }
                mFuncDialog.show();
            }
        });

        addButton = findViewById(R.id.iv_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(MixActivity.this, AddScheduleActivity.class);
                startActivity(addIntent);
            }
        });

        search = findViewById(R.id.iv_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(MixActivity.this, SearchActivity.class);
                startActivity(searchIntent);
            }
        });

        // 今日日程列表的监听
        mScheduleListAdapter = new ScheduleListAdapter(this, LocalDate.now());
        mScheduleListAdapter.setOnItemClickListener(new ScheduleListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Schedule schedule) {
                if(schedule.getScheduleDate() != null){
                    Log.i("Event List Click", "In Activity:" + position);
                    Log.i("Event List Click", "In Activity:" + schedule.toString());
                    Intent intent = new Intent(MixActivity.this, ScheduleDetailsActivity.class);
                    intent.putExtra("Name", schedule.getSchedule());
                    intent.putExtra("Date", schedule.getScheduleDate());
                    intent.putExtra("Time", String.valueOf(schedule.getScheduleStartTime()));
                    intent.putExtra("EndTime", String.valueOf(schedule.getScheduleEndTime()));
                    intent.putExtra("Weather", "晴 18 - 27");
                    intent.putExtra("WeatherDetails", "有微风");
                    intent.putExtra("Type", "我的日历");
                    startActivity(intent);
                }
                else{
                    Log.i("Event List Click", "In Activity:" + "No schedule today");
                    Intent intent = new Intent(MixActivity.this, AddScheduleActivity.class);
                    startActivity(intent);
                }
            }
        });

        // 暂时的老年版按钮的监听
        mOldManBtn = findViewById(R.id.iv_old_man_btn);
        mOldManBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MixActivity.this, OldmanActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();

        Map<String, Calendar> map = new HashMap<>();
        map.put(getSchemeCalendar(year, month, 3, 0xFF40db25, "假").toString(),
                getSchemeCalendar(year, month, 3, 0xFF40db25, "假"));
        map.put(getSchemeCalendar(year, month, 6, 0xFFe69138, "事").toString(),
                getSchemeCalendar(year, month, 6, 0xFFe69138, "事"));
        map.put(getSchemeCalendar(year, month, 9, 0xFFdf1356, "议").toString(),
                getSchemeCalendar(year, month, 9, 0xFFdf1356, "议"));
        map.put(getSchemeCalendar(year, month, 13, 0xFFedc56d, "记").toString(),
                getSchemeCalendar(year, month, 13, 0xFFedc56d, "记"));
        map.put(getSchemeCalendar(year, month, 14, 0xFFedc56d, "记").toString(),
                getSchemeCalendar(year, month, 14, 0xFFedc56d, "记"));
        map.put(getSchemeCalendar(year, month, 15, 0xFFaacc44, "假").toString(),
                getSchemeCalendar(year, month, 15, 0xFFaacc44, "假"));
        map.put(getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记").toString(),
                getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记"));
        map.put(getSchemeCalendar(year, month, 25, 0xFF13acf0, "假").toString(),
                getSchemeCalendar(year, month, 25, 0xFF13acf0, "假"));
        map.put(getSchemeCalendar(year, month, 27, 0xFF13acf0, "多").toString(),
                getSchemeCalendar(year, month, 27, 0xFF13acf0, "多"));
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        mCalendarView.setSchemeDate(map);


        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new GroupItemDecoration<String, Schedule>());
        mRecyclerView.setAdapter(mScheduleListAdapter);
        mRecyclerView.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        calendar.addScheme(new Calendar.Scheme());
        calendar.addScheme(0xFF008800, "假");
        calendar.addScheme(0xFF008800, "节");
        return calendar;
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    // 日期的点击事件:单击修改左上角的日期,双击打开日视图
    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        dayClickCount += 1;
        LocalDate clickedDay = LocalDate.of(calendar.getYear(), calendar.getMonth(), calendar.getDay());
        if (dayClickCount >= 2 && dayClickRecord.equals(clickedDay)) {
            dayClickCount = 0;
            startActivity(new Intent(MixActivity.this, DailyCalendarActivity.class));
        } else {
            dayClickRecord = LocalDate.of(calendar.getYear(), calendar.getMonth(), calendar.getDay());
        }
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
        mScheduleListAdapter = new ScheduleListAdapter(this, dayClickRecord);
        mScheduleListAdapter.setOnItemClickListener(new ScheduleListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Schedule schedule) {
                if(schedule.getScheduleDate() != null){
                    Log.i("Event List Click", "In Activity:" + position);
                    Log.i("Event List Click", "In Activity:" + schedule.toString());
                    Intent intent = new Intent(MixActivity.this, ScheduleDetailsActivity.class);
                    intent.putExtra("Name", schedule.getSchedule());
                    intent.putExtra("Date", schedule.getScheduleDate());
                    intent.putExtra("Time", String.valueOf(schedule.getScheduleStartTime()));
                    intent.putExtra("EndTime", String.valueOf(schedule.getScheduleEndTime()));
                    intent.putExtra("Weather", "晴 18 - 27");
                    intent.putExtra("WeatherDetails", "有微风");
                    intent.putExtra("Type", "我的日历");
                    startActivity(intent);
                }
                else{
                    Log.i("Event List Click", "In Activity:" + "No schedule today");
                    Intent intent = new Intent(MixActivity.this, AddScheduleActivity.class);
                    startActivity(intent);
                }
            }
        });
        mRecyclerView.setAdapter(mScheduleListAdapter);
        mRecyclerView.notifyDataSetChanged();

        Log.e("onDateSelected", "  -- " + calendar.getYear() +
                "  --  " + calendar.getMonth() +
                "  -- " + calendar.getDay() +
                "  --  " + isClick + "  --   " + calendar.getScheme());
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }


    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case 0:
                mCalendarView.setWeekStarWithSun();
                break;
            case 1:
                mCalendarView.setWeekStarWithMon();
                break;
            case 2:
                mCalendarView.setWeekStarWithSat();
                break;
            case 3:
                if (mCalendarView.isSingleSelectMode()) {
                    mCalendarView.setSelectDefaultMode();
                } else {
                    mCalendarView.setSelectSingleMode();
                }
                break;
            case 5:
                mCalendarView.setAllMode();
                break;
            case 6:
                mCalendarView.setOnlyCurrentMode();
                break;
            case 7:
                mCalendarView.setFixMode();
                break;
        }
    }
}
