package com.jijc.viewdemo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jijc.viewdemo.R;
import com.jijc.viewdemo.bean.ContactsBean;
import com.jijc.viewdemo.utils.ListUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/26.
 */

public class PhoneContactAdapter extends RecyclerView.Adapter<PhoneContactAdapter.ViewHolder> implements StickyHeaderAdapter<PhoneContactAdapter.HeaderHolder> {


    private final ArrayList<ContactsBean> contactLists;
    private final ForegroundColorSpan blueSpan;
    private Context context;
    private final LayoutInflater mInflater;
    private char lastChar = '\u0000';
    private int DisplayIndex = 0;
    private boolean isHeaderShow = true;
    private boolean isSelectShow = false;
    SpannableStringBuilder textBuild = new SpannableStringBuilder();

    public PhoneContactAdapter(Context context, ArrayList<ContactsBean> contactLists) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.contactLists = contactLists;
        blueSpan = new ForegroundColorSpan(Color.parseColor("#28d19d"));
    }

    public PhoneContactAdapter(Context context, ArrayList<ContactsBean> contactLists,boolean selectShow) {
        this(context,contactLists);
        isSelectShow = selectShow;
    }

    public ArrayList<ContactsBean> getData() {
        return contactLists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.item_phone_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setClickPosition(position);
        ContactsBean contactsBean = contactLists.get(position);
        if (contactsBean != null){
            if (contactsBean.matchType == 1) {//高亮名字
                textBuild.clear();
                textBuild.append(contactsBean.name);
                textBuild.setSpan(blueSpan, contactsBean.highlightedStart, contactsBean.highlightedEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.tvNumber.setText(contactsBean.number);
                holder.nickName.setText(textBuild);
            } else if (contactsBean.matchType == 2) {//高亮号码
                textBuild.clear();
                textBuild.append(contactsBean.number);
                textBuild.setSpan(blueSpan, contactsBean.highlightedStart, contactsBean.highlightedEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.tvNumber.setText(textBuild);
                holder.nickName.setText(contactsBean.name);
            } else {//不高亮
                holder.tvNumber.setText(contactsBean.number);
                holder.nickName.setText(contactsBean.name);
            }

            if (isSelectShow){
                holder.cb_contact.setVisibility(View.VISIBLE);
                holder.cb_contact.setChecked(contactsBean.isSelected);
            }else {
                holder.cb_contact.setVisibility(View.GONE);
            }
            if (isHeaderShow){
                if (position == 0) {
                    holder.diviView.setVisibility(View.INVISIBLE);
                } else {
                    ContactsBean currentItem = contactLists.get(position);
                    ContactsBean lastItem = contactLists.get(position - 1);
                    if (!currentItem.pinyinFirst.equals(lastItem.pinyinFirst)) {
                        holder.diviView.setVisibility(View.INVISIBLE);
                    } else {
                        holder.diviView.setVisibility(View.VISIBLE);
                    }
                }
            }else {
                holder.diviView.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return contactLists.size();
    }

    @Override
    public void setHeaderShow(boolean isHeaderShow){
        this.isHeaderShow = isHeaderShow;
        notifyDataSetChanged();
    }

    @Override
    public boolean isHeaderShow(){
        return isHeaderShow;
    }

    //=================悬浮栏=================
    @Override
    public long getHeaderId(int position) {
        if (!isHeaderShow){
            return DisplayIndex;
        }
        //这里面的是如果当前position与之前position重复（内部判断）  则不显示悬浮标题栏  如果不一样则显示标题栏
        if (null != contactLists.get(position) && contactLists.get(position).pinyinFirst.charAt(0) != '\0') {
            char ch = contactLists.get(position).pinyinFirst.charAt(0);
            if (lastChar == '\u0000') {
                lastChar = ch;
                return DisplayIndex;
            } else {
                if (lastChar == ch) {
                    return DisplayIndex;
                } else {
                    lastChar = ch;
                    DisplayIndex++;
                    return DisplayIndex;
                }

            }
        } else {
            return DisplayIndex;
        }
    }

    @Override
    public HeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
        final View view = mInflater.inflate(R.layout.item_contacts_head, parent, false);
        return new HeaderHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderHolder viewholder, int position) {
        if (contactLists.get(position).pinyinFirst.charAt(0) == '\0') {
            viewholder.header.setText("#");
        } else {
            viewholder.header.setText(contactLists.get(position).pinyinFirst);
        }
    }

    public int getPositionForSection(char pinyinFirst) {
        for (int i = 0; i < getItemCount(); i++) {
            char firstChar = contactLists.get(i).pinyinFirst.charAt(0);
            if (firstChar == pinyinFirst) {
                return i;
            }
        }
        return -1;

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView nickName;
        private final TextView tvNumber;
        private final CheckBox cb_contact;
        private final View diviView;
        private int mPosition;

        public ViewHolder(View itemView) {
            super(itemView);
            nickName = (TextView) itemView.findViewById(R.id.tv_name);
            tvNumber = (TextView) itemView.findViewById(R.id.tv_number);
            cb_contact = (CheckBox) itemView.findViewById(R.id.cb_contact);
            diviView = itemView.findViewById(R.id.vw_divisition);
            itemView.setOnClickListener(this);
        }

        public void setClickPosition(int position){
            this.mPosition = position;
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null){
                mOnItemClickListener.onItemClick(v,mPosition);
            }
        }
    }

    public class HeaderHolder extends RecyclerView.ViewHolder {
        public TextView header;

        public HeaderHolder(View itemView) {
            super(itemView);
            header = (TextView) itemView;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(View v,int position);
    }
}
