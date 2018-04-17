package com.example.kyle.forgradle.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class EntityParcelable implements Parcelable {
    private String mString;
    private double mDouble;
    private int mInt;
    private boolean mBln;

    public EntityParcelable() {

    }

    public void setString(String str) {
        mString = str;
    }

    public void setValues(String s, double d, int i, boolean b) {
        mString = s;
        mDouble = d;
        mInt = i;
        mBln = b;
    }

    public EntityParcelable(Parcel source) {
        readFromParcel(source);
    }

    public static final Parcelable.Creator<EntityParcelable> CREATOR = new
            Creator<EntityParcelable>() {
        @Override
        public EntityParcelable createFromParcel(Parcel source) {
            return new EntityParcelable(source);
        }

        @Override
        public EntityParcelable[] newArray(int size) {
            return new EntityParcelable[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mString);
        dest.writeDouble(mDouble);
        dest.writeInt(mInt);
        dest.writeInt(mBln ? 1 : 0);
    }

    public void readFromParcel(Parcel src) {
        mString = src.readString();
        mDouble = src.readDouble();
        mInt = src.readInt();
        mBln = src.readInt() == 1;
    }

    @Override
    public String toString() {
        return "EntityParcelable:" + "[" + mString + "," + mDouble + "," + mInt + "," + mBln + "]";
    }
}
