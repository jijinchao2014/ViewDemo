package com.jijc.viewdemo.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.github.promeg.pinyinhelper.Pinyin;
import com.jijc.viewdemo.bean.ContactsBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Administrator on 2017/5/27.
 */

public class ContactsUtil {
    private static String indexStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";


    public static ArrayList<ContactsBean> getContactData(Context context, ArrayList<ContactsBean> searchContactLists) {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;//ContactsContract.Contacts.CONTENT_URI
        //得到ContentResolver对象
        ContentResolver cr = context.getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(uri, null, null, null, "sort_key");
        //向下移动光标
        while (cursor.moveToNext()) {
            //取得联系人名字
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            String name = cursor.getString(nameFieldColumnIndex);
            //取得联系人ID
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//            //要获取所有的联系人,一个联系人会有多个号码
//            getContactById(cr, contactId, name, searchContactLists);
            ContactsBean contact = new ContactsBean();
            contact.name = name;
            contact.contactId = contactId;
//            String regEx = "[`~!@#$%^&*()\\-+={}':;,\\[\\].<>/?￥%…（）_+|【】‘；：”“’。，、？\\s]";
//            Pattern p = Pattern.compile(regEx);
//            Matcher m = p.matcher(number);
//            number = m.replaceAll("").trim();
            number = number.replace("+86","").replace(" ","").trim();
            if (!TextUtils.isEmpty(contact.name)) {
                getPinyinList(contact);
            } else {
                contact.pinyinFirst = "#";
            }

            contact.number = number;

            if (RegexChk.isMobileExact(number)){
                searchContactLists.add(contact);
            }
        }
//        for (ContactsBean contactsBean: searchContactLists) {
//
//        }
        Collections.sort(searchContactLists, new SortByPinyin());//数据排序
        return searchContactLists;
    }

    private static void getContactById(ContentResolver cr, String contactId, String name, ArrayList<ContactsBean> searchContactLists) {
        if (!TextUtils.isEmpty(contactId)) {
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
            if (null != phone) {
                ContactsBean contact = new ContactsBean();
                contact.name = name;
                contact.contactId = contactId;

                if (!TextUtils.isEmpty(contact.name)) {
                    getPinyinList(contact);
                } else {
                    contact.pinyinFirst = "#";
                }
                while (phone.moveToNext()) {
                    String phoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contact.numberList.add(phoneNumber);
                }
                searchContactLists.add(contact);
            }
        }
    }

    private static void getPinyinList(ContactsBean contactsBean) {
        StringBuffer bufferNamePiny = new StringBuffer();//NIHAO
        StringBuffer bufferNameMatch = new StringBuffer();//NH
        String name = contactsBean.name;
        for (int i = 0; i < name.length(); i++) {
            StringBuffer bufferNamePer = new StringBuffer();
            String namePer = name.charAt(i) + "";//名字的每个字
            for (int j = 0; j < namePer.length(); j++) {
                char character = namePer.charAt(j);
                String pinCh = Pinyin.toPinyin(character).toUpperCase();
                bufferNamePer.append(pinCh);
                bufferNameMatch.append(pinCh.charAt(0));
                bufferNamePiny.append(pinCh);
            }
            contactsBean.namePinyinList.add(bufferNamePer.toString());//单个名字集合
        }
        contactsBean.namePinYin = bufferNamePiny.toString();
        contactsBean.matchPin = bufferNameMatch.toString();
        String firstPinyin = contactsBean.namePinYin.charAt(0) + "";
        if (indexStr.contains(firstPinyin)) {
            contactsBean.pinyinFirst = firstPinyin;
        } else {
            contactsBean.pinyinFirst = "#";
        }
    }
    public static String transformPinYin(String character) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < character.length(); i++) {
            buffer.append(Pinyin.toPinyin(character.charAt(i)).toUpperCase());
        }
        return buffer.toString();
    }
    /**
     * 按照名字分类方便索引
     */
    static class SortByPinyin implements Comparator {
        public int compare(Object o1, Object o2) {
            ContactsBean s1 = (ContactsBean) o1;
            ContactsBean s2 = (ContactsBean) o2;
            if (s1.pinyinFirst.equals("#")) {
                return 1;
            } else if (s2.pinyinFirst.equals("#")) {
                return -1;
            }
            return s1.pinyinFirst.compareTo(s2.pinyinFirst);
        }
    }

}
