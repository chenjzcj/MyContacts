package huahua.mycontacts;

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
 * Created by Felix.Zhong on 2018/12/22 15:20
 */
public class ListAdapter extends BaseAdapter {
    private ArrayList<Persons> persons;
    private Context context;

    ListAdapter(Context context, ArrayList<Persons> persons) {
        this.persons = persons;
        this.context = context;
    }

    /**
     * 当联系人列表数据发生变化时,用此方法来更新列表
     */
    public void updateListView(ArrayList<Persons> persons) {
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
            convertView = View.inflate(context, R.layout.vp_item, null);
        }
        List<View> views = new ArrayList<>();
        //将要添加到Viewpager中的3个View
        @SuppressLint("InflateParams") View view1 = LayoutInflater.from(context).inflate(R.layout.fragment1, null);
        @SuppressLint("InflateParams") View view2 = LayoutInflater.from(context).inflate(R.layout.list_item, null);
        @SuppressLint("InflateParams") View view3 = LayoutInflater.from(context).inflate(R.layout.fragment3, null);
        views.add(view1);
        views.add(view2);
        views.add(view3);

        Persons persons = this.persons.get(position);

        TextView name = view2.findViewById(R.id.contacts_name);
        final TextView number = view2.findViewById(R.id.contacts_number);

        name.setText(persons.name);
        number.setText(persons.number);

        //每个listview的item都是一个Viewpager
        ItemsViewPager vp = convertView.findViewById(R.id.tabcontent_vp);
        //给viewpager设置适配器
        vp.setAdapter(new TabAdapter(views));
        //给viewpager设置滑动页监听器
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onPageSelected(int position) {
                Intent intent = null;
                String number ="110";// ListAdapter.this.persons.get(mContactslist.mChooseposition).number;
                switch (position) {
                    //打电话
                    case 0:
                        vibrate(context);
                        intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel://" + number));
                        break;
                    //发短信
                    case 2:
                        vibrate(context);
                        intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto://" + number));
                        break;
                    default:
                        break;
                }
                if (intent != null) {
                    context.startActivity(intent);
                }
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }
        });
        //第一次加载都是设置的显示第2个页面
        vp.setCurrentItem(1);

        //字母提示textview的显示
        TextView letterTag = convertView.findViewById(R.id.pb_item_LetterTag);
        //获得当前姓名的拼音首字母
        String firstLetter = persons.py.substring(0, 1).toUpperCase();

        //如果是第1个联系人 那么letterTag始终要显示
        if (position == 0) {
            letterTag.setVisibility(View.VISIBLE);
            letterTag.setText(firstLetter);
        } else {
            //获得上一个姓名的拼音首字母
            String firstLetterPre = this.persons.get(position - 1).py.substring(0, 1).toUpperCase();
            //比较一下两者是否相同
            if (firstLetter.equals(firstLetterPre)) {
                letterTag.setVisibility(View.GONE);
            } else {
                letterTag.setVisibility(View.VISIBLE);
                letterTag.setText(firstLetter);
            }
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
