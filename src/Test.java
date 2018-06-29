import littlerx.*;
import os.Handler;

/**
 * Created by iceGeneral on 2016/10/15.
 */
public class Test {
    public static void main(String[] args) 
    {
    	System.out.println("22222222222");    	
    	
        Observable
                .create(new OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        subscriber.onNext(1);
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        System.out.println(integer);
                    }
                });
        System.out.println("-------------------------------------------------------------");


        Observable
                .create(new OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        subscriber.onNext(2);
                    }
                })
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return "map" + integer;
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onNext(String s) {
                        System.out.println(s);
                    }
                });
        System.out.println("-------------------------------------------------------------");


        Observable
                .create(new OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        System.out.println("create() " + Thread.currentThread());
                        subscriber.onNext(3);
                    }
                })
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        System.out.println("map() " + Thread.currentThread());
                        return "map" + integer;
                    }
                })
                .subscribeOnIO()
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onNext(String s) {
                        System.out.println("onNext() " + Thread.currentThread());
                        System.out.println(s);
                        System.out.println("-------------------------------------------------------------");
                    }
                });


        Observable
                .just(4, 5) // or from(new int[]{4, 5})
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        System.out.println(integer);
                    }
                });
        System.out.println("-------------------------------------------------------------");


        Observable
                .create(new OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        subscriber.onNext(6);
                    }
                })
                .flatMap(new Func1<Integer, Observable<String>>() {
                    @Override
                    public Observable<String> call(Integer integer) {
                        return Observable.just("flatMap" + integer, "flatMap" + 7);
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onNext(String s) {
                        System.out.println(s);
                    }
                });
        System.out.println("-------------------------------------------------------------");


        Observable<Integer> o1 = Observable.create(new OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(8);
                subscriber.onNext(9);
            }
        });
        Observable<String> o2 = Observable.create(new OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("A");
                subscriber.onNext("B");
                subscriber.onNext("C");
            }
        });
        Observable.zip(o1, o2, new Func2<Integer, String, String>() {
            @Override
            public String call(Integer integer, String s) {
                return "zip" + integer + s;
            }
        }).subscribe(new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                System.out.println(s);
            }
        });
        System.out.println("-------------------------------------------------------------");


        Handler handler = new Handler();
        Observable
                .create(new OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        System.out.println("create() " + Thread.currentThread());
                        subscriber.onNext(10);
                    }
                })
                .observeOn(handler)
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        System.out.println("map() " + Thread.currentThread());
                        return "map" + integer;
                    }
                })
                .subscribeOnIO()
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onNext(String s) {
                        System.out.println("onNext() " + Thread.currentThread());
                        System.out.println(s);
                        System.out.println("-------------------------------------------------------------");
                    }
                });
        handler.loop();

    }

}
