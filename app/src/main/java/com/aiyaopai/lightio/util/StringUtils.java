package com.aiyaopai.lightio.util;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {


    /**
     * @param targetStr 要处理的字符串
     * @description 切割字符串，将文本和img标签碎片化，如"ab<img>cd"转换为"ab"、"<img>"、"cd"
     */
    public static List<String> cutStringByImgTag(String targetStr) {
        List<String> splitTextList = new ArrayList<String>();
        Pattern pattern = Pattern.compile("<img.*?src=\\\"(.*?)\\\".*?>");
        Matcher matcher = pattern.matcher(targetStr);
        int lastIndex = 0;
        while (matcher.find()) {
            if (matcher.start() > lastIndex) {
                splitTextList.add(targetStr.substring(lastIndex, matcher.start()));
            }
            splitTextList.add(targetStr.substring(matcher.start(), matcher.end()));
            lastIndex = matcher.end();
        }
        if (lastIndex != targetStr.length()) {
            splitTextList.add(targetStr.substring(lastIndex, targetStr.length()));
        }
        return splitTextList;
    }

    /**
     * 获取img标签中的src值
     *
     * @param content
     * @return
     */
    public static String getImgSrc(String content) {
        String str_src = null;
        //目前img标签标示有3种表达式
        //<img alt="" src="1.jpg"/>   <img alt="" src="1.jpg"></img>     <img alt="" src="1.jpg">
        //开始匹配content中的<img />标签
        Pattern p_img = Pattern.compile("<(img|IMG)(.*?)(/>|></img>|>)");
        Matcher m_img = p_img.matcher(content);
        boolean result_img = m_img.find();
        if (result_img) {
            while (result_img) {
                //获取到匹配的<img />标签中的内容
                String str_img = m_img.group(2);

                //开始匹配<img />标签中的src
                Pattern p_src = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");
                Matcher m_src = p_src.matcher(str_img);
                if (m_src.find()) {
                    str_src = m_src.group(3);
                }
                //结束匹配<img />标签中的src

                //匹配content中是否存在下一个<img />标签，有则继续以上步骤匹配<img />标签中的src
                result_img = m_img.find();
            }
        }
        return str_src;
    }

    /**
     * 关键字高亮显示
     *
     * @param target 需要高亮的关键字
     * @param text   需要显示的文字
     * @return spannable 处理完后的结果，记得不要toString()，否则没有效果
     * SpannableStringBuilder textString = TextUtilTools.highlight(item.getItemName(), KnowledgeActivity.searchKey);
     * vHolder.tv_itemName_search.setText(textString);
     */
    public static SpannableStringBuilder highlight(String text, String target) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);
        CharacterStyle span = null;

        Pattern p = Pattern.compile(target);
        Matcher m = p.matcher(text);
        while (m.find()) {
            span = new ForegroundColorSpan(Color.parseColor("#EE5C42"));// 需要重复！
            spannable.setSpan(span, m.start(), m.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }

    /**
     * 从html文本中提取图片地址，或者文本内容
     *
     * @param html       传入html文本
     * @param isGetImage true获取图片，false获取文本
     * @return
     */
    public static ArrayList<String> getTextFromHtml(String html, boolean isGetImage) {
        ArrayList<String> imageList = new ArrayList<>();
        ArrayList<String> textList = new ArrayList<>();
        //根据img标签分割出图片和字符串
        List<String> list = cutStringByImgTag(html);
        for (int i = 0; i < list.size(); i++) {
            String text = list.get(i);
            if (text.contains("<img") && text.contains("src=")) {
                //从img标签中获取图片地址
                String imagePath = getImgSrc(text);
                imageList.add(imagePath);
            } else {
                textList.add(text);
            }
        }
        //判断是获取图片还是文本
        if (isGetImage) {
            return imageList;
        } else {
            return textList;
        }
    }

    public static String getLocationName(String locationName) {
        if (!TextUtils.isEmpty(locationName)) {
            String[] location = locationName.split("/");
            if (location.length > 0) {
                return location[0];
            }
        }
        return "";
    }

    public static SpannableStringBuilder getSb(String content, int index) {
        SpannableStringBuilder sb = new SpannableStringBuilder(content);
        ForegroundColorSpan ss1 = new ForegroundColorSpan(Color.parseColor("#666666"));
        ForegroundColorSpan ss2 = new ForegroundColorSpan(Color.parseColor("#333333"));
        sb.setSpan(ss1, 0, index, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(ss2, index + 1, content.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return sb;
    }

    public static SpannableStringBuilder getRedSb(String content, int index) {
        SpannableStringBuilder sb = new SpannableStringBuilder(content);
        ForegroundColorSpan ss1 = new ForegroundColorSpan(Color.parseColor("#FF3838"));
        ForegroundColorSpan ss2 = new ForegroundColorSpan(Color.parseColor("#666666"));
        sb.setSpan(ss1, 0, index, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(ss2, index + 1, content.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return sb;
    }

    public static SpannableStringBuilder getBlueSb(String content, int index) {
        SpannableStringBuilder sb = new SpannableStringBuilder(content);
        ForegroundColorSpan ss1 = new ForegroundColorSpan(Color.parseColor("#333333"));
        ForegroundColorSpan ss2 = new ForegroundColorSpan(Color.parseColor("#FF0076FE"));
        sb.setSpan(ss1, 0, index, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(ss2, index + 1, content.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return sb;
    }

    public static SpannableStringBuilder getNameSb(String content, int index) {
        SpannableStringBuilder sb = new SpannableStringBuilder(content);
        ForegroundColorSpan ss1 = new ForegroundColorSpan(Color.parseColor("#666666"));
        ForegroundColorSpan ss2 = new ForegroundColorSpan(Color.parseColor("#FF464C5B"));
        sb.setSpan(ss1, 0, index, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(ss2, index + 1, content.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return sb;
    }

    public static SpannableStringBuilder changeTextSizeSb(String content, int index) {
        SpannableStringBuilder sb = new SpannableStringBuilder(content);
        sb.setSpan(new AbsoluteSizeSpan(14,true), 0,index, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(new AbsoluteSizeSpan(10,true), index,content.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return sb;
    }
    public static SpannableStringBuilder changeTextSizeSb(String content) {
        SpannableStringBuilder sb = new SpannableStringBuilder(content);
        sb.setSpan(new AbsoluteSizeSpan(10,true), 0,content.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return sb;
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     * @param v1 被除数
     * @param v2 除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


}