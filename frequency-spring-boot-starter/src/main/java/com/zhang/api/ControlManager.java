package com.zhang.api;

import com.zhang.aspect.FrequencyControl;
import com.zhang.core.ControlRule;

import java.io.IOException;

/**
 * @author zhang
 * @date 2024/5/22
 * @Description
 */
public interface ControlManager {
    void controlStrategy(ControlRule controlRule, String uri, String ipAddress) throws IOException;
}
