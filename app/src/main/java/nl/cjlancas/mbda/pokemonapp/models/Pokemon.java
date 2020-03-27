package nl.cjlancas.mbda.pokemonapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Pokemon implements Parcelable {

    private String url;

    private String name;
    private int id;
    private List<Type> types;
    private String imageUrl;
    private List<Stat> stats;

    public Pokemon(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public Pokemon(String url, String name, int id, List<Type> types, String imageUrl, List<Stat> stats) {
        this.url = url;
        this.name = name;
        this.id = id;
        this.types = types;
        this.imageUrl = imageUrl;
        this.stats = stats;
    }

    protected Pokemon(Parcel in) {
        this.url = in.readString();
        this.name = in.readString();
        this.id = in.readInt();
        in.readTypedList(types, Type.CREATOR);
        this.imageUrl = in.readString();
        in.readTypedList(stats, Stat.CREATOR);
    }

    public static final Creator<Pokemon> CREATOR = new Creator<Pokemon>() {
        @Override
        public Pokemon createFromParcel(Parcel in) {
            return new Pokemon(in);
        }

        @Override
        public Pokemon[] newArray(int size) {
            return new Pokemon[size];
        }
    };

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public List<Type> getTypes() {
        return types;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<Stat> getStats() {
        return stats;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(name);
        dest.writeInt(id);
        dest.writeString(imageUrl);
    }

}
