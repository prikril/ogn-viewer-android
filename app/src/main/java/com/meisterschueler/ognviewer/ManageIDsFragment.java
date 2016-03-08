package com.meisterschueler.ognviewer;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.Map;

public class ManageIDsFragment extends ListFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        CustomAircraftDescriptorProvider adp1 = (CustomAircraftDescriptorProvider) AircraftDescriptorProviderHelper.getCustomDbAircraftDescriptorProvider();
        Map<String,CustomAircraftDescriptor> aircraftDescriptorMap =  adp1.getAircraftDescriptorMap();

        CustomAircraftDescriptorAdapter adapter = new CustomAircraftDescriptorAdapter(getActivity(), aircraftDescriptorMap.values().toArray(new CustomAircraftDescriptor[aircraftDescriptorMap.size()]));
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        CustomAircraftDescriptor cad = (CustomAircraftDescriptor) getListAdapter().getItem(position);
        AircraftDialog.showDialog(getActivity(), cad);
    }
}
