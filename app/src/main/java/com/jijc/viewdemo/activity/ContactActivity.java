package com.jijc.viewdemo.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jijc.viewdemo.adapter.PhoneContactAdapter;
import com.jijc.viewdemo.bean.ContactsBean;
import com.jijc.viewdemo.R;
import com.jijc.viewdemo.utils.RegexChk;
import com.jijc.viewdemo.utils.ContactsUtil;
import com.jijc.viewdemo.view.QuickIndexBar;
import com.jijc.viewdemo.view.StickyHeaderDecoration;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ContactActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String KEY_SELECT_TYPE = "KEY_SELECT_TYPE";
    public static final String KEY_FINISH_SELECT = "KEY_FINISH_SELECT";
    public static final int SINGLE_SELECTION = 0;
    public static final int MULTIPLE_SELECTION = 1;

    private EditText mEtSearch;
    private QuickIndexBar quickIndexBar;
    private RecyclerView rvContacts;
    private LinearLayout ll_result;
    ArrayList<ContactsBean> contactLists = new ArrayList<>();
    ArrayList<ContactsBean> searchContactLists = new ArrayList<>();//用于搜索的集合数据
    private PhoneContactAdapter contactAdapter;
    private LinearLayoutManager manager;
    private StickyHeaderDecoration decoration;
    private int mSelectType;
    private TextView tv_number;
    private TextView tv_ok;
    private int selectNum = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Intent intent = getIntent();
        mSelectType = intent.getIntExtra(KEY_SELECT_TYPE, SINGLE_SELECTION);
        initView();
        getContactsByPerm();
    }

    private void initView() {
        //搜索框
        mEtSearch = (EditText) findViewById(R.id.mEtSearch);
        //索引
        quickIndexBar = (QuickIndexBar) findViewById(R.id.qiBar);
        //数据列表
        rvContacts = (RecyclerView) findViewById(R.id.rvContacts);
        ll_result = (LinearLayout) findViewById(R.id.ll_result);
        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
        manager = new LinearLayoutManager(this);
        rvContacts.setLayoutManager(manager);
        rvContacts.setHasFixedSize(true);
        if (mSelectType == MULTIPLE_SELECTION){
            contactAdapter = new PhoneContactAdapter(this, contactLists,true);
            ll_result.setVisibility(View.VISIBLE);
        }else {
            contactAdapter = new PhoneContactAdapter(this, contactLists);
            ll_result.setVisibility(View.GONE);
        }

        //设置悬浮索引
        decoration = new StickyHeaderDecoration(contactAdapter);
        rvContacts.setAdapter(contactAdapter);
        rvContacts.addItemDecoration(decoration);

        tv_ok.setOnClickListener(this);

        contactAdapter.setOnItemClickListener(new PhoneContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                ContactsBean contactsBean = contactAdapter.getData().get(position);
                if (mSelectType == MULTIPLE_SELECTION){
                    contactsBean.isSelected = !contactsBean.isSelected;
                    contactAdapter.notifyDataSetChanged();
                    if (contactsBean.isSelected){
                        selectNum++;
                    }else {
                        selectNum--;
                    }

                    tv_number.setText("已选择"+selectNum+"人");
                }else {
                    ArrayList<ContactsBean> selected = new ArrayList<>();
                    selected.add(contactsBean);
                    Intent intent = new Intent();
                    intent.putExtra(KEY_FINISH_SELECT,selected);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }
            }
        });

        //索引监听
        quickIndexBar.setOnLetterChangeListener(new QuickIndexBar.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

                for (int i = 0; i < contactLists.size(); i++) {

                    if (letter.equals(contactLists.get(i).pinyinFirst + "")) {

                        int position = contactAdapter.getPositionForSection(contactLists.get(i).pinyinFirst.charAt(0));
                        if (position != -1) {
                            //滑动到指定位置
                            manager.scrollToPositionWithOffset(position, 0);
                        }
                        break;
                    }
                }
            }

            @Override
            public void onReset() {

            }
        });
        //触摸隐藏键盘
        rvContacts.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                return false;
            }
        });
        //搜索功能
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                contactAdapter.setHeaderShow(TextUtils.isEmpty(s.toString()));
                //查询数据
                filtDatas(s);
            }
        });
    }

    private void getContactsByPerm() {
        //先获取手机和sim卡联系人
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        } else {
            getContacts(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getContacts(this);
            } else {
                Toast.makeText(this, "权限拒绝", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getContacts(final Context context) {
        contactLists.clear();
        searchContactLists.clear();
        Observable.create(new ObservableOnSubscribe<ArrayList<ContactsBean>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<ContactsBean>> e) throws Exception {
                if (!e.isDisposed()) {
                    e.onNext(ContactsUtil.getContactData(context, searchContactLists));
                    e.onComplete();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<ContactsBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<ContactsBean> contactsBeen) {
                        contactLists.addAll(contactsBeen);
                        contactAdapter.notifyDataSetChanged();
                    }


                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("");
                    }
                });

    }

    //搜索数据
    private void filtDatas(Editable s) {
        if (searchContactLists.size() == 0) {
            return;
        }
        String inputStr = s.toString();
        if (TextUtils.isEmpty(inputStr)) {
            resetSearchData();
            contactLists.clear();
            contactLists.addAll(searchContactLists);
            contactAdapter.notifyDataSetChanged();
        } else {
            contactLists.clear();
            //因为每次搜索的结果不同，所以匹配类型不同，但是数据源都是同一个数据源，所以每次搜索前，要重置数据
            resetSearchData();
            if (RegexChk.isNumeric(inputStr)) {//如果是数字
                findDataByNumberOrCN(inputStr);
            } else if (RegexChk.isContainChinese(inputStr)) {//如果含义中文，需要精确匹配
                findDataByNumberOrCN(inputStr);
            } else if (RegexChk.isEnglishAlphabet(inputStr)) {//是不是全是英文字母或者是拼音的话
                findDataByEN(inputStr);
            } else {//需要精确匹配
                findDataByNumberOrCN(inputStr);
            }
            contactAdapter.notifyDataSetChanged();
        }
    }

    private void resetSearchData() {
        for (int i = 0; i < searchContactLists.size(); i++) {
            searchContactLists.get(i).matchType = 0;//重置为没有匹配类型
        }
    }

    private void findDataByNumberOrCN(String inputStr) {
        for (int i = 0; i < searchContactLists.size(); i++) {
            ContactsBean contactsBean = searchContactLists.get(i);
            if (!TextUtils.isEmpty(contactsBean.name) && contactsBean.name.contains(inputStr)) {
                contactsBean.matchType = 1;//名字匹配
                contactsBean.highlightedStart = contactsBean.name.indexOf(inputStr);
                contactsBean.highlightedEnd = contactsBean.highlightedStart + inputStr.length();
                contactLists.add(contactsBean);
                continue;
            }

            if (!TextUtils.isEmpty(contactsBean.number) && contactsBean.number.contains(inputStr)){
//                contactsBean.setShowNumberIndex(j);//显示号码的下标
                contactsBean.matchType = 2;//电话匹配
                contactsBean.highlightedStart = contactsBean.number.indexOf(inputStr);
                contactsBean.highlightedEnd = contactsBean.highlightedStart + inputStr.length();
                contactLists.add(contactsBean);
            }
//            if (contactsBean.getNumberList().size() > 0) {
//                for (int j = 0; j < contactsBean.getNumberList().size(); j++) {
//                    String number = contactsBean.getNumberList().get(j);
//                    if (!TextUtils.isEmpty(number) && number.contains(inputStr)) {
//                        contactsBean.setShowNumberIndex(j);//显示号码的下标
//                        contactsBean.setMatchType(2);//电话匹配
//                        contactsBean.setHighlightedStart(number.indexOf(inputStr));
//                        contactsBean.setHighlightedEnd(contactsBean.getHighlightedStart() + inputStr.length());
//                        contactLists.add(contactsBean);
//                    }
//                }
//            }

        }
    }

    //通过拼音或者英文字母
    private void findDataByEN(String inputStr) {
        //把输入的内容变为大写
        String searPinyin = ContactsUtil.transformPinYin(inputStr);
        //搜索字符串的长度
        int searLength = searPinyin.length();
        //搜索的第一个大写字母
        String searPinyinFirst = searPinyin.charAt(0) + "";
        for (int i = 0; i < searchContactLists.size(); i++) {
            ContactsBean contactsBean = searchContactLists.get(i);
            contactsBean.matchType = 1;//字母匹配肯定是名字
            //如果输入的每一个字母都和名字的首字母一样，那就可以匹配比如：你好，NH，输入nh就ok
            if (contactsBean.matchPin.contains(searPinyin)) {
                contactsBean.highlightedStart = contactsBean.matchPin.indexOf(searPinyin);
                contactsBean.highlightedEnd = contactsBean.highlightedStart + searLength;
                contactLists.add(contactsBean);
            } else {
                boolean isMatch = false;
                //先去匹配单个字，比如你好：NI,HAO.输入NI，肯定匹配第一个
                for (int j = 0; j < contactsBean.namePinyinList.size(); j++) {
                    String namePinyinPer = contactsBean.namePinyinList.get(j);
                    if (!TextUtils.isEmpty(namePinyinPer) && namePinyinPer.startsWith(searPinyin)) {
                        //符合的话就是当前字匹配成功
                        contactsBean.highlightedStart = j;
                        contactsBean.highlightedEnd = j + 1;
                        contactLists.add(contactsBean);
                        isMatch = true;
                        break;
                    }
                }
                if (isMatch) {
                    continue;
                }
                //根据拼音包含来实现，比如你好：NIHAO,输入NIHA或者NIHAO。
                if (!TextUtils.isEmpty(contactsBean.namePinYin) && contactsBean.namePinYin.contains(searPinyin)) {
                    //这样的话就要从每个字的拼音开始匹配起
                    for (int j = 0; j < contactsBean.namePinyinList.size(); j++) {
                        StringBuilder sbMatch = new StringBuilder();
                        for (int k = j; k < contactsBean.namePinyinList.size(); k++) {
                            sbMatch.append(contactsBean.namePinyinList.get(k));
                        }
                        if (sbMatch.toString().startsWith(searPinyin)) {
                            //匹配成功
                            contactsBean.highlightedStart = j;
                            int length = 0;
                            //比如输入是NIH，或者NIHA,或者NIHAO,这些都可以匹配上，从而就可以通过NIHAO>=NIH,HIHA,NIHAO
                            for (int k = j; k < contactsBean.namePinyinList.size(); k++) {
                                length = length + contactsBean.namePinyinList.get(k).length();
                                if (length >= searLength) {
                                    contactsBean.highlightedEnd = k + 1;
                                    break;
                                }
                            }
                            isMatch = true;
                            contactLists.add(contactsBean);
                        }
                    }
                }

                if (isMatch) {
                    continue;
                }

                //最后一种情况比如：广发银行，输入GuangFY或者GuangFYH都可以匹配成功，这样的情况名字集合必须大于等于3
                if (contactsBean.namePinyinList.size() > 2) {
                    for (int j = 0; j < contactsBean.namePinyinList.size(); j++) {

                        StringBuilder sbMatch0 = new StringBuilder();
                        sbMatch0.append(contactsBean.namePinyinList.get(j));
                        //只匹配到倒数第二个
                        if (j < contactsBean.namePinyinList.size() - 2) {
                            for (int k = j + 1; k < contactsBean.matchPin.length(); k++) {
                                //依次添加后面每个字的首字母
                                sbMatch0.append(contactsBean.matchPin.charAt(k));
                                if (sbMatch0.toString().equals(searPinyin)) {
                                    contactsBean.highlightedStart = j;
                                    contactsBean.highlightedEnd = k + 1;
                                    contactLists.add(contactsBean);
                                    isMatch = true;
                                    break;
                                }
                            }
                        }

                        if (isMatch) {
                            //跳出循环已找到
                            break;
                        }

                        //sbMatch1是循环匹配对象比如GUANGFYH，GUANGFAYH，GUANGFAYINH,GUANGFAYINHANG，
                        //FAYH,YINH
                        StringBuilder sbMatch1 = new StringBuilder();
                        for (int k = 0; k <= j; k++) {//依次作为初始匹配的起点
                            sbMatch1.append(contactsBean.namePinyinList.get(k));
                        }
                        //只匹配到倒数第二个
                        if (j < contactsBean.namePinyinList.size() - 2) {
                            for (int k = j + 1; k < contactsBean.matchPin.length(); k++) {
                                //依次添加后面每个字的首字母
                                sbMatch1.append(contactsBean.matchPin.charAt(k));
                                if (sbMatch1.toString().equals(searPinyin)) {
                                    contactsBean.highlightedStart = j;
                                    contactsBean.highlightedEnd = k + 1;
                                    contactLists.add(contactsBean);
                                    isMatch = true;
                                    break;
                                }
                            }
                        }
                        if (isMatch) {
                            //跳出循环已找到
                            break;
                        }

                        if (j >= contactsBean.namePinyinList.size() - 2) {
                            //如果说是剩余最后两个拼音不需要匹配了
                            break;
                        }
                        StringBuilder sbMatch2 = new StringBuilder();
                        sbMatch2.append(contactsBean.namePinyinList.get(j));
                        for (int k = j + 1; k < contactsBean.namePinyinList.size(); k++) {
                            sbMatch2.append(contactsBean.namePinyinList.get(k));
                            StringBuilder sbMatch3 = new StringBuilder();
                            sbMatch3.append(sbMatch2.toString());
                            //只匹配到倒数第二个
                            if (j < contactsBean.namePinyinList.size() - 2) {
                                for (int m = k + 1; m < contactsBean.matchPin.length(); m++) {
                                    //依次添加后面每个字的首字母
                                    sbMatch3.append(contactsBean.matchPin.charAt(m));
                                    if (sbMatch3.toString().equals(searPinyin)) {
                                        contactsBean.highlightedStart = j;
                                        contactsBean.highlightedEnd = m + 1;
                                        contactLists.add(contactsBean);
                                        isMatch = true;
                                        break;
                                    }
                                }
                            }
                            if (isMatch) {
                                //跳出循环已找到
                                break;
                            }
                        }

                        if (isMatch) {
                            //跳出循环已找到
                            break;
                        }


                    }
                }

            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.tv_ok:
                ArrayList<ContactsBean> selected = new ArrayList<>();
//                ArrayList<ContactsBean> data = contactAdapter.getData();
                for (ContactsBean contactsBean : searchContactLists){
                    if (contactsBean.isSelected){
                        selected.add(contactsBean);
                    }
                }
                Intent intent = new Intent();
                intent.putExtra(KEY_FINISH_SELECT,selected);
                setResult(Activity.RESULT_OK,intent);
                finish();
                break;
        }
    }
}
