package org.ckzs.ckdp.pojo;

public class BaseContext {
    private static ThreadLocal<Integer> currentUserId = new ThreadLocal<>();

    // 设置当前用户 ID
    public static void setCurrentUserId(Integer userId) {
        currentUserId.set(userId);
    }

    // 获取当前用户 ID
    public static Integer getCurrentUserId() {
        return currentUserId.get();
    }

    // 清理当前线程中的用户 ID
    public static void clear() {
        currentUserId.remove();
    }
}
