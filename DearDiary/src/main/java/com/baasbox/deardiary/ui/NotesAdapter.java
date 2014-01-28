package com.baasbox.deardiary.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.baasbox.android.BaasDocument;

import java.util.List;

/**
 * Created by Andrea Tortorella on 24/01/14.
 */
class NotesAdapter extends BaseAdapter {

    private List<BaasDocument> mDocuments;
    private LayoutInflater mInflater;

    NotesAdapter(Context context,List<BaasDocument> documents){
        this.mDocuments=documents;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (mDocuments==null){
            return 0;
        }
        return mDocuments.size();
    }

    @Override
    public BaasDocument getItem(int position) {
        return mDocuments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder h;
        if (convertView==null){
            convertView = mInflater.inflate(android.R.layout.simple_list_item_2,parent,false);
            h = new ViewHolder();
            h.text1 = (TextView)convertView.findViewById(android.R.id.text1);
            h.text2 = (TextView)convertView.findViewById(android.R.id.text2);
            convertView.setTag(h);
        } else {
            h = (ViewHolder)convertView.getTag();
        }
        BaasDocument document = mDocuments.get(position);
        String title = document.getString("title");
        String creationDate = document.getCreationDate();
        h.text1.setText(title);
        h.text2.setText(creationDate);
        return convertView;
    }

    private static class ViewHolder{
        private TextView text1;
        private TextView text2;
    }
}
