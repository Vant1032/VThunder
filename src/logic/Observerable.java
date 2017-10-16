package logic;

/**
 * @author Vant
 * @version 2017/10/12 下午 2:24
 */
public interface Observerable<T> {
    boolean addListener(T listener);

    boolean removeListener(T listener);

    void informAll();
}
