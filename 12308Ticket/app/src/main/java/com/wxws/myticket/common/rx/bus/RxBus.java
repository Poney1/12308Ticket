package com.wxws.myticket.common.rx.bus;


import android.support.annotation.NonNull;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;
import rx.subscriptions.CompositeSubscription;


public class RxBus {

    private static RxBus instance;
    //发布者
    private final Subject bus;
    //存放订阅者信息
    private Map<Object, CompositeSubscription> subscriptions = new HashMap<>();

    public static RxBus getInstance() {
        if (instance == null) {
            synchronized (RxBus.class) {
                if (instance == null) {
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }

    /**
     * PublishSubject 创建一个可以在订阅之后把数据传输给订阅者Subject
     * SerializedSubject 序列化Subject为线程安全的Subject
     */
    private RxBus() {
        bus = new SerializedSubject<>(PublishSubject.create());
    }

    /**
     * 发布事件
     */
    public void post(@NonNull Object obj) {
        bus.onNext(obj);
    }

    /**
     * 订阅事件
     */
    public <T> Observable tObservable(final Class<T> eventType) {
        return bus.ofType(eventType);// ofType = filter + cast
    }

    /**
     * 订阅者注册
     *
     * @param subscriber 订阅消息的对象
     */
    public void register(@NonNull final Object subscriber) {
        Observable.just(subscriber)
                .filter(new Func1<Object, Boolean>() {//判断订阅者不为空,并且订阅者没有在序列中
                    @Override
                    public Boolean call(Object s) {
                        return s != null && subscriptions.get(s) == null;
                    }
                })
                .map(new Func1<Object, Class>() {//拿到Class
                    @Override
                    public Class call(Object o) {
                        return o.getClass();
                    }
                })
                .flatMap(new Func1<Class, Observable<Method>>() {//获取订阅者方法并且用Observable装载
                    @Override
                    public Observable<Method> call(Class aClass) {
                        return Observable.from(aClass.getDeclaredMethods());
                    }
                })
                .map(new Func1<Method, Method>() {//使非public方法可以被invoke,并且关闭安全检查提升反射效率
                    @Override
                    public Method call(Method m) {
                        m.setAccessible(true);
                        return m;
                    }
                })
                .filter(new Func1<Method, Boolean>() {//方法必须被Subscribe注解
                    @Override
                    public Boolean call(Method m) {
                        return m.isAnnotationPresent(Subscribe.class);
                    }
                })
                .subscribe(new Action1<Method>() {
                    @Override
                    public void call(Method m) {
                        addSubscription(m, subscriber);// 将找到的方法和订阅者一起添加订阅
                    }
                });
    }

    /**
     * 添加订阅
     *
     * @param m          方法
     * @param subscriber 订阅者
     */
    private void addSubscription(final Method m, final Object subscriber) {
        //获取方法内参数
        Class[] parameterType = m.getParameterTypes();
        //只获取第一个方法参数，否则默认为Object
        Class cla = Object.class;
        if (parameterType.length > 1) {
            cla = parameterType[0];
        }
        //获取注解
        Subscribe sub = m.getAnnotation(Subscribe.class);

        //订阅事件
        Subscription subscription = tObservable(cla)
                .observeOn(sub.thread().getScheduler())
                .subscribe(new Action1() {
                               @Override
                               public void call(Object o) {
                                   try {
                                       m.invoke(subscriber, o);
                                   } catch (Exception e) {
                                       e.printStackTrace();
                                   }
                               }
                           }
                );
        putSubscriptionsData(subscriber, subscription);
    }

    /**
     * 添加订阅者到map空间来unRegister
     *
     * @param subscriber   订阅者
     * @param subscription 订阅者 Subscription
     */
    private void putSubscriptionsData(Object subscriber, Subscription subscription) {
        CompositeSubscription subs = subscriptions.get(subscriber);
        if (subs == null) {
            subs = new CompositeSubscription();
        }
        subs.add(subscription);
        subscriptions.put(subscriber, subs);
    }

    /**
     * 解除订阅者
     *
     * @param subscriber 订阅者
     */
    public void unRegister(final Object subscriber) {
        Observable.just(subscriber)
                .filter(new Func1<Object, Boolean>() {
                    @Override
                    public Boolean call(Object o) {
                        return o != null;
                    }
                })
                .map(new Func1<Object, CompositeSubscription>() {
                    @Override
                    public CompositeSubscription call(Object o) {
                        return subscriptions.get(o);
                    }
                })
                .filter(new Func1<CompositeSubscription, Boolean>() {
                    @Override
                    public Boolean call(CompositeSubscription subs) {
                        return subs != null;
                    }
                })
                .subscribe(new Action1<CompositeSubscription>() {
                    @Override
                    public void call(CompositeSubscription subs) {
                        subs.unsubscribe();
                        subscriptions.remove(subscriber);
                    }
                });
    }

}
