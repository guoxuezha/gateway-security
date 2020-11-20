package com.qianmeng.computerroom.util.autoincrement;

import java.util.List;
import java.util.TreeSet;

/**
 * @author 郭超
 * Date:2020-08-12 17:53
 * Description: 提供硬件编号(hardware_number)数字部分自增策略
 */
public class HardwareNumberAutoincrement {

    /**
     * 筛选查询到的List集合,取出最大值并+1返回
     *
     * @param titleCase  硬件类型的首字母
     * @param numberList 某类硬件编号集合
     * @return 新增硬件的硬件编号
     */
    public static String getMaxNumber(String titleCase, List<String> numberList) {
        // treeSet具有自动去重和排序的功能
        TreeSet numberSet = new TreeSet();

        if (numberList.size() > 0) {
            String sample = numberList.get(0);
            int startIndex = sample.length() - 6;
            int endIndex = sample.length();
            // 截取字符串集合的后6位,转为Integer存入Set
            for (String number : numberList) {
                String numberPart = number.substring(startIndex, endIndex);
                Integer num = Integer.parseInt(numberPart);
                numberSet.add(num);
            }
            return titleCase + String.format("%06d", Integer.parseInt(numberSet.last().toString()) + 1);
        } else {
            // 如果list大小为0,则直接赋予编号'XX000001'
            return titleCase + "000001";
        }
    }

}
