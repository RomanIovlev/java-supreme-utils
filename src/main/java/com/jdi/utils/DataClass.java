package com.jdi.utils;
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
 * Created by Roman Iovlev on 10.27.2017
 */
import com.jdi.utils.func.JAction1;
import com.jdi.utils.map.MapArray;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static com.jdi.utils.LinqUtils.first;
import static com.jdi.utils.PrintUtils.printFields;

public class DataClass<T> {
    public T set(JAction1<T> valueFunc) {
        T thisObj = (T)this;
        valueFunc.execute(thisObj);
        return thisObj;
    }
    public Map<String, Object> asMap() {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields)
            try {
                map.put(field.getName(), field.get(this));
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        return map;
    }
    public MapArray<String, Object> asMapArray() {
        Field[] fields = getClass().getDeclaredFields();
        return new MapArray<>(fields,
            field -> field.getName(), f -> f.get(this));
    }

    @Override
    public String toString() {
        return printFields(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        try {
            Field[] otherFields = getClass().getDeclaredFields();
            for (Field f : getClass().getDeclaredFields()) {
                Field fOther = first(otherFields, fo -> fo.getName().equals(f.getName()));
                if (f.get(this) == null && fOther.get(o) == null)
                    continue;
                if (f.get(this) != null && fOther.get(o) == null ||
                    f.get(this) == null && fOther.get(o) != null ||
                    !f.get(this).equals(fOther.get(o)))
                    return false;
                }
        }
        catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (Field f : getClass().getDeclaredFields())
            try { result += 31 * result + f.get(this).hashCode();
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        return result;
    }
}
