package logic;

/**
 * 需要考虑退出时执行任务的就实现此方法
 *
 * @author Vant
 * @version 2017/10/11 下午 9:05
 */
public interface Exitable {
    /**
     * 因为退出通知次数不多,所以采用此方法
     */
    void onExit();

    /**
     * 控制系统关闭的类就可以通过这个方法来查询某个类是否已经关闭了,
     * 每个类都应该在关闭事件结束后就设置此方法返回true
     *
     * @return
     */
    boolean isExited();
}