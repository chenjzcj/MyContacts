package com.feilx.mycontacts;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends Activity {
    /**
     * 显示选中的字母
     */
    private TextView mLetternotice;
    /**
     * 联系人的列表
     */
    private ContactsListView mContactslist;
    /**
     * 联系人列表的适配器
     */
    private ContactsAdapter mListadapter;
    /**
     * 所有联系人数组
     */
    private ArrayList<Person> persons = new ArrayList<>();
    /**
     * 搜索过滤联系人EditText
     */
    private EditText mFilteredittext;
    /**
     * 没有匹配联系人时显示的TextView
     */
    private TextView mListemptytext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkSelfPermission();
        loadContacts();
        //得到字母列的对象,并设置触摸响应监听器

        // 根据拼音为联系人数组进行排序
        Collections.sort(persons, new ComparatorPy());

        //得到联系人列表,并设置适配器
        mContactslist = findViewById(R.id.pb_listvew);
        mListemptytext = findViewById(R.id.pb_nocontacts_notice);
        //初始化搜索编辑框,设置文本改变时的监听器
        mFilteredittext = findViewById(R.id.pb_search_edit);

        mListadapter = new ContactsAdapter(this, persons, mContactslist);
        mContactslist.setAdapter(mListadapter);
        mContactslist.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this, persons.get(position).number, Toast.LENGTH_LONG).show();
            }
        });
        mFilteredittext.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!"".equals(s.toString().trim())) {
                    //根据编辑框值过滤联系人并更新联系列表
                    filterContacts(s.toString().trim());
                } else {
                    mListadapter.updateListView(persons);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 加载联系人
     */
    private void loadContacts() {
        //获取手机中的联系人,并将所有联系人保存到perosns数组中
        //联系人比较多的话,初始化中会比较耗时,以后再优化

        //使用兼容库就无需判断系统版本
        int hasWriteStoragePermission = checkSelfPermission(Manifest.permission.READ_CONTACTS);
        if (hasWriteStoragePermission == PackageManager.PERMISSION_GRANTED) {
            //拥有权限，执行操作
            getContacts();
        } else {
            //没有权限，向用户请求权限
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, 0);
        }
    }

    /**
     * 检查权限
     */
    private void checkSelfPermission() {
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        getContacts();
    }

    /**
     * 获取手机联系人关键方法
     */
    public void getContacts() {
        // 获得所有联系人数据集的游标
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        // 循环遍历
        if (cursor != null && cursor.moveToFirst()) {
            int idColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID);
            int displayNameColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int numberColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            while (cursor.moveToNext()) {
                Person person = new Person();
                // 获得联系人的ID号
                String contactId = cursor.getString(idColumn);
                // 获得联系人姓名
                person.name = cursor.getString(displayNameColumn);
                person.py = PinyinUtils.getPingYin(person.name);
                person.fisrtspell = PinyinUtils.getFirstSpell(person.name);
                person.number = cursor.getString(numberColumn);
                Log.v("felix", "名字:" + person.name + "号码:" + person.number + "姓名首字母:" + person.fisrtspell);

                this.persons.add(person);
            }
            cursor.close();
        }
    }

    private void filterContacts(String filterStr) {
        ArrayList<Person> filterpersons = new ArrayList<>();

        //遍历所有联系人数组,筛选出包含关键字的联系人
        for (int i = 0; i < persons.size(); i++) {
            //过滤的条件
            if (isStrInString(persons.get(i).number, filterStr)
                    || isStrInString(persons.get(i).py, filterStr)
                    || persons.get(i).name.contains(filterStr)
                    || isStrInString(persons.get(i).fisrtspell, filterStr)) {
                //将筛选出来的联系人重新添加到filterpersons数组中
                Person filterperson = new Person();
                filterperson.name = persons.get(i).name;
                filterperson.py = persons.get(i).py;
                filterperson.number = persons.get(i).number;
                filterperson.fisrtspell = persons.get(i).fisrtspell;
                filterpersons.add(filterperson);
            }
        }

        //如果没有匹配的联系人
        if (filterpersons.isEmpty()) {
            mContactslist.setEmptyView(mListemptytext);
        }

        //将列表更新为过滤的联系人
        mListadapter.updateListView(filterpersons);
    }

    public boolean isStrInString(String bigStr, String smallStr) {
        return bigStr.toUpperCase().contains(smallStr.toUpperCase());
    }

}
