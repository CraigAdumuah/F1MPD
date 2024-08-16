package com.example.f1mpdresit;

import android.os.Parcel;
import android.os.Parcelable;

public class RaceLocation implements Parcelable {
    private String name;
    private double latitude;
    private double longitude;

    public RaceLocation(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected RaceLocation(Parcel in) {
        name = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<RaceLocation> CREATOR = new Creator<RaceLocation>() {
        @Override
        public RaceLocation createFromParcel(Parcel in) {
            return new RaceLocation(in);
        }

        @Override
        public RaceLocation[] newArray(int size) {
            return new RaceLocation[size];
        }
    };

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }
}
