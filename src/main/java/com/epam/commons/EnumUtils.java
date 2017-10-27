package com.epam.commons;
/* The MIT License
 *
 * Copyright 2004-2017 EPAM Systems
 *
 * This file is part of JDI project.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:

 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 */

/**
 * Created by Roman Iovlev on 10.03.2017
 */
import java.lang.reflect.Field;
import java.util.List;

import static com.epam.commons.TryCatchUtil.tryGetResult;
import static java.util.Arrays.asList;

public final class EnumUtils {

    private EnumUtils() {
    }

    public static String getEnumValue(Enum enumWithValue) {
        Class<?> type = enumWithValue.getClass();
        Field[] fields = type.getDeclaredFields();
        Field field;
        switch (fields.length) {
            case 0:
                return enumWithValue.toString();
            case 1:
                field = fields[0];
                break;
            default:
                try {
                    field = type.getField("value");
                } catch (NoSuchFieldException ex) {
                    return enumWithValue.toString();
                }
                break;
        }
        return tryGetResult(() -> field.get(enumWithValue).toString());
    }

    public static <T extends Enum> List<T> getAllEnumValues(Class<T> enumValue) {
        return asList(getAllEnumValuesAsArray(enumValue));
    }

    public static <T extends Enum> T[] getAllEnumValuesAsArray(Class<T> enumValue) {
        return enumValue.getEnumConstants();
    }

    public static <T extends Enum> List<String> getAllEnumNames(Class<T> enumValue) {
        return LinqUtils.select(getAllEnumValuesAsArray(enumValue), EnumUtils::getEnumValue);
    }

    public static <T extends Enum> String[] getAllEnumNamesAsArray(Class<T> enumValue) {
        return LinqUtils.toStringArray(getAllEnumNames(enumValue));
    }
}