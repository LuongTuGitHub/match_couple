package com.android.database.local.no_sql.module;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.android.database.local.no_sql.annotation.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JSON {


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String convertJSON(@NotNull Object object) throws InvocationTargetException, IllegalAccessException {
        // TODO
        StringBuilder result = new StringBuilder("{");
        Method[] methods = object.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isBridge()) {
                if (method.getParameterCount() == 0) {
                    if (!(method.getReturnType() == Void.class)) {
                        String name = method.getName();
                        String value = method.invoke(object).toString();
                        Class<?> type = method.getReturnType();
                        if (type == String.class) {
                            if (value == null || value.equals("") || value.length() == 0) {
                                result.append("'").append(name.substring(3).toLowerCase()).append("' : ").append("null").append(",");
                            } else {
                                result.append("'").append(name.substring(3).toLowerCase()).append("' : '").append(value).append("',");
                            }
                        } else {
                            result.append("'").append(name.substring(3).toLowerCase()).append("' : ").append(value).append(",");
                        }
                    }
                }
            }

        }

        String value = result.toString();
        if (value.length() > 1) {
            if ((value.charAt(value.length() - 1) + "").equals(",")) {
                value = value.substring(0, value.length() - 1);
                result = new StringBuilder().append(value);
            }
        }
        result.append("}");
        return result.toString();
    }

}