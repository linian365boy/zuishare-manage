package top.zuishare.util;

/**
 * @author niange
 * @ClassName: TimeUtils
 * @desp:
 * @date: 2017/12/23 下午1:46
 * @since JDK 1.7
 */
public class TimeUtils {
    /**
     * 获取当前时间，单位为秒（S）
     * @return
     */
    public static long getTime(){
        return System.currentTimeMillis()/1000;
    }

}
