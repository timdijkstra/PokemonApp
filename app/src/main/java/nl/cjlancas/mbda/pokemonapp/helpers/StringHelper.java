package nl.cjlancas.mbda.pokemonapp.helpers;

public class StringHelper {
    
    public static String capitalize(String lowercase) {
        return lowercase.substring(0, 1).toUpperCase() + lowercase.substring(1);
    }

}
