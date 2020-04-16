package com.wj.manager.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * shiro相关信息的配置类
 */
@ConfigurationProperties(prefix = "crm")
public class ShiroProperties implements Serializable{
    public String name;

    private List<String> anonUri;

    public List<String> getAnonUri() {
        return anonUri;
    }

    public void setAnonUri(List<String> anonUri) {
        this.anonUri = anonUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
