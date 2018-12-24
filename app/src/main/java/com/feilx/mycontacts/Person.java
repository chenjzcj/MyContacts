package com.feilx.mycontacts;

/**
 * 联系人包含的信息
 *
 * @author Administrator
 */
public class Person {
    /**
     * 联系人的ID号
     */
    public String contactId;

    /**
     * 联系人姓名
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
                "contactId='" + contactId + '\'' +
                ", name='" + name + '\'' +
                ", py='" + py + '\'' +
                ", number='" + number + '\'' +
                ", fisrtspell='" + fisrtspell + '\'' +
                '}';
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
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
