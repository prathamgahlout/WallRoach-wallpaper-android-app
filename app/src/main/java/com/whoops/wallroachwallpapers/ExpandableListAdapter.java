package com.whoops.wallroachwallpapers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;



import java.util.HashMap;
import java.util.List;


public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listDataItem;

    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listDataItem) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataItem = listDataItem;
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return this.listDataItem.get(this.listDataHeader.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return this.listDataHeader.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return this.listDataItem.get(this.listDataHeader.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        final String headertitle=(String) getGroup(i);
        if (view==null){
            LayoutInflater inflater=(LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.list_group,null);
        }
        TextView txt=view.findViewById(R.id.listheader);
        txt.setText(headertitle);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final String childtext=(String) getChild(i,i1);
        if (view==null){
            LayoutInflater inflater=(LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.list_item,null);
        }
        TextView txt=view.findViewById(R.id.listchild);
        txt.setText(childtext);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
