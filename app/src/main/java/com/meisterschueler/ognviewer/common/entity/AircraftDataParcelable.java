package com.meisterschueler.ognviewer.common.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dominik on 20.08.2017.
 */

public class AircraftDataParcelable implements Parcelable {

    protected AircraftDataParcelable(Parcel in) {
    }

    public static final Creator<AircraftDataParcelable> CREATOR = new Creator<AircraftDataParcelable>() {
        @Override
        public AircraftDataParcelable createFromParcel(Parcel in) {
            return new AircraftDataParcelable(in);
        }

        @Override
        public AircraftDataParcelable[] newArray(int size) {
            return new AircraftDataParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
