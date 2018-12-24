package com.feilx.mycontacts;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 联系人数据适配器
 *
 * @author Created by Felix.Zhong on 2018/12/22 15:20
 */
public class ContactsAdapter extends BaseAdapter {

    private List<Person> persons;
    private Context context;
    private ContactsListView mContactslist;

    ContactsAdapter(Context context, List<Person> persons, ContactsListView contactsListView) {
        this.context = context;
        this.persons = persons;
        this.mContactslist = contactsListView;
    }

    /**
     * 当联系人列表数据发生变化时,用此方法来更新列表
     */
    public void updateListView(List<Person> persons) {
        this.persons = persons;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return persons.size();
    }

    @Override
    public Object getItem(int position) {
        return persons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_contacts, null);
        }
        List<View> views = new ArrayList<>();
        //将要添加到Viewpager中的3个View
        @SuppressLint("InflateParams") View itemViewCall = LayoutInflater.from(context).inflate(R.layout.item_view_call, null);
        @SuppressLint("InflateParams") View itemViewMain = LayoutInflater.from(context).inflate(R.layout.item_view_main, null);
        @SuppressLint("InflateParams") View itemViewSms = LayoutInflater.from(context).inflate(R.layout.item_view_sms, null);
        views.add(itemViewCall);
        views.add(itemViewMain);
        views.add(itemViewSms);

        Person person = this.persons.get(position);

        TextView name = itemViewMain.findViewById(R.id.tv_contacts_name);
        TextView number = itemViewMain.findViewById(R.id.tv_contacts_number);
        name.setText(person.getName());
        number.setText(person.getNumber());

        //每个listview的item都是一个Viewpager
        final ItemsViewPager vp = convertView.findViewById(R.id.item_viewpager);
        //给viewpager设置适配器
        vp.setAdapter(new TabAdapter(views));
        //给viewpager设置滑动页监听器
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Intent intent = null;
                mContactslist.mChooseposition = (mContactslist.mChooseposition >= persons.size() && persons.size() > 0) ? persons.size() - 1 : mContactslist.mChooseposition;
                String number = ContactsAdapter.this.persons.get(mContactslist.mChooseposition).getNumber();
                switch (position) {
                    //打电话
                    case 0:
                        vibrate(context);
                        intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
                        break;
                    //发短信
                    case 2:
                        vibrate(context);
                        intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + number));
                        break;
                    default:
                        break;
                }
                if (intent != null) {
                    context.startActivity(intent);
                    //复原
                    vp.setCurrentItem(1);
                }
            }

            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }

        });
        //第一次加载都是设置的显示第2个页面
        vp.setCurrentItem(1);

        //字母提示textview的显示
        TextView tvItemLetterTag = convertView.findViewById(R.id.tv_item_letter_tag);
        //获得当前姓名的拼音首字母
        String firstLetter = person.py.substring(0, 1).toUpperCase();

        //如果是第1个联系人 那么letterTag始终要显示
        tvItemLetterTag.setText(firstLetter);
        if (position == 0) {
            tvItemLetterTag.setVisibility(View.VISIBLE);
        } else {
            //获得上一个姓名的拼音首字母
            String firstLetterPre = this.persons.get(position - 1).getPy().substring(0, 1).toUpperCase();
            //比较一下两者是否相同
            tvItemLetterTag.setVisibility(firstLetter.equals(firstLetterPre) ? View.GONE : View.VISIBLE);
        }
        return convertView;
    }

    /**
     * 震动一下
     *
     * @param context Context
     */
    private void vibrate(Context context) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        if (vib != null) {
            vib.vibrate(50);
        }
    }
}
