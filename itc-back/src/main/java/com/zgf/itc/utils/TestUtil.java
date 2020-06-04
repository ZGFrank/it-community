package com.zgf.itc.utils;

import com.zgf.itc.entity.User;
import org.junit.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * @author NYF
 */
public class TestUtil {

    @Test public void test1() {
        for (Field field : User.class.getDeclaredFields()) {
            ExcelFields desc = field.getAnnotation(ExcelFields.class);
            if (desc != null) {
                System.out.println(desc.value());
            }
        }

    }

    @Test
    public void test2() {

        System.out.println(Integer.parseInt(new BigDecimal("2.016212056E9").toPlainString()));
    }
}
