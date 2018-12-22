package huahua.mycontacts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * 自定义联系人的ListView
 *
 * @author Administrator
 */
public class ContactsListView extends ListView {
    /**
     * 选中的是哪个联系人的索引
     */
    int mChooseposition;

    public ContactsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 记录按下的是哪个联系人
            int x = (int) ev.getX();
            int y = (int) ev.getY();
            mChooseposition = pointToPosition(x, y);
        }
        return super.dispatchTouchEvent(ev);
    }

}
