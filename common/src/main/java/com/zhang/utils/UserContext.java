package com.zhang.utils;

import com.zhang.entity.User;

/**
 * @author zhang
 * @date 2024/5/24
 * @Description
 */
public class UserContext {
    private static final ThreadLocal<String> userThreadLocal = new ThreadLocal<>();
    public static void set(String userId) {
        userThreadLocal.set(userId);
    }
    public static String get() {
        return userThreadLocal.get();
    }
    public static void remove() {
        userThreadLocal.remove();
    }
}
