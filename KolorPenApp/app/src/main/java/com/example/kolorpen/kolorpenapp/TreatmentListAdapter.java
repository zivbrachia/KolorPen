package com.example.kolorpen.kolorpenapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TreatmentListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] maintitle;
    private final String[] subtitle;
    private final Integer[] imgidBefore;
    private final Integer[] imgidAfter;

    public TreatmentListAdapter(Activity context, String[] maintitle,String[] subtitle, Integer[] imgidBefore, Integer[] imgidAfter) {
        super(context, R.layout.treatment_list_item, maintitle);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.maintitle=maintitle;
        this.subtitle=subtitle;
        this.imgidBefore=imgidBefore;
        this.imgidAfter=imgidAfter;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.treatment_list_item, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        ImageView imageViewBefore = (ImageView) rowView.findViewById(R.id.icon);
        ImageView imageViewAfter = (ImageView) rowView.findViewById(R.id.iconAfter);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);

        titleText.setText(maintitle[position]);
        imageViewBefore.setImageResource(imgidBefore[position]);
        imageViewAfter.setImageResource(imgidAfter[position]);
        subtitleText.setText(subtitle[position]);

        return rowView;

    };
}
