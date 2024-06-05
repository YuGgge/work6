package com.zhang.core;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * 属性设置
 * @author zhang
 * &#064;date  2024/4/30
 * &#064;Description
 */
@ConfigurationProperties(prefix = "frequency-control")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ControlProperties {
    /**
     * limit是否可用
     */

    private boolean enabled = true;


    /*@Autowired
    public ControlProperties(@Value("${frequency-control.enabled}") boolean enabled) {
        this.enabled = enabled;
    }
    *//**
     * 频率控制配置规则
     * 嵌套的配置属性
     */
    @NestedConfigurationProperty
    private ControlRule controlRule = new ControlRule().init();

    public ControlProperties(boolean enabled) {
        this.enabled = enabled;
    }

    public ControlProperties(ControlRule controlRule) {
        this.controlRule = controlRule;
    }
}
