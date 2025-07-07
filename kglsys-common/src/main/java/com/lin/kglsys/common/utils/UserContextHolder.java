package com.lin.kglsys.common.utils;

/**
 * 用户上下文持有工具类
 * 使用ThreadLocal在请求线程中传递用户信息
 */
public final class UserContextHolder {

    private static final ThreadLocal<Long> USER_ID_HOLDER = new ThreadLocal<>();

    private UserContextHolder() {}

    /**
     * 设置当前线程的用户ID
     * @param userId 用户ID
     */
    public static void setUserId(Long userId) {
        USER_ID_HOLDER.set(userId);
    }

    /**
     * 获取当前线程的用户ID
     * @return 用户ID，如果未设置则返回null
     */
    public static Long getUserId() {
        return USER_ID_HOLDER.get();
    }

    /**
     * 清除当前线程的用户信息
     * 必须在请求处理完成后（如在拦截器或过滤器的finally块中）调用
     */
    public static void clear() {
        USER_ID_HOLDER.remove();
    }
}