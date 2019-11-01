package com.unionman.webuploader.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Array;
import java.util.*;

/**
 * 判断工具类
 * @date 2019/01/15 09:18
 * @author Rong.Jia
 */
@Slf4j
public class AssertUtils {

    /**
     *  判断obj 是否为空
     * @param obj 判断值
     * @return boolean
     */
    public static boolean isNull (Object obj) {

        if (obj == null) {
            return true;
        }

        if (obj instanceof String) {
            if (StringUtils.isBlank(obj.toString())) {
                return true;
            }
        }

        if (obj instanceof Optional) {
            return !((Optional) obj).isPresent();
        }

        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof Collection) {

            Boolean flag = Boolean.FALSE;

            flag = CollectionUtils.isEmpty((Collection<?>) obj);

            if (!flag) {
                if (obj instanceof Set) {
                    List<?> list = new ArrayList<>((Set)obj);
                    if (AssertUtils.isNull(list.get(0))) {
                        flag = Boolean.TRUE;
                    }
                }
                if (obj instanceof List) {
                    List list = (List) obj;
                    if (AssertUtils.isNull(list.get(0))) {
                        flag = Boolean.TRUE;
                    }
                }
                if (obj instanceof Map) {
                    Map map = (Map) obj;
                    if (AssertUtils.isNull(new ArrayList(map.keySet()).get(0))) {
                        flag = Boolean.TRUE;
                    }
                }
            }
            return flag;
        }
        if (obj instanceof Map) {
            return MapUtils.isEmpty((Map)obj);
        }

        if (obj instanceof MultipartFile) {
            return ((MultipartFile) obj).isEmpty();
        }

        return false;

    }

    /**
     *  判断obj 是否不为空
     * @param obj 判断值
     * @return boolean
     */
    public static boolean isNotNull (Object obj) {
        return !isNull(obj);
    }

    /**
     *  判断obj 全部不为空
     * @param values 判断值
     * @return boolean
     */
    public static boolean allNotNull (final Object... values) {
        if (values == null) {
            return false;
        }

        for (final Object val : values) {
            if (val == null) {
                return false;
            }
        }

        return true;
    }

    /**
     *  判断第一个不为空， 第二个为空
     * @return
     */
    public static boolean isFirstNotNullAndSecondNull(Object obj, Object obj2) {

        if (isNotNull(obj) && isNull(obj2)) {
            return true;
        }

        return false;

    }

    /**
     *  判断 是否 小于0
     * @return
     */
    public static boolean isNumberLess0(Integer int1) {

        if (isNull(int1)) {
            return false;
        }

        if (NumberUtils.compare(int1, 0) == -1) {
            return true;
        }

        return false;

    }

    /**
     *  判断 是否 大于等于0
     * @return
     */
    public static boolean isNumberGreater0(Integer int1) {
        return !isNumberLess0(int1);

    }

    public static boolean isEquals(Object obj, Object obj1) {

        if (obj instanceof Number && obj1 instanceof Number) {
            return obj.equals(obj1);
        }

        if (obj instanceof String && obj1 instanceof String) {

            return StringUtils.equals(obj1.toString(), obj.toString());

        }

        return false;

    }






}
