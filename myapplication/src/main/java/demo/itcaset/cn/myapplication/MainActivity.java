package demo.itcaset.cn.myapplication;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import demo.itcaset.cn.myapplication.bean.Person;
import demo.itcaset.cn.myapplication.tolls.Cheeses;
import demo.itcaset.cn.myapplication.tolls.Utils;
import demo.itcaset.cn.myapplication.ui.QuickIndexBar;
public class MainActivity extends AppCompatActivity {

    private ArrayList<Person> mPersons;
    private ListView mLv;
    private TextView mBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intView();
    }

    private void intView() {
        QuickIndexBar bar  = (QuickIndexBar) findViewById(R.id.bar);
        mLv = (ListView) findViewById(R.id.lv);
        mBg = (TextView) findViewById(R.id.bg);
        MyAdapter myAdapter = new MyAdapter();
        mPersons = new ArrayList<>();
        for (int i = 0; i < Cheeses.NAMES.length; i++) {
            Person person = new Person(Cheeses.NAMES[i]);
            mPersons.add(person);
        }
        //排序集合
        Collections.sort(mPersons);
        mLv.setAdapter(myAdapter);
        bar.setLetterUpdateListener(new QuickIndexBar.OnLetterUpdateListener() {
            @Override
            public void onLetterUpdate(String letter) {
                for (int i = 0; i <mPersons.size() ; i++) {
                    Person person = mPersons.get(i);
                    String pinyin = person.getPinyin().charAt(0)+"";
                    if (TextUtils.equals(letter,pinyin)){
                        mLv.setSelection(i);
                        showtext(letter);
                        break;
                    }
                }
                Utils.showToast(MainActivity.this,letter);
            }
        });
    }

    /**
     * 点击显示隐藏大吐司
     */
    private Handler h=new Handler();
    private void showtext(String letter) {
        h.removeCallbacks(null);
        mBg.setVisibility(View.VISIBLE);
        mBg.setText(letter);
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                mBg.setVisibility(View.GONE);
            }
        },2000);
    }

    private class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mPersons.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            View view = convertView;
            if(convertView == null){
                view = view.inflate(getApplicationContext(), R.layout.item_list, null);
            }
            ViewHolder mViewHolder = ViewHolder.getHolder(view);

            Person p = mPersons.get(i);

            String str = null;
            String currentLetter = p.getPinyin().charAt(0) + "";
            if(i == 0){
                str = currentLetter;
            }else {
                // 上一个人的拼音的首字母
                String preLetter = mPersons.get(i - 1).getPinyin().charAt(0) + "";
                if(!TextUtils.equals(preLetter, currentLetter)){
                    str = currentLetter;
                }
            }

            // 根据str是否为空,决定是否显示索引栏
            mViewHolder.mIndex.setVisibility(str == null ? View.GONE : View.VISIBLE);
            mViewHolder.mIndex.setText(currentLetter);
            mViewHolder.mName.setText(p.getName());

            return view;
        }
    }
    private  static class ViewHolder {
        TextView mIndex;
        TextView mName;

        public static ViewHolder getHolder(View view) {
            Object tag = view.getTag();
            if(tag != null){
                return (ViewHolder)tag;
            }else {
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.mIndex = (TextView) view.findViewById(R.id.tv_index);
                viewHolder.mName = (TextView) view.findViewById(R.id.tv_name);
                view.setTag(viewHolder);
                return viewHolder;
            }
        }

    }
}
