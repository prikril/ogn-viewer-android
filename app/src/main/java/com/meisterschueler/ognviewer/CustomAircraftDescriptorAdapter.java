package com.meisterschueler.ognviewer;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomAircraftDescriptorAdapter extends ArrayAdapter<CustomAircraftDescriptor> {

    private final Context context;
    private final CustomAircraftDescriptor[] values;

    public CustomAircraftDescriptorAdapter(Context context, CustomAircraftDescriptor[] values) {
        super(context, R.layout.rowlayout, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
        TextView tvAddress = rowView.findViewById(R.id.column_address);
        TextView tvRegNumber = rowView.findViewById(R.id.column_regNumber);
        TextView tvCN = rowView.findViewById(R.id.column_CN);
        TextView tvModel = rowView.findViewById(R.id.column_model);
        TextView tvOwner = rowView.findViewById(R.id.column_owner);
        //ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        CustomAircraftDescriptor cad = values[position];
        tvAddress.setText(cad.address);
        tvRegNumber.setText(cad.regNumber);
        tvCN.setText(cad.CN);
        tvModel.setText(cad.model);
        tvOwner.setText(cad.owner);
        // Change the icon for Windows and iPhone
        /*String s = values[position];
        if (s.startsWith("Windows7") || s.startsWith("iPhone")
                || s.startsWith("Solaris")) {
            imageView.setImageResource(R.drawable.no);
        } else {
            imageView.setImageResource(R.drawable.ok);
        }*/

        return rowView;
    }


}
