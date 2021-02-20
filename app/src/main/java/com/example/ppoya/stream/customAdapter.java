package com.example.ppoya.stream;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import javaPackage.Item;

public class customAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<Item> listArray;
    private int layout;

    public customAdapter(Context context, int layout, ArrayList<Item> listArray)
    {
        this.layout=layout;
        this.listArray=listArray;
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public String getSong(int position)
    {
        return listArray.get(position).getSong();
    }

    @Override
    public int getCount(){return listArray.size();}

    @Override
    public Object getItem(int position){return listArray.get(position);}

    @Override
    public long getItemId(int position){return (long)listArray.get(position).getIcon();}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }
        Item listviewitem = listArray.get(position);
        ImageView icon = (ImageView) convertView.findViewById(R.id.imgSong);
        icon.setImageResource(listviewitem.getIcon());
        TextView name = (TextView) convertView.findViewById(R.id.txSong);
        name.setText(listviewitem.getSong());
       // int topPadding=(name.getMaxHeight()-name.getHeight())/2;
       // name.setPadding(0,topPadding,0,0);

        TextView singer = (TextView) convertView.findViewById(R.id.txSinger);
        singer.setText(listviewitem.getSinger());
        return convertView;
    }

    @Override
    public int getItemViewType(int i) {
        return listArray.get(i).getType();
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }


}
