package com.meisterschueler.ognviewer;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.ogn.commons.beacon.AircraftDescriptor;
import org.ogn.commons.beacon.descriptor.AircraftDescriptorProvider;

import java.util.ArrayList;
import java.util.List;

public class AircraftDialog {

    public static void showDialog(Context context, final String address) {
        AircraftDescriptorProvider adp1 = AircraftDescriptorProviderHelper.getCustomDbAircraftDescriptorProvider();
        AircraftDescriptorProvider adp2 = AircraftDescriptorProviderHelper.getOgnDbAircraftDescriptorProvider();

        AircraftDescriptor descriptor = adp1.findDescriptor(address);
        if (descriptor == null) {
            descriptor = adp2.findDescriptor(address);
        }

        CustomAircraftDescriptor cad;
        if (descriptor != null) {
            cad = new CustomAircraftDescriptor(address, descriptor);
        } else {
            cad = new CustomAircraftDescriptor(address, "", "", "", "", "", "");
        }

        showDialog(context, cad);
    }

    public static void showDialog(Context context, final CustomAircraftDescriptor cad) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_aircraft, null);
        TextView tv = (TextView) view.findViewById(R.id.title);

        final EditText etRegNumber = (EditText) view.findViewById(R.id.editTextRegNumber);
        final EditText etCN = (EditText) view.findViewById(R.id.editTextCN);
        final EditText etOwner = (EditText) view.findViewById(R.id.editTextOwner);
        final EditText etHomeBase = (EditText) view.findViewById(R.id.editTextHomeBase);
        final EditText etModel = (EditText) view.findViewById(R.id.editTextModel);
        final EditText etFreq = (EditText) view.findViewById(R.id.editTextFreq);

        tv.setText(cad.address);
        etRegNumber.setText(cad.regNumber);
        etCN.setText(cad.CN);
        etOwner.setText(cad.owner);
        etHomeBase.setText(cad.homeBase);
        etModel.setText(cad.model);
        etFreq.setText(cad.freq);

        final Spinner spModel = (Spinner) view.findViewById(R.id.spinner);
        List<String> list = new ArrayList<String>();
        list.add("Cirrus");
        list.add("DG 1000");
        list.add("LS 4");
        list.add("Mü 28");
        list.add("LS4");
        list.add("ASW-24");
        list.add("KA 6");
        list.add("Ka8");
        list.add("Mü 17");
        list.add("ASH 25");
        list.add("Mü 22");
        list.add("ASK 13");
        list.add("SZD 59");
        list.add("Kestrel");
        list.add("Duo Discus");
        list.add("Nimbus 3");
        list.add("ASW-20");
        list.add("LS4");
        list.add("Twin Astir");
        list.add("DG-1000");
        list.add("Discus 2c");
        list.add("Kestrel");
        list.add("Discus 2c-18m");
        list.add("LS 3a");
        list.add("LS 8");
        list.add("LS-1");
        list.add("LS-4");
        list.add("Discus");
        list.add("ASW 19");
        list.add("Discus");
        list.add("LS-4a");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spModel.setAdapter(dataAdapter);

        new AlertDialog.Builder(context)
                .setView(view)
                .setTitle("Edit aircraft informations")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        cad.regNumber = etRegNumber.getText().toString();
                        cad.CN = etCN.getText().toString();
                        cad.owner = etOwner.getText().toString();
                        cad.homeBase = etHomeBase.getText().toString();
                        cad.model = etModel.getText().toString();
                        cad.freq = etFreq.getText().toString();

                        CustomAircraftDescriptorProvider cadp = (CustomAircraftDescriptorProvider) AircraftDescriptorProviderHelper.getCustomDbAircraftDescriptorProvider();
                        cadp.saveCustomAircraftDescriptor(cad);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // cancel...
                    }
                })
                .create()
                .show();
    }
    }
