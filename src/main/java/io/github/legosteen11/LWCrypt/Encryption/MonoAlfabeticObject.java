package io.github.legosteen11.LWCrypt.Encryption;

import io.github.legosteen11.LWCrypt.Util.CharUtils;
import io.github.legosteen11.LWCrypt.Util.Modulo;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wouter on 4-2-17.
 */
public class MonoAlfabeticObject {
    private String key;
    private String cipherText;
    private String plainText;

    public MonoAlfabeticObject(String cipherText) {
        this.cipherText = cipherText;
    }
    
    public String encrypt(String plainText, String key) {
        plainText = plainText.toLowerCase();
        this.key = key;
        this.plainText = plainText;
        String cipherText = plainText;
        for (int i = 0; i < key.length(); i++) {
            cipherText = cipherText.replaceAll(CharUtils.Mod26ToChar(i) + "", key.charAt(i) + "");
        }
        this.cipherText = cipherText;
        return cipherText;
    }
    
    public String decrypt(String key) {
        this.key = key;
        for (int i = 0; i < key.length(); i++) {
            cipherText = cipherText.replaceAll(key.charAt(i) + "", CharUtils.Mod26ToChar(i) + "");
        }
        return cipherText;
    }

    public static char[] mostLikelyKeys(int count, HashMap<Character, Double> correctFrequencies, double minimumDelta, int stringSize) {
        if(count == 0) return new char[]{'a'};
        char bestChar = 'a';
        double lowestDelta = Double.MAX_VALUE;
        double frequency = (count / (double) stringSize) * 100;
        ArrayList<Character> mostLikelyKeys = new ArrayList<>();
        int totalChars = 0;
        for (int i = 0; i < 26; i++) {
            double currentDelta = Math.abs(frequency - correctFrequencies.get(CharUtils.Mod26ToChar(i)));
            if(currentDelta < lowestDelta) {
                lowestDelta = currentDelta;
                bestChar = CharUtils.Mod26ToChar(i);
            }
            if(currentDelta < minimumDelta) mostLikelyKeys.add(CharUtils.Mod26ToChar(i));
        }
        if(mostLikelyKeys.size() == 0) {
            return new char[]{bestChar};
        }
        
        return (char[]) ArrayUtils.toPrimitive(mostLikelyKeys);
    }
    
    public static String[] createKeys(HashMap<Character, Character[]> characterHashMap, int minimumDelta) {
        ArrayList<String> keys = new ArrayList<>();
        HashMap<Character, Integer> charPositionsList = new HashMap<>();
        for (int i = 0; i < characterHashMap.get('a')[characterHashMap.get('a').length]; i++) {
            for(int j = 0; j < 26; j++) {
                // TODO: do this lol

            }
        }
        // TODO: fix this
        return null;
    }
    
    public static Decrypted crack(String cipherText, HashMap<Character, Double> correctFrequencies, int minimumDelta) {
        cipherText = cipherText.toLowerCase();
        HashMap<Character, Character[]> possibleKeys = new HashMap<>();
        HashMap<Character, Integer> characterCountHashMap = VigenereCipherObject.charAmounts(cipherText);
        for (int i = 0; i < 26; i++) {
            possibleKeys.put(CharUtils.Mod26ToChar(i), ArrayUtils.toObject(mostLikelyKeys(characterCountHashMap.get(CharUtils.Mod26ToChar(i)), correctFrequencies, minimumDelta, cipherText.length())));
        }
        // TODO: do this lol
        return null;
    }

    public String getKey() {
        return key;
    }

    public String getCipherText() {
        return cipherText;
    }

    public String getPlainText() {
        return plainText;
    }
}
