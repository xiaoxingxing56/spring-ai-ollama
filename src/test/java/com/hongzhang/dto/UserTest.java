package com.hongzhang.dto;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void test01() throws ClassNotFoundException {
        Integer i = 128;
        Integer j = 128;
        System.out.println(i == j);
        System.out.println(i.equals(j));

        Class<?> aClass = Class.forName("com.hongzhang.dto.User");

        ClassLoader classLoader = aClass.getClassLoader();
        Class<?> aClass1 = classLoader.loadClass("com.hongzhang.dto.User");

        Field[] declaredFields = aClass1.getDeclaredFields();

        for (Field declaredField : declaredFields) {
            System.out.println(declaredField.getName());
        }
    }

}