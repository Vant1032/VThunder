package logic;

/**
 * 实现了这个接口的类表示可被观察,它会通知其他类关于自己的改变
 * @author Vant
 * @version 2017/10/12 下午 2:24
 * @param <T> 作为被通知者要实现的方法
 */
public interface Observerable<T> {
    boolean addListener(T listener);

    boolean removeListener(T listener);

    void informAll();
}