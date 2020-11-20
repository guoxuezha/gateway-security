package com.qianmeng.computerroom.mail;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qianmeng
 * className Developer
 * description TODO
 * date 2020/7/11 13:32
 **/
@Data
@Component
@ConfigurationProperties(prefix = "developer")
public class Developer {

    /**
     * 姓名
     */
    private String name;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 自身列表
     */
    private List<Developer> list = new ArrayList<>();
}
