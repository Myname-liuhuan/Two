package com.example.two.fragment01;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.LayoutInflaterCompat;
import android.widget.ArrayAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.two.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by liuhuan1 on 2018/6/14.
 */

public class ListAdapter extends ArrayAdapter<ListViewItem> {

    List<ListViewItem>  listViewItemList;
    int itemLayout;
    Context mContext;

    public ListAdapter(@NonNull Context context, int resource, @NonNull List<ListViewItem> listViewItemList) {
        super(context, resource, listViewItemList);
        itemLayout=resource;
        this.listViewItemList =listViewItemList;
        mContext=context;
    }

    @Override
    public int getCount(){
        return listViewItemList.size();
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent){

        View view=LayoutInflater.from(mContext).inflate(itemLayout,parent,false);//和RecyclerView的Adapter的区别是这里的布局是由构造方法传进来的
                                                                                //但是RecyclerView的是直接使用XML文件的文件名，直接使用的
        TextView date=(TextView) view.findViewById(R.id.textViewItem_Date);
        TextView information=(TextView) view.findViewById(R.id.textViewItem_information);
        TextView temperature=(TextView) view.findViewById(R.id.textViewItem_temperature);

        date.setText(listViewItemList.get(position).date);
        information.setText(listViewItemList.get(position).information);
        temperature.setText(listViewItemList.get(position).temperature);

        return view;
    }
}
