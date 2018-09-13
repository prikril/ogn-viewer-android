package com.meisterschueler.ognviewer.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.meisterschueler.ognviewer.CustomAircraftDescriptor;
import com.meisterschueler.ognviewer.R;
import com.meisterschueler.ognviewer.common.AircraftDescriptorProviderHelper;
import com.meisterschueler.ognviewer.common.CustomAircraftDescriptorProvider;

import java.io.File;
import java.util.Map;

public class ManageIDsFragment extends ListFragment implements AircraftDialogCallback {

    private CustomAircraftDescriptorProvider adp1;
    private Map<String, CustomAircraftDescriptor> aircraftDescriptorMap;
    private CustomAircraftDescriptorAdapter adapter;

    private static final int REQUEST_CODE_IMPORT = 123;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.manageid_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();

        switch (id) {
            case R.id.action_add_item:
                AircraftDialog.showEmptyDialog(getActivity(), this);
                break;

            case R.id.action_import_items:
                Intent intentImport = new Intent(Intent.ACTION_GET_CONTENT);
                intentImport.addCategory(Intent.CATEGORY_OPENABLE);
                intentImport.setType("text/comma-separated-values");
                startActivityForResult(intentImport, REQUEST_CODE_IMPORT);
                break;

            case R.id.action_export_items:
                adp1.writeToFile();
                break;

            case R.id.action_delete_all_items:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                adp1.removeAll();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_IMPORT && resultCode == Activity.RESULT_OK && data != null) {
            adp1.readFromFile(new File(data.getData().getPath()));
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adp1 = (CustomAircraftDescriptorProvider) AircraftDescriptorProviderHelper.getCustomDbAircraftDescriptorProvider();
        aircraftDescriptorMap = adp1.getAircraftDescriptorMap();

        resetListAdapter();

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final CustomAircraftDescriptor cad = adapter.getItem(position);

                if (cad != null) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Remove " + cad.address + " ?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    adp1.removeCustomAircraftDescriptor(cad);
                                    resetListAdapter();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // cancel...
                                }
                            })
                            .create()
                            .show();
                }

                return true; //true to prevent call of other click listeners
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        CustomAircraftDescriptor cad = (CustomAircraftDescriptor) getListAdapter().getItem(position);
        AircraftDialog.showDialog(getActivity(), cad, this);
    }

    private void resetListAdapter() {
        adapter = new CustomAircraftDescriptorAdapter(getActivity(), aircraftDescriptorMap.values().toArray(new CustomAircraftDescriptor[aircraftDescriptorMap.size()]));
        setListAdapter(adapter);
    }

    @Override
    public void notifyUpdated() {
        resetListAdapter();
    }
}
