package com.lopez.espada.falconi.people_list_devspark;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabriel on 30/07/15.
 */
public class PersonListAdapter extends BaseAdapter {

    private final Context context;

    static class PersonViewHolder{
        TextView personText;
    }

    private final List<Person> personList;

    public PersonListAdapter(Context context, List<Person> personList) {
        this.context = context;
        this.personList = personList;
    }

    @Override
    public int getCount() {
        return personList.size();
    }

    @Override
    public Object getItem(int position) {
        return personList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PersonViewHolder holder;
        if (convertView == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.person, parent, false);

            holder = new PersonViewHolder();
            holder.personText = (TextView)convertView.findViewById(R.id.personItemView);
            convertView.setTag(holder);
        }else{
            holder = (PersonViewHolder) convertView.getTag();
        }

        if (getItem(position) != null){
            holder.personText.setText(((Person)getItem(position)).getName());
        }
        return convertView;
    }
}
