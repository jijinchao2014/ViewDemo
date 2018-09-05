package com.jijc.viewdemo.bean;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class ContactsBean implements Serializable,Comparable<ContactsBean> {
    public String contactId;//通讯录ID
    public String name; //名字
    public String number;//电话号码
    public boolean isSelected = false;//用于多选
    public String pinyinFirst;//拼音首字母用于悬浮栏
    public int showNumberIndex = 0;//因为一个人可能会有多个号码
    public int highlightedStart = 0;//需要高亮的开始下标
    public int highlightedEnd = 0;//需要高亮的结束下标
    public String matchPin = "";//用来匹配的拼音每个字的首字母比如：你好，NH
    public String namePinYin = "";//全名字拼音,比如：你好,NIHAO
    public int matchType = 0;//匹配类型，名字1，电话号码2，其他0,根据输入的来判断
    public ArrayList<String> namePinyinList = new ArrayList<>();//名字拼音集合，比如你好，NI,HAO
    public ArrayList<String> numberList = new ArrayList<>();//电话号码集合，一个人可能会有多个号码
    public int matchIndex = 0;//匹配到号码后的下标

//    public int getMatchIndex() {
//        return matchIndex;
//    }
//
//    public void setMatchIndex(int matchIndex) {
//        this.matchIndex = matchIndex;
//    }
//
//    public String getPinyinFirst() {
//        return pinyinFirst;
//    }
//
//    public void setPinyinFirst(String pinyinFirst) {
//        this.pinyinFirst = pinyinFirst;
//    }
//
//    public String getContactId() {
//        return contactId;
//    }
//
//    public void setContactId(String contactId) {
//        this.contactId = contactId;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getNumber() {
//        return number;
//    }
//
//    public void setNumber(String number) {
//        this.number = number;
//    }
//
//    public int getShowNumberIndex() {
//        return showNumberIndex;
//    }
//
//    public void setShowNumberIndex(int showNumberIndex) {
//        this.showNumberIndex = showNumberIndex;
//    }
//
//    public int getHighlightedStart() {
//        return highlightedStart;
//    }
//
//    public void setHighlightedStart(int highlightedStart) {
//        this.highlightedStart = highlightedStart;
//    }
//
//    public int getHighlightedEnd() {
//        return highlightedEnd;
//    }
//
//    public void setHighlightedEnd(int highlightedEnd) {
//        this.highlightedEnd = highlightedEnd;
//    }
//
//    public String getMatchPin() {
//        return matchPin;
//    }
//
//    public void setMatchPin(String matchPin) {
//        this.matchPin = matchPin;
//    }
//
//    public String getNamePinYin() {
//        return namePinYin;
//    }
//
//    public void setNamePinYin(String namePinYin) {
//        this.namePinYin = namePinYin;
//    }
//
//    public int getMatchType() {
//        return matchType;
//    }
//
//    public void setMatchType(int matchType) {
//        this.matchType = matchType;
//    }
//
//    public ArrayList<String> getNamePinyinList() {
//        return namePinyinList;
//    }
//
//    public void setNamePinyinList(ArrayList<String> namePinyinList) {
//        this.namePinyinList = namePinyinList;
//    }
//
//    public ArrayList<String> getNumberList() {
//        return numberList;
//    }
//
//    public void setNumberList(ArrayList<String> numberList) {
//        this.numberList = numberList;
//    }


    @Override
    public String toString() {
        return "ContactsBean{" +
                "contactId='" + contactId + '\'' +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }

    @Override
    public int compareTo(@NonNull ContactsBean contactsBean) {
        return 0;
    }
}
