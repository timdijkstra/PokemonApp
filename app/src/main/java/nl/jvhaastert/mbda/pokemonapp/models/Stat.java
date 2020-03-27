package nl.jvhaastert.mbda.pokemonapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Stat implements Parcelable {

    private String baseStat;

    public static final Creator<Stat> CREATOR = new Creator<Stat>() {
        @Override
        public Stat createFromParcel(Parcel in) {
            return new Stat(in);
        }

        @Override
        public Stat[] newArray(int size) {
            return new Stat[size];
        }
    };

    public Stat(String baseStat) {
        this.baseStat = baseStat;
    }

    protected Stat(Parcel in) {
        baseStat = in.readString();
    }

    public String getBaseStat() {
        return baseStat;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(baseStat);
    }

}
