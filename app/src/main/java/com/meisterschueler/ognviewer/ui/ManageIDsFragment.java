package com.meisterschueler.ognviewer.ui;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
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

import java.util.Map;

public class ManageIDsFragment extends ListFragment implements AircraftDialogCallback {

    private CustomAircraftDescriptorProvider adp1;
    private Map<String, CustomAircraftDescriptor> aircraftDescriptorMap;
    private CustomAircraftDescriptorAdapter adapter;

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
        }

        return super.onOptionsItemSelected(item);
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
