package com.feilx.mycontacts;

import java.util.Comparator;

/**
 * 拼音比较器
 *
 * @author Administrator
 */
public class ComparatorPy implements Comparator<Person> {

    @Override
    public int compare(Person person1, Person person2) {
        return person1.py.compareToIgnoreCase(person2.py);
    }
}
