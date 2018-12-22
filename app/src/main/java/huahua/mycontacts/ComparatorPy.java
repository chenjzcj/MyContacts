package huahua.mycontacts;

import java.util.Comparator;

/**
 * 拼音比较器
 *
 * @author Administrator
 */
public class ComparatorPy implements Comparator<Persons> {

    @Override
    public int compare(Persons persons1, Persons persons2) {
        return persons1.py.compareToIgnoreCase(persons2.py);
    }
}
