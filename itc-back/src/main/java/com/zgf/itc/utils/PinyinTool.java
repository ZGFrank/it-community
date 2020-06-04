package com.zgf.itc.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.UUID;

/**
 * @author ZGF
 */
public class PinyinTool {
    private static HanyuPinyinOutputFormat format = null;

    public static enum Type {

        UPPERCASE,              //全部大写

        LOWERCASE,              //全部小写

        FIRSTUPPER              //首字母大写

    }


    static {

        format = new HanyuPinyinOutputFormat();

        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);

        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

    }

    public static void main(String[] args) throws BadHanyuPinyinOutputFormatCombination {
        System.out.println(UUID.randomUUID().toString().replace("-", ""));
    }

    public static String toPinYin(String str, Type type) {

        try {
            return toPinYin(str, "", type);
        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
            int start = (int) (Math.random() * 26);
            return UUID.randomUUID().toString().replace("-", "").substring(start, start + 6);
        }

    }


    public String toPinYin(String str, String spera) throws BadHanyuPinyinOutputFormatCombination {

        return toPinYin(str, spera, Type.UPPERCASE);

    }


    /**
     * 将str转换成拼音，如果不是汉字或者没有对应的拼音，则不作转换
     * <p>
     * 如： 明天 转换成 MINGTIAN
     *
     * @param str：要转化的汉字
     * @param spera：转化结果的分割符
     * @return
     * @throws BadHanyuPinyinOutputFormatCombination
     */

    private static String toPinYin(String str, String spera, Type type) throws BadHanyuPinyinOutputFormatCombination {

        if (str == null || str.trim().length() == 0) {
            return "";
        }

        if (type == Type.UPPERCASE) {
            format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        } else {
            format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        }


        StringBuilder py = new StringBuilder();

        String temp = "";

        String[] t;

        for (int i = 0; i < str.length(); i++) {

            char c = str.charAt(i);

            if ((int) c <= 128) {

                py.append(c);
            } else {
                t = PinyinHelper.toHanyuPinyinStringArray(c, format);
                if (t == null) {
                    py.append(c);
                } else {
                    temp = t[0];
                    if (type == Type.FIRSTUPPER) {
                        temp = t[0].toUpperCase().charAt(0) + temp.substring(1);
                    }
                    py.append(temp).append(i == str.length() - 1 ? "" : spera);

                }

            }

        }
        return py.toString().trim();

    }
}
