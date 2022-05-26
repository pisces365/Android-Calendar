package edu.zjut.androiddeveloper_ailaiziciqi.Calendar.DesktopWidget;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.Timer;
import java.util.TimerTask;

import edu.zjut.androiddeveloper_ailaiziciqi.Calendar.CalendarImpl.add.AddScheduleActivity;
import edu.zjut.androiddeveloper_ailaiziciqi.Calendar.CalendarImpl.mix.MixActivity;
import edu.zjut.androiddeveloper_ailaiziciqi.Calendar.R;

/**
 * Implementation of App Widget functionality.
 */
public class DayCalenderWidget extends AppWidgetProvider {
    private static final String CLICK_ACTION = "edu.zjut.androiddeveloper_ailaiziciqi.CLICK";
    private static final String COLLECTION_VIEW_ACTION = "edu.zjut.androiddeveloper_ailaiziciqi.COLLECTION_VIEW_ACTION";
    private static final String CLICK_ACTION_DATE = "edu.zjut.androiddeveloper_ailaiziciqi.CLICKDATE";




    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

//        CharSequence widgetText = context.getString(R.string.appwidget_text);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.day_calender_widget);


        // Construct the RemoteViews object
        CharSequence widgetText = LocalDate.now().toString();
        views.setTextViewText(R.id.tv_month_day, widgetText);

        Intent intentClick = new Intent();
        //这个必须要设置，不然点击效果会无效
        intentClick.setClass(context, DayCalenderWidget.class);
        intentClick.setAction(CLICK_ACTION);

        //PendingIntent表示的是一种即将发生的意图，区别于Intent它不是立即会发生的
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intentClick,PendingIntent.FLAG_UPDATE_CURRENT);
        //为布局文件中的按钮设置点击监听
        views.setOnClickPendingIntent(R.id.iv_add_widget, pendingIntent);




        Intent intentClick_date = new Intent();
        //这个必须要设置，不然点击效果会无效
        intentClick_date.setClass(context, DayCalenderWidget.class);
        intentClick_date.setAction(CLICK_ACTION_DATE);

        //PendingIntent表示的是一种即将发生的意图，区别于Intent它不是立即会发生的
        PendingIntent pendingIntent_date = PendingIntent.getBroadcast(context,0,intentClick_date,PendingIntent.FLAG_UPDATE_CURRENT);
        //为布局文件中的按钮设置点击监听
        views.setOnClickPendingIntent(R.id.tv_month_day, pendingIntent_date);



        // 设置 “ListView” 的adapter。
        // (01) intent: 对应启动 ListWidgetService(RemoteViewsService) 的intent
        // (02) setRemoteAdapter: 设置 gridview的适配器
        //    通过setRemoteAdapter将ListView和ListWidgetService关联起来，
        //    以达到通过 ListWidgetService 更新 ListView的目的
        Intent serviceIntent = new Intent(context, ListWidgetService.class);

        views.setRemoteAdapter(R.id.lv_show_widget, serviceIntent);


        // 设置响应 “ListView” 的intent模板
        // 说明：“集合控件(如GridView、ListView、StackView等)”中包含很多子元素，如GridView包含很多格子。
        //     它们不能像普通的按钮一样通过 setOnClickPendingIntent 设置点击事件，必须先通过两步。
        //        (01) 通过 setPendingIntentTemplate 设置 “intent模板”，这是比不可少的！
        //        (02) 然后在处理该“集合控件”的RemoteViewsFactory类的getViewAt()接口中 通过 setOnClickFillInIntent 设置“集合控件的某一项的数据”
        Intent gridIntent = new Intent();

        gridIntent.setAction(COLLECTION_VIEW_ACTION);
        gridIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntentListView = PendingIntent.getBroadcast(context, 0, gridIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // 设置intent模板
        views.setPendingIntentTemplate(R.id.lv_show_widget, pendingIntentListView);


        Toast.makeText(context,"run", Toast.LENGTH_SHORT).show();

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Log.w("","runrun");
                AppWidgetManager manager = AppWidgetManager.getInstance(context.getApplicationContext());//获得appwidget管理实例，用于管理appwidget以便进行更新操作
                ComponentName componentName = new ComponentName(context.getApplicationContext(), DayCalenderWidget.class);//获得所有本程序创建的appwidget
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.day_calender_widget);//获取远程视图
                manager.updateAppWidget(componentName,remoteViews);
            }
        };
        timer.schedule(task, 0, 3000);

        Intent serviceIntent = new Intent(context, ListWidgetService.class);
        context.startService(serviceIntent);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        super.onDisabled(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        //context.stopService(new Intent(context, WidgetService.class));
        Log.i("AppWidget", "删除成功！");
    }

    //onReceive不存在widget生命周期中，它是用来接收广播，通知全局的
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        //当我们点击桌面上的widget按钮（这个按钮我们在onUpdate中已经为它设置了监听），widget就会发送广播
        //这个广播我们也在onUpdate中为它设置好了意图，设置了action，在这里我们接收到对应的action并做相应处理
        if (intent.getAction().equals(CLICK_ACTION)) {
            Toast.makeText(context,"click", Toast.LENGTH_SHORT).show();
            //因为点击按钮后要对布局中的文本进行更新，所以需要创建一个远程view
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.day_calender_widget);

            Intent add_activity_intent = new Intent(context, AddScheduleActivity.class);
            add_activity_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(add_activity_intent);

            //更新widget
            appWidgetManager.updateAppWidget(new ComponentName(context,DayCalenderWidget.class),remoteViews);
        } else if (intent.getAction().equals(CLICK_ACTION_DATE)){
            Toast.makeText(context,"click", Toast.LENGTH_SHORT).show();
            //因为点击按钮后要对布局中的文本进行更新，所以需要创建一个远程view
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.day_calender_widget);

            Intent add_activity_intent = new Intent(context, MixActivity.class);
            add_activity_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(add_activity_intent);

            //更新widget
            appWidgetManager.updateAppWidget(new ComponentName(context,DayCalenderWidget.class),remoteViews);

        }
        else if (intent.getAction().equals(COLLECTION_VIEW_ACTION)) {
            // 接受“ListView”的点击事件的广播
            int type = intent.getIntExtra("Type", 0);
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

        }
    }

    //当widget被初次添加或大小被改变时回调
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        updateAppWidget(context, appWidgetManager, appWidgetId);
    }
}