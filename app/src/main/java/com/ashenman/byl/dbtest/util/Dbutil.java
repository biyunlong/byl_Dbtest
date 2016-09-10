package com.ashenman.byl.dbtest.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by android on 2016/9/7.
 */
public class Dbutil {
    public static String creatDbName(Class c) {
        HashMap map = getClassAttribute(c);
        StringBuffer buffer = new StringBuffer();
        buffer.append("create table ").append(c.getSimpleName() + "db").append(" (");
        buffer.append("_id integer primary key autoincrement, ");

        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            buffer.append(entry.getKey()).append(" ");
//            buffer.append(entry.getValue());
            if (entry.getValue().equals("BOOLEAN")) {
                buffer.append("INTEGER");
            } else {
                buffer.append(entry.getValue());
            }
            if (it.hasNext()) {
                buffer.append(", ");
            } else {
                buffer.append(")");
            }
        }
        return buffer.toString();
    }

    /**
     *  获取属性类型(type)，属性名(name)，属性值(value)的map组成的list
     *  
     */
    public static ContentValues getContentValues(Object o) {
        ContentValues content = new ContentValues();
        Field[] fields = o.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String type = field.getType().toString();
            String name = field.getName();
            field.setAccessible(true);

            if (type.equals("class java.lang.String")) {
                String value = getFieldValue(o, name,String.class);
                content.put(name, null==value?"":value);
            } else if (type.equals("int")) {
                int value = (int)getFieldValue(o, name,Integer.class);
                content.put(name, value);
            } else if (type.equals("float")) {
                Float value = getFieldValue(o, name,float.class);
                content.put(name, null==value?0:value);
            } else if (type.equals("boolean")) {
                boolean value = getFieldValue(o, name,boolean.class);
                if (value == false) {
                    content.put(name, 0);
                } else {
                    content.put(name, 1);
                }

            }
        }
        return content;
    }


    public static HashMap<String, String> getClassAttribute(Class c) {
        HashMap<String, String> map = new HashMap<>();
        Field[] fields = c.getDeclaredFields();
        for (int i = 0; i < fields.length - 1; i++) {
            String key = fields[i].getName();
            String value = parseValue(fields[i].getType().toString());
            map.put(key, value);
        }
        return map;
    }

    public static String parseValue(String str) {
        String s = "";
        if (str.equals("class java.lang.String")) {
            return "TEXT";
        } else if (str.equals("int")) {
            return "INTEGER";
        } else if (str.equals("float")) {
            return "FLOAT";
        } else if (str.equals("boolean")) {
            return "BOOLEAN";
        } else {
            return str;
        }
    }


    /**
     *  
     *     * 通过反射，用属性名称获得属性值 
     *     * @param thisClass 需要获取属性值的类 
     *     * @param fieldName 该类的属性名称 
     *     * @return 
     */
    private static<T> T getFieldValue(Object thisClass, String fieldName,Class<T> t) {
        T value =null;
        Method method = null;
        try {
            String methodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            method = thisClass.getClass().getMethod("get" + methodName);
            value = (T)method.invoke(thisClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static <T> T getBean(Class<T> cls) {
        Constructor[] cons = null;//得到所有构造器
        try {
            cons = cls.getConstructors();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                throw new Exception("构造器错误！");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        if (cons == null || cons.length < 1) {
            try {
                throw new Exception("没有默认构造方法！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //如果上面没错，就有构造方法

        Constructor defCon = cons[0];//得到默认构造器,第0个是默认构造器，无参构造方法
        T obj = null;//实例化，得到一个对象 //Spring - bean -id
        try {
            obj = (T) defCon.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static void setValue(Method[] methods, String propertyName, Object propertyValue, Object bean) {
        for (Method m : methods) {
            if (m.getName().equalsIgnoreCase("set" + propertyName)) {
                //找到方法就注入
                try {
                    m.invoke(bean, propertyValue);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
    /**
     * 从数据库得到实体类
     *
     * @return
     */
    public static <T> List getEntity(Cursor c, Class<T> t) {
        List list = new ArrayList<>();
        HashMap map = getClassAttribute(t);
        Method[] methods = t.getMethods();//得到所有方法
        try{
            while (c.moveToNext()) {
                T obj = getBean(t);
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                int i = 0;
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = it.next();
                    String name = entry.getKey();
                    String type = entry.getValue();

                    if (type.equals("TEXT")) {
                        String   value = c.getString(c.getColumnIndex(name));
                        setValue(methods, name, value, obj);
                    } else if (type.equals("INTEGER")) {
                        int value = c.getInt(c.getColumnIndex(name));
                        setValue(methods, name, value, obj);
                    } else if (type.equals("FLOAT")) {
                        float  value = c.getFloat(c.getColumnIndex(name));
                        setValue(methods, name, value, obj);
                    } else if (type.equals("BOOLEAN")) {
                        int  valueint = c.getInt(c.getColumnIndex(name));
                        if (valueint == 1) {
                            boolean value = true;
                            setValue(methods, name, value, obj);
                        } else {
                            boolean  value = false;
                            setValue(methods, name, value, obj);
                        }

                    }

                }
                list.add(obj);
            }
        }catch (Exception e){
        }finally {
            if (c != null) {
                c.close();
            }
        }
        return list;
    }


}
