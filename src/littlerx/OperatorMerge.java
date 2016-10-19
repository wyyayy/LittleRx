package littlerx;

/**
 * Created by Apple on 2016/10/19.
 */
public final class OperatorMerge<T> implements Operator<T, Observable<? extends T>> {

    @Override
    public Subscriber<? super Observable<? extends T>> call(Subscriber<? super T> subscriber) {
        return null;
    }

}
