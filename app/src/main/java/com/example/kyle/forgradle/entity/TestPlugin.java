package com.example.kyle.forgradle.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class TestPlugin implements Parcelable {
    private int value;
    private String name;
    private boolean flag;
    private float valueF;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.value);
        dest.writeString(this.name);
        dest.writeByte(this.flag ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.valueF);
    }

    public void readFromParcel(Parcel src){

    }

    public TestPlugin() {
    }

    protected TestPlugin(Parcel in) {
        this.value = in.readInt();
        this.name = in.readString();
        this.flag = in.readByte() != 0;
        this.valueF = in.readFloat();
    }

    public static final Parcelable.Creator<TestPlugin> CREATOR = new Parcelable
            .Creator<TestPlugin>() {
        @Override
        public TestPlugin createFromParcel(Parcel source) {
            return new TestPlugin(source);
        }

        @Override
        public TestPlugin[] newArray(int size) {
            return new TestPlugin[size];
        }
    };
}
