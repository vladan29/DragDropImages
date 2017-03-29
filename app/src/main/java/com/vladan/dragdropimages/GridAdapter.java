package com.vladan.dragdropimages;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created  on 3/26/2017.
 */

public class GridAdapter extends BaseAdapter {


    public Activity activity;
    private LayoutInflater inflater;
    private List<BasisGrid> basisGrids;
    GridView gridView;

    public GridAdapter(Activity activity,List<BasisGrid>basisGrids){
        this.activity=activity;
        this.basisGrids=basisGrids;
    }
    @Override
    public int getCount() {
        return basisGrids.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater==null){
            inflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView==null){
            convertView=inflater.inflate(R.layout.grid_item,null);
        }

        RelativeLayout relativeLayout=(RelativeLayout) inflater.inflate(R.layout.activity_drag_drop,null);
        gridView=(GridView)relativeLayout.findViewById(R.id.gridImage);
        TextView grid_item_label = (TextView) convertView.findViewById(R.id.grid_item_label);
        ImageView grid_item_image = (ImageView) convertView.findViewById(R.id.grid_item_image);
        final BasisGrid bG=basisGrids.get(position);
        grid_item_label.setText(bG.getTextComment());
        grid_item_image.setImageResource(bG.getImageId());
        return convertView;
    }
}
