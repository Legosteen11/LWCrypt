package io.github.legosteen11.LWCrypt.Util;

/**
 * Created by wouter on 2-2-17.
 */
public class Modulo {
    public static int performModulo(int input, int mod) {
        int firstModulo = input % mod;
        if(firstModulo < 0) {
            return mod + firstModulo;
        } else {
            return firstModulo;
        }
    }
}
