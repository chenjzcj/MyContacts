package com.feilx.mycontacts;

/**
 * 联系人包含的信息
 *
 * @author Administrator
 */
public class Person {
    /**
     * 姓名
     */
    public String name;

    /**
     * 姓名拼音 (花无缺:huawuque)
     */
    public String py;
    /**
     * 电话号码
     */
    public String number;

    /**
     * 中文名首字母 (花无缺:hwq)
     */
    public String fisrtspell;

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", py='" + py + '\'' +
                ", number='" + number + '\'' +
                ", fisrtspell='" + fisrtspell + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPy() {
        return py;
    }

    public void setPy(String py) {
        this.py = py;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getFisrtspell() {
        return fisrtspell;
    }

    public void setFisrtspell(String fisrtspell) {
        this.fisrtspell = fisrtspell;
    }
}
