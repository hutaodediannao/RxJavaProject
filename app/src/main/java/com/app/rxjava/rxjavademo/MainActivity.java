package com.app.rxjava.rxjavademo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * RxJava项目演示
 */
public class MainActivity extends BaseActivity {

    private List<Person> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list.addAll(C.PERSON_LIST);
    }

    //普通使用方法
    public void normal(View view) {
        //1.0 new创建一个被观察者对象
        Observable<Person> observable = new Observable<Person>() {
            @Override
            protected void subscribeActual(Observer<? super Person> observer) {//订阅方法
                observer.onNext(list.get(0));
            }
        };

        //1.1 create关键字创建
        Observable<Person> observable12 = Observable.create(new ObservableOnSubscribe<Person>() {
            @Override
            public void subscribe(ObservableEmitter<Person> emitter) throws Exception {
                emitter.onNext(list.get(0));
            }
        });

        //2.创建观察者对象
        Observer<Person> observer = new Observer<Person>() {
            @Override
            public void onSubscribe(Disposable d) {//观察者开始订阅的时候开始执行
                boolean isDisposable = d.isDisposed();
                showMsg(isDisposable + "");
            }

            @Override
            public void onNext(Person person) {//监控被观察者onNext行为
                showMsg(person.toString());
            }

            @Override
            public void onError(Throwable e) {//监控被观察者出错发生的行为，与onComplete互斥
                showMsg(e.getMessage());
            }

            @Override
            public void onComplete() {//监控被观察者的complete行为
                showMsg("complete");
            }
        };

        //3.被观察者开始订阅观察者监控事件行为
        observable12.subscribe(observer);
    }

    //map遍历方法
    public void map(View view) {
        Observable.create(new ObservableOnSubscribe<Person>() {
            @Override
            public void subscribe(ObservableEmitter<Person> emitter) throws Exception {
                for (Person p : list) {
                    emitter.onNext(p);
                }
                emitter.onComplete();
            }
        }).map(new Function<Person, Integer>() {
            @Override
            public Integer apply(Person person) throws Exception {
                //将年龄大于30的返回，否则返回null
                if (person.getAge() > 30) return person.getAge();
                return null;
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                showMsg(d.isDisposed());
            }

            @Override
            public void onNext(Integer integer) {
                showMsg(integer);
            }

            @Override
            public void onError(Throwable e) {
                showMsg(e.getMessage());
            }

            @Override
            public void onComplete() {
                showMsg("complte");
            }
        });
    }

    //flatMap自定义遍历方法
    public void flatMap(View view) {
        Observable
                .just(list)//直接生成iterator遍历
                .flatMap(new Function<List<Person>, ObservableSource<Person>>() {
                    @Override
                    public ObservableSource<Person> apply(final List<Person> people) throws Exception {
                        return new ObservableSource<Person>() {
                            @Override
                            public void subscribe(Observer<? super Person> observer) {
                                for (Person p : people) {
                                    p.setAge(20);//将年龄全部修改为20
                                    observer.onNext(p);//开始发射消息
                                }

                                //发射完成
                                observer.onComplete();
                            }
                        };
                    }
                })
                .subscribe(new Consumer<Person>() {
                    @Override
                    public void accept(Person person) throws Exception {
                        showMsg(person);
                    }
                });
    }

    //filter筛选数据源遍历方法
    public void filter(View view) {
        Observable
                .fromIterable(list)//表示需要遍历
                .filter(new Predicate<Person>() {
                    @Override
                    public boolean test(Person person) throws Exception {
                        if (person.getAge() > 30) return true;
                        return false;
                    }
                })
                .subscribe(new Consumer<Person>() {
                    @Override
                    public void accept(Person person) throws Exception {
                        showMsg(person.getAge());
                    }
                });
    }

    //take准许每次输出一个元素之前做一些额外的事情
    public void doOnNext(View view) {
        Observable
                .fromIterable(list)
                .doOnNext(new Consumer<Person>() {
                    @Override
                    public void accept(Person person) throws Exception {
                        person.setAge(person.getAge() + 1);
                        showMsg("开始筛选,将每个人的年龄加1");
                    }
                })
                .subscribe(new Consumer<Person>() {
                    @Override
                    public void accept(Person person) throws Exception {
                        showMsg(person);
                    }
                });
    }

    //线程调度的单个任务使用
    public void singleThreadTest(View view) {
    Observable
            .create(new ObservableOnSubscribe<Person>() {
                @Override
                public void subscribe(ObservableEmitter<Person> emitter) throws Exception {
                    Person p = new Person("hutao", "shenzhen", 28);
                    emitter.onNext(p);
                    emitter.onComplete();
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<Person>() {
                @Override
                public void onSubscribe(Disposable d) {
                    showMsg(d.isDisposed()+"");
                }

                @Override
                public void onNext(Person person) {
                    showMsg(person);
                }

                @Override
                public void onError(Throwable e) {
                    showMsg(e.getMessage());
                }

                @Override
                public void onComplete() {
                    showMsg("complete");
                }
            });
    }

    //线程调度的多个任务使用
    public void muiltThreadTest(View view) {
     Observable
             .fromIterable(list)
             .filter(new Predicate<Person>() {
                 @Override
                 public boolean test(Person person) throws Exception {
                     Thread.sleep(5000);
                     person.setCity("中国-上海");
                     return true;
                 }
             })
             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe(new Consumer<Person>() {
                 @Override
                 public void accept(Person person) throws Exception {
                     showMsg(person);
                 }
             });
    }
}
