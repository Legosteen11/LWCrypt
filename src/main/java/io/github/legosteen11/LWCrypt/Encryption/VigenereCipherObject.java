package io.github.legosteen11.LWCrypt.Encryption;

import io.github.legosteen11.LWCrypt.Util.CharUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by wouter on 2-2-17.
 */
public class VigenereCipherObject {
    private String cipherText;
    private String plainText;
    private String key;
    
    public VigenereCipherObject() {
        
    }

    public VigenereCipherObject(String cipherText) {
        this.cipherText = cipherText;
    }

    public String decrypt(String key) {
        this.key = key;

        StringBuilder plainTextBuilder = new StringBuilder();

        int currentKeyChar = 0;
        for (char character :
                cipherText.toCharArray()) {
            if(currentKeyChar >= key.length()) {
                currentKeyChar = 0;
            }
            int cipherCharInt = CharUtils.CharToMod26(character);
            int plainCharInt = CaesarsCipherObject.decryptCharacter(cipherCharInt, CharUtils.CharToMod26(key.charAt(currentKeyChar))); // E(x) = x + key (mod 26)
            plainTextBuilder.append(CharUtils.Mod26ToChar(plainCharInt));
            currentKeyChar++;
        }

        this.plainText = plainTextBuilder.toString();

        return this.plainText;
    }

    public String encrypt(String plainText, String key) {
        this.key = key;
        this.plainText = plainText;

        StringBuilder cipherTextBuilder = new StringBuilder();

        int currentKeyChar = 0;
        for (char character :
                plainText.toCharArray()) {
            if(currentKeyChar >= key.length()) {
                currentKeyChar = 0;
            }
            int cipherCharInt = CharUtils.CharToMod26(character);
            int plainCharInt = CaesarsCipherObject.encryptCharacter(cipherCharInt, CharUtils.CharToMod26(key.charAt(currentKeyChar))); // E(x) = x + key (mod 26)
            cipherTextBuilder.append(CharUtils.Mod26ToChar(plainCharInt));
            currentKeyChar++;
        }

        this.cipherText = cipherTextBuilder.toString();

        return this.cipherText;
    }
    
    public static String[] stringToTriplets(String string) {
        Set<String> stringsSet = new HashSet<>();
        for(int i = 0; i < string.length() - 2; i++) {
            stringsSet.add(string.substring(i, i + 2));
        }
        return stringsSet.toArray(new String[0]);
    }
    
    public static int[] distanceBetweenPositions(int[] positions) {
        ArrayList<Integer> distances = new ArrayList<>();
        for (int position :
                positions) {
            for (int i = 0; i < positions.length; i++) {
                int difference = positions[i] - position;
                
                if(difference <= 0) continue;
                
                distances.add(difference);
            }
        }
        
        return distances.stream().mapToInt(i->i).toArray();
    }
    
    public static int[] positionsInString(String match, String string) {
        ArrayList<Integer> distanceList = new ArrayList<>();
        String remainingString = string;
        int distanceStrippedAway = 0;
        while (remainingString.contains(match)) {
            int distanceToStrip = remainingString.indexOf(match);
            distanceList.add(distanceStrippedAway + distanceToStrip);
            distanceToStrip += match.length();
            distanceStrippedAway += distanceToStrip;
            remainingString = remainingString.substring(distanceToStrip, remainingString.length());
        }
        return distanceList.stream().mapToInt(i->i).toArray();
    }
    
    public static int[] possibleKeySizes(int distanceBetweenPositions) {
        ArrayList<Integer> possibleKeySizes = new ArrayList<>();
        for(double i = 1; i <= distanceBetweenPositions; i++) {
            if(distanceBetweenPositions / i % 1 == 0) possibleKeySizes.add((int) i);
        }
        
        return possibleKeySizes.stream().mapToInt(i->i).toArray();
    }
    
    public String getCipherText() {
        return cipherText;
    }

    public String getPlainText() {
        return plainText;
    }

    public String getKey() {
        return key;
    }
}
