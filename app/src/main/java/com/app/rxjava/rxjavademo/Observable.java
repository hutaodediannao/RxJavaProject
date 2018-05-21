//package com.app.rxjava.rxjavademo;
//
//import android.content.Context;
//import android.util.Log;
//
//import static android.content.ContentValues.TAG;
//
///**
// * 被观察者
// * Created by Administrator on 2018/5/20.
// */
//
//public class Observable {
//
//    private AnimObserver pig, tiger;
//
//    /**
//     * 构造方法中注册多个观察者
//     * @param context
//     */
//    public Observable(Context context) {
//        pig = new PigObserverImp(context);//订阅观察者
//        tiger = new TigerObserverImp(context);//订阅观察者
//    }
//
//    /**
//     * 被观察者发生改变，通知观察者回调
//     */
//    public void notifyAllData() {
//        Log.i(TAG, "notifyAllData: 我发生改变了，我需要通知观察者了");
//        pig.notifyDataSetChange("傻逼");//通知观察者pig
//        tiger.notifyDataSetChange("碉堡了");//通知观察者老虎
//    }
//
//}
