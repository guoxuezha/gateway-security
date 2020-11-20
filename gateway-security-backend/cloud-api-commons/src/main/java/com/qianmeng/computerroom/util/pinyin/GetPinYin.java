package com.qianmeng.computerroom.util.pinyin;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * @author 郭超
 * Date:2020-08-12 17:38
 * Description: 获取拼音帮助类
 */
public class GetPinYin {

    public static String getPingYin(String src) {
        char[] t1;
        t1 = src.toCharArray();
        String[] t2;
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        StringBuilder t4 = new StringBuilder();
        try {
            for (char c : t1) {
                // 判断是否为汉字字符
                if (Character.toString(c).matches("[\\u4E00-\\u9FA5]+")) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(c, t3);
                    t4.append(t2[0]);
                } else {
                    t4.append(c);
                }
            }
            return t4.toString();
        } catch (BadHanyuPinyinOutputFormatCombination e1) {
            e1.printStackTrace();
        }
        return t4.toString();
    }

    /**
     * 得到中文首字母
     *
     * @param str 汉字
     * @return 中文首字母
     */
    public static String getPinYinHeadChar(String str) {
        StringBuilder convert = new StringBuilder();
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert.append(pinyinArray[0].charAt(0));
            } else {
                convert.append(word);
            }
        }
        return convert.toString();
    }

    /**
     * 将字符串转移为ASCII码
     *
     * @param cnStr 待转换字符串
     * @return ASCⅡ码
     */
    public static String getCnASCII(String cnStr) {
        StringBuilder strBuf = new StringBuilder();
        byte[] bytes = cnStr.getBytes();
        for (byte aByte : bytes) {
            strBuf.append(Integer.toHexString(aByte & 0xff));
        }
        return strBuf.toString();
    }

}
