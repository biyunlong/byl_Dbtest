package com.ashenman.byl.dbtest.modle;

/**
 * Created by android on 2016/9/6.
 */
public class User {
    private String name;
    private int age;


    private String school;
    private int idnum;
    public User() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }
    public int getIdnum() {
        return idnum;
    }

    public void setIdnum(int idnum) {
        this.idnum = idnum;
    }


    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("姓名：" + name + "\t");
        buffer.append("年龄：" + age + "\t");
        buffer.append("学校：" + school + "\t");
        buffer.append("序号：" + idnum + "\n");
        return buffer.toString();

    }
}
