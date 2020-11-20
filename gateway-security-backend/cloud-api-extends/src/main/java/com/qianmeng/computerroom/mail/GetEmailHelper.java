package com.qianmeng.computerroom.mail;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 郭超
 * Date:2020-11-18 10:49
 * Description:
 */
@Slf4j
public class GetEmailHelper {

    /**
     * 根据开发者姓名获取yml文件中的邮箱
     *
     * @param developerName 开发者姓名
     * @return 开发者邮箱
     */
    public static String getEmail(String developerName, Developer developer) {
        log.info(" developerName = " + developerName);
        List<Developer> developerVoList = developer.getList();
        log.info(" developerVoList = " + developerVoList);
        Map<String, String> developerMap = developerVoList.stream().collect(Collectors.toMap(Developer::getName, Developer::getEmail));
        return developerMap.get(developerName);
    }

}
