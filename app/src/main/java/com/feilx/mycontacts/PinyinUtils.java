package com.feilx.mycontacts;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 拼音帮助类
 */
public class PinyinUtils {
    /**
     * 将字符串中的中文转化为拼音,其他字符不变
     * 花无缺->huawuque
     *
     * @param inputString 需要获取拼音的包含汉字字符串
     * @return 包含汉字字符串转换后的拼音
     */
    public static String getPingYin(String inputString) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        char[] input = inputString.trim().toCharArray();
        StringBuilder output = new StringBuilder();
        try {
            for (char curchar : input) {
                if (java.lang.Character.toString(curchar).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(curchar, format);
                    output.append(temp[0]);
                } else {
                    output.append(Character.toString(curchar));
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    /**
     * 汉字转换为汉语拼音首字母，英文字符不变
     * 花无缺->hwq
     *
     * @param chinese 汉字
     * @return 拼音
     */
    public static String getFirstSpell(String chinese) {
        StringBuilder pybf = new StringBuilder();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (char curchar : arr) {
            if (curchar > 128) {
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(curchar, defaultFormat);
                    if (temp != null) {
                        pybf.append(temp[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(curchar);
            }
        }
        return pybf.toString().replaceAll("\\W", "").trim();
    }

}
