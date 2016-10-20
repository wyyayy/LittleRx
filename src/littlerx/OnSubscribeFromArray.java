package littlerx;

import littlerx.OnSubscribe;
import littlerx.Subscriber;

/**
 * Created by iceGeneral on 2016/10/20.
 */
public final class OnSubscribeFromArray<T> implements OnSubscribe<T> {
    final T[] array;

    public OnSubscribeFromArray(T[] array) {
        this.array = array;
    }

    @Override
    public void call(Subscriber<? super T> subscriber) {
        for (T t : array) {
            subscriber.onNext(t);
        }
    }

}
