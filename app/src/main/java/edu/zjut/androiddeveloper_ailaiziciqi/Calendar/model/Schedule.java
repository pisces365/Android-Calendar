package edu.zjut.androiddeveloper_ailaiziciqi.Calendar.model;

import android.util.Log;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * 日程类
 */
public class Schedule {
    private int id;

    private String schedule;    // 日程名称
    private LocalDate scheduleDate; // 日程开始日期
    private LocalDate scheduleEndDate; // 日程结束日期
    private LocalTime scheduleStartTime; // 日程开始时间
    private LocalTime scheduleEndTime;  // 日程结束时间
    private String week; // 日程所在星期
    private String lunar; // 日程农历日期

    public Schedule(Schedule s) {
        this.schedule = s.schedule;
        this.scheduleDate = s.scheduleDate;
        this.scheduleStartTime = s.scheduleStartTime;
        this.scheduleEndTime = s.scheduleEndTime;
        this.week = s.week;
        this.lunar = s.lunar;
    }

    public Schedule(LocalDate scheduleDate, LocalTime scheduleStartTime, LocalTime scheduleEndTime, String week, String lunar, String schedule) {
        this.scheduleDate = scheduleDate;
        this.scheduleStartTime = scheduleStartTime;
        this.scheduleEndTime = scheduleEndTime;
        this.week = week;
        this.lunar = lunar;
        this.schedule = schedule;
    }

    public Schedule(String schedule, String mainDate, String startTime, String endTime) {
        this.schedule = schedule;

        String year = mainDate.substring(0, 4);
        Log.w("year", year);
        String month = mainDate.substring(5, 7);
        Log.w("month_origin", month);
        if (month.substring(0, 1).equals("0")) {
            month = month.substring(1, 2);
            Log.w("month_", month);
        }

        String day = mainDate.substring(8, 10);
        Log.w("day_origin", day);
        if (day.substring(0, 1).equals("0")) {
            day = day.substring(1, 2);
        }
        Log.w("day_", day);
        this.scheduleDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));

        String week = mainDate.substring(13, 14);
        this.week = week;
        Log.w("week_", week);

        String lunar = mainDate.substring(17, 21);
        this.lunar = lunar;
        Log.w("lunar_", lunar);

        this.scheduleStartTime = LocalTime.parse(startTime + ":00",
                DateTimeFormatter.ISO_TIME);

        this.scheduleEndTime = LocalTime.parse(endTime + ":00",
                DateTimeFormatter.ISO_TIME);
    }

    public Schedule(LocalDate scheduleDate, LocalDate scheduleEndDate, LocalTime scheduleStartTime, LocalTime scheduleEndTime, String week, String lunar, String schedule) {
        this.scheduleDate = scheduleDate;
        this.scheduleEndDate = scheduleEndDate;
        this.scheduleStartTime = scheduleStartTime;
        this.scheduleEndTime = scheduleEndTime;
        this.week = week;
        this.lunar = lunar;
        this.schedule = schedule;
    }

    public Schedule(String name, LocalDate date, LocalTime time) {
        this.schedule = name;
        this.scheduleDate = date;
        this.scheduleEndDate = date;
        this.scheduleStartTime = time;
        scheduleEndTime = time.plusHours(1);
    }

    public Schedule(String name, LocalDate date, LocalTime time, LocalTime endTime) {
        this.schedule = name;
        this.scheduleDate = date;
        this.scheduleStartTime = time;
        this.scheduleEndTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(LocalDate scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public LocalDate getScheduleEndDate() {
        return scheduleEndDate;
    }

    public void setScheduleEndDate(LocalDate scheduleEndDate) {
        this.scheduleEndDate = scheduleEndDate;
    }

    public LocalTime getScheduleStartTime() {
        return scheduleStartTime;
    }

    public void setScheduleStartTime(LocalTime scheduleStartTime) {
        this.scheduleStartTime = scheduleStartTime;
    }

    public LocalTime getScheduleEndTime() {
        return scheduleEndTime;
    }

    public void setScheduleEndTime(LocalTime scheduleEndTime) {
        this.scheduleEndTime = scheduleEndTime;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getLunar() {
        return lunar;
    }

    public void setLunar(String lunar) {
        this.lunar = lunar;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    /**
     * 用来保存从DB读取的所有日程
     */
    public static ArrayList<Schedule> scheduleArrayList = new ArrayList<>();

    /**
     * 根据开始日期获取日程
     */
    public static ArrayList<Schedule> eventsForDate(LocalDate date) {
        ArrayList<Schedule> events = new ArrayList<>();
        for (Schedule event : scheduleArrayList) {
            if (event.getScheduleDate().equals(date)) {
                events.add(event);
            }
        }
        return events;
    }

    /**
     * 根据开始日期和开始时间获取日程
     */
    public static ArrayList<Schedule> eventsForDateAndTime(LocalDate date, LocalTime time) {
        ArrayList<Schedule> events = new ArrayList<>();
        for (Schedule event : scheduleArrayList) {
            int eventHour = event.scheduleStartTime.getHour();
            int cellHour = time.getHour();
            if (event.getScheduleDate().equals(date) && eventHour == cellHour) {
                events.add(event);
            }
        }
        return events;
    }

    /**
     * 根据名称模糊搜索日程(忽略大小写)
     */
    public static ArrayList<Schedule> schedulesForName(String input) {
        ArrayList<Schedule> schedules = new ArrayList<>();
        for (Schedule schedule : scheduleArrayList) {
            if (schedule.getSchedule().toLowerCase().contains(input.toLowerCase())) {
                schedules.add(schedule);
            }
        }
        return schedules;
    }

    @NonNull
    @Override
    public String toString() {
        String msg = "日程: ";
        msg += "名称 = " + schedule + " " +
                "日期 = " + scheduleDate + " " +
                "开始时间 = " + scheduleStartTime + " " +
                "结束时间 = " + scheduleEndTime;
        return msg;
    }
}