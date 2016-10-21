package littlerx;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by iceGeneral on 2016/10/21.
 */
public final class OperatorZip<R> implements Operator<R, Observable<?>[]> {

    final FuncN<? extends R> zipFunction;

    public OperatorZip(Func2 f) {
        zipFunction = FuncN.fromFunc(f);
    }

    @Override
    public Subscriber<? super Observable<?>[]> call(Subscriber<? super R> child) {
        final Zip<R> zipper = new Zip<R>(child, zipFunction);
        final ZipSubscriber subscriber = new ZipSubscriber(child, zipper);
        return subscriber;
    }

    private final class ZipSubscriber extends Subscriber<Observable[]> {

        final Subscriber<? super R> child;
        final Zip<R> zipper;

        public ZipSubscriber(Subscriber<? super R> child, Zip<R> zipper) {
            this.child = child;
            this.zipper = zipper;
        }

        @Override
        public void onNext(Observable[] observables) {
            zipper.start(observables);
        }
    }

    static final class Zip<R> {
        final Subscriber<? super R> child;
        private final FuncN<? extends R> zipFunction;
        private Subscriber[] subscribers;

        public Zip(final Subscriber<? super R> child, FuncN<? extends R> zipFunction) {
            this.child = child;
            this.zipFunction = zipFunction;
        }

        public void start(Observable[] observables) {
            final int length = observables.length;
            subscribers = new Subscriber[length];
            for (int i = 0; i < observables.length; i++) {
                InnerSubscriber subscriber = new InnerSubscriber();
                subscribers[i] = subscriber;
            }
            for (int i = 0; i < observables.length; i++) {
                observables[i].subscribe(subscribers[i]);
            }
        }

        private void tick() {
            final int length = subscribers.length;
            final Object[] objs = new Object[length];
            for (int i = 0; i < length; i++) {
                InnerSubscriber subscriber = (InnerSubscriber) subscribers[i];
                objs[i] = subscriber.queue.peek();
                if (objs[i] == null) {
                    return;
                }
            }
            for (int i = 0; i < length; i++) {
                InnerSubscriber subscriber = (InnerSubscriber) subscribers[i];
                subscriber.queue.poll();
            }
            child.onNext(zipFunction.call(objs));
        }

        final class InnerSubscriber extends Subscriber {
            Queue queue = new LinkedList();

            @Override
            public void onNext(Object o) {
                queue.offer(o);
                tick();
            }
        }

    }

}
