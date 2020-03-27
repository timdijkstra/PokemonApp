package nl.jvhaastert.mbda.pokemonapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Type implements Parcelable {

    private int slot;
    private String name;

    public static final Creator<Type> CREATOR = new Creator<Type>() {
        @Override
        public Type createFromParcel(Parcel in) {
            return new Type(in);
        }

        @Override
        public Type[] newArray(int size) {
            return new Type[size];
        }
    };

    public Type(int slot, String name) {
        this.slot = slot;
        this.name = name;
    }

    protected Type(Parcel in) {
        slot = in.readInt();
        name = in.readString();
    }

    public int getSlot() {
        return slot;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(slot);
        dest.writeString(name);
    }

}
