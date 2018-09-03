package com.meisterschueler.ognviewer.ui;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.meisterschueler.ognviewer.common.AircraftDescriptorProviderHelper;
import com.meisterschueler.ognviewer.CustomAircraftDescriptor;
import com.meisterschueler.ognviewer.common.CustomAircraftDescriptorProvider;
import com.meisterschueler.ognviewer.R;

import org.ogn.commons.beacon.AircraftDescriptor;
import org.ogn.commons.beacon.descriptor.AircraftDescriptorProvider;

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

        showDialog(context, cad, null);
    }

    public static void showEmptyDialog(Context context, AircraftDialogCallback callback) {
        CustomAircraftDescriptor cad = new CustomAircraftDescriptor("", "", "", "", "", "", "");
        showDialog(context, cad, callback);
    }

    public static void showDialog(Context context, final CustomAircraftDescriptor cad, final AircraftDialogCallback callback) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_aircraft, null);

        final EditText etAddress = view.findViewById(R.id.editTextAddress);
        final EditText etRegNumber = view.findViewById(R.id.editTextRegId);
        final EditText etCN = view.findViewById(R.id.editTextCN);
        final EditText etOwner = view.findViewById(R.id.editTextOwner);
        final EditText etHomeBase = view.findViewById(R.id.editTextHomeBase);
        final EditText etModel = view.findViewById(R.id.editTextModel);
        final EditText etFreq = view.findViewById(R.id.editTextFreq);

        etAddress.setText(cad.address);
        etRegNumber.setText(cad.regNumber);
        etCN.setText(cad.CN);
        etOwner.setText(cad.owner);
        etHomeBase.setText(cad.homeBase);
        etModel.setText(cad.model);
        etFreq.setText(cad.freq);

        new AlertDialog.Builder(context)
                .setView(view)
                .setTitle("Edit aircraft information")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        cad.address = etAddress.getText().toString();
                        cad.regNumber = etRegNumber.getText().toString();
                        cad.CN = etCN.getText().toString();
                        cad.owner = etOwner.getText().toString();
                        cad.homeBase = etHomeBase.getText().toString();
                        cad.model = etModel.getText().toString();
                        cad.freq = etFreq.getText().toString();

                        CustomAircraftDescriptorProvider cadp = (CustomAircraftDescriptorProvider) AircraftDescriptorProviderHelper.getCustomDbAircraftDescriptorProvider();
                        cadp.saveCustomAircraftDescriptor(cad);
                        if (callback != null) {
                            callback.notifyUpdated();
                        }
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
