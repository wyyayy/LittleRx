package littlerx;

/**
 * Created by iceGeneral on 2016/10/21.
 */
public interface FuncN<R> {
    R call(Object... args);

    static <T0, T1, R> FuncN<R> fromFunc(final Func2<? super T0, ? super T1, ? extends R> f) {
        return new FuncN<R>() {

            @SuppressWarnings("unchecked")
            @Override
            public R call(Object... args) {
                if (args.length != 2) {
                    throw new RuntimeException("Func2 expecting 2 arguments.");
                }
                return f.call((T0) args[0], (T1) args[1]);
            }

        };
    }
}
