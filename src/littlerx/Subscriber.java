package littlerx;

import littlerx.producer.Producer;

/**
 * Created by iceGeneral on 2016/10/15.
 */
public abstract class Subscriber<T> {

    public abstract void onNext(T t);

    public void setProducer(Producer p) {

    }

}
