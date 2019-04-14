package com.cl.apiprotect.demo.util;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * @ClassName: ValidatorUtil
 * @Description:TODO
 * @author: chenling
 * @Date: 2019/4/14 0:28
 * @version : V1.0.0
 */
public class ValidatorUtil {

    private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");

    public static boolean isMobile(String src) {
        if(StringUtils.isEmpty(src)) {
            return false;
        }
        Matcher m = mobile_pattern.matcher(src);
        return m.matches();
    }

}
