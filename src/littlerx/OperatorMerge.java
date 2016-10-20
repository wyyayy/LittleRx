package littlerx;

/**
 * Created by iceGeneral on 2016/10/19.
 */
public final class OperatorMerge<T> implements Operator<T, Observable<? extends T>> {

    @Override
    public Subscriber<? super Observable<? extends T>> call(Subscriber<? super T> o) {
        MergeSubscriber<T> subscriber = new MergeSubscriber<T>(o);
        return subscriber;
    }

    static final class MergeSubscriber<T> extends Subscriber<Observable<? extends T>> {
        final Subscriber<? super T> actual;

        public MergeSubscriber(Subscriber<? super T> o) {
            this.actual = o;
        }

        @Override
        public void onNext(Observable<? extends T> observable) {
            observable.subscribe(actual);
        }
    }
}
