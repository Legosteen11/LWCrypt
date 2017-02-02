package io.github.legosteen11.LWCrypt.Util;

/**
 * Created by wouter on 2-2-17.
 */
public class CharUtils {
    public static int CharToAscii(char character) {
        return (int) character;
    }
    
    public static char AsciiToChar(int character) {
        return (char) character;
    }
    
    public static int CharToMod26(char character) {
        return Character.toLowerCase(character) - 'a'; // From ASCII to Mod26, so remove all the chars that are before the 'a' char.
    }
    
    public static char Mod26ToChar(int character) {
        return (char) (character + 'a'); // From Mod26 to ASCII, so add up all the chars that are before the 'a' char.
    }
}
