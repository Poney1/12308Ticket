package com.wxws.myticket.app;

import android.app.Activity;

import java.util.LinkedList;
import java.util.Stack;

/**
 * desc: activity 栈管理
 * Date: 2016/10/9 09:55
 *
 * @auther: lixiangxiang
 */
public class StackManager {

    private static final Stack<Activity> sActivityList = new Stack<>();

    /***
     * 在每个Activity的onCreate中调用，用来记录打开了的activity
     */
    public static void addActivity(Activity act) {
        sActivityList.add(act);
    }

    /***
     * 在每个Activity的onDestroy中调用
     */
    public static void removeActivity(Activity act) {
        sActivityList.remove(act);
        act.finish();
    }
    /***
     * 结束所有的activity，并关闭程序的进程
     */
    public static void exit() {
        finishAll();
        System.exit(0);
    }

    /***
     * 结束所有的activity，但不会关闭程序的进程
     */
    public static void finishAll() {
        for (Activity act : sActivityList) {
            if (act !=null)
            act.finish();
        }
        sActivityList.clear();
    }

    /**
     *关闭除首页外的其他界面
     */
    public static void goMain(){
        LinkedList<Activity> linkedList = new LinkedList<>();
        for (Activity act : sActivityList) {
            if(null != act && !act.isFinishing() && !(act instanceof MainActivity)){
                linkedList.add(act);
                act.finish();
            }
        }
        try {
            sActivityList.remove(linkedList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 保持某个activity 不关闭
     * @param activity
     */
    public static void openActivity(Class<?> activity){
        LinkedList<Activity> linkedList = new LinkedList<>();
        for (Activity act : sActivityList) {
            if(null != act &&!(act.getClass().equals(activity))&& !act.isFinishing()){
                act.finish();
            }
            if(null != act && act.getClass().equals(activity)){
                linkedList.add(act);
            }
        }
        sActivityList.clear();
        sActivityList.addAll(linkedList);
    }

    /**
     * 关闭上一个打开的Activity
     */
    public static void closeTheLastActivity(){
        if(sActivityList.size()>1){
            sActivityList.get(sActivityList.size()-2).finish();
            sActivityList.remove(sActivityList.size()-2);
        }
    }
}
