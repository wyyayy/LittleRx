package littlerx;

import os.Handler;

/**
 * Created by iceGeneral on 2016/10/15.
 */
public class Observable<T> {

    private OnSubscribe<T> onSubscribe;

    private Observable(OnSubscribe<T> onSubscribe) {
        this.onSubscribe = onSubscribe;
    }

    public final void subscribe(Subscriber<? super T> subscriber) {
        onSubscribe.call(subscriber);
    }

    public final <R> Observable<R> map(Func1<? super T, ? extends R> func) {
        return lift(new OperatorMap<T, R>(func));
    }

    public final <R> Observable<R> lift(final Operator<? extends R, ? super T> operator) {
        return new Observable<>(new OnSubscribeLift<T, R>(onSubscribe, operator));
    }

    public final Observable<T> subscribeOnIO() {
        return create(new OnSubscribeOnIO<T>(this));
    }

    public final Observable<T> observeOn(Handler handler) {
        return lift(new OperatorObserveOn<T>(handler));
    }

    public final <R> Observable<R> flatMap(Func1<? super T, ? extends Observable<? extends R>> func) {
        return merge(map(func));
    }

    public static <T> Observable<T> create(OnSubscribe<T> onSubscribe) {
        return new Observable<>(onSubscribe);
    }

    public static <T> Observable<T> just(final T value) {
        return create(new OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                subscriber.onNext(value);
            }
        });
    }

    public static <T> Observable<T> just(final T t1, final T t2) {
        return from((T[]) new Object[]{t1, t2});
    }

    public static <T> Observable<T> from(T[] array) {
        return create(new OnSubscribeFromArray<T>(array));
    }

    public static <T> Observable<T> merge(Observable<? extends T> t1, Observable<? extends T> t2) {
        return merge(from((Observable<? extends T>[]) new Observable[]{t1, t2}));
    }

    private static <T> Observable<T> merge(Observable<? extends Observable<? extends T>> source) {
        return source.lift(new OperatorMerge<T>());
    }

    public static <T1, T2, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, final Func2<? super T1, ? super T2, ? extends R> zipFunction) {
        return just(new Observable<?>[]{o1, o2}).lift(new OperatorZip<R>(zipFunction));
    }

}