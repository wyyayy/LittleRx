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

    public static <T> Observable<T> just(final T... values) {
        return from(values);
    }

    public static <T> Observable<T> from(T[] array) {
        return create(new OnSubscribeFromArray<T>(array));
    }

    public static <T> Observable<T> merge(Observable<? extends T>... sequences) {
        return merge(from(sequences));
    }

    private static <T> Observable<T> merge(Observable<? extends Observable<? extends T>> source) {
        return source.lift(new OperatorMerge<T>());
    }

}