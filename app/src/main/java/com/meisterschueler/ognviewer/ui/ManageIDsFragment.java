package com.meisterschueler.ognviewer.ui;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.meisterschueler.ognviewer.AircraftDescriptorProviderHelper;
import com.meisterschueler.ognviewer.AircraftDialog;
import com.meisterschueler.ognviewer.CustomAircraftDescriptor;
import com.meisterschueler.ognviewer.CustomAircraftDescriptorAdapter;
import com.meisterschueler.ognviewer.CustomAircraftDescriptorProvider;
import com.meisterschueler.ognviewer.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import co.uk.rushorm.core.Rush;
import co.uk.rushorm.core.RushConflict;
import co.uk.rushorm.core.RushCore;

public class ManageIDsFragment extends ListFragment {
    private Map<String, CustomAircraftDescriptor> aircraftDescriptorMap;
    private String jsonStringTemp;

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
                AircraftDialog.showDialog(getActivity(), "");

                CustomAircraftDescriptorAdapter adapter = (CustomAircraftDescriptorAdapter) getListAdapter();
                adapter.notifyDataSetChanged();

                break;

            case R.id.action_export_list:
                exportList();
                break;

            case R.id.action_import_list:
                importList();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        CustomAircraftDescriptorProvider adp1 = (CustomAircraftDescriptorProvider) AircraftDescriptorProviderHelper.getCustomDbAircraftDescriptorProvider();
        this.aircraftDescriptorMap = adp1.getAircraftDescriptorMap();

        CustomAircraftDescriptorAdapter adapter = new CustomAircraftDescriptorAdapter(getActivity(), aircraftDescriptorMap.values().toArray(new CustomAircraftDescriptor[aircraftDescriptorMap.size()]));
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        CustomAircraftDescriptor cad = (CustomAircraftDescriptor) getListAdapter().getItem(position);
        AircraftDialog.showDialog(getActivity(), cad);
    }

    private void exportList() {
        String jsonString = RushCore.getInstance().serialize(new ArrayList<>(this.aircraftDescriptorMap.values()));
        this.jsonStringTemp = jsonString;
    }

    private void importList() {
        List<Rush> objects = RushCore.getInstance().deserialize(this.jsonStringTemp);
        List<RushConflict> conflicts = RushCore.getInstance().saveOnlyWithoutConflict(objects);

    }
}
