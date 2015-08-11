package com.lopez.espada.falconi.people_list_devspark;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabriel on 30/07/15.
 */
public class PersonListAdapter extends BaseAdapter implements Filterable {

    private final Context context;

    static class PersonViewHolder{
        TextView personText;
    }

    private List<Person> personList;
    private List<Person> allPersonList;

    public PersonListAdapter(Context context, List<Person> personList) {
        this.context = context;
        this.allPersonList = personList;
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

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                personList = (List<Person>) results.values;
                Log.d("Filter","publishing result");
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence search) {

                FilterResults results = new FilterResults();
                ArrayList<Person> matching = new ArrayList<Person>();

                String matcher = search.toString().toLowerCase();
                Log.d("Filter","filtering "+allPersonList.size()+" people for "+matcher);
                for (Person p : allPersonList) {
                    if (p.getName().toLowerCase().contains(matcher))  {
                        matching.add(p);
                    }
                }
                Log.d("Filter","got matching "+matching.size());
                results.count = matching.size();
                results.values = matching;
                return results;
            }
        };

        return filter;
    }
}
