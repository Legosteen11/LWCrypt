package io.github.legosteen11.LWCrypt.Encryption;

import io.github.legosteen11.LWCrypt.Util.CharUtils;
import io.github.legosteen11.LWCrypt.Util.Modulo;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;

/**
 * Created by wouter on 2-2-17.
 */
public class CaesarsCipherObject {
    private String cipherText;
    private String plainText;
    private int key;

    public CaesarsCipherObject(String cipherText) {
        this.cipherText = cipherText;
    }

    public CaesarsCipherObject() {
    }

    public String decrypt(int key) {
        this.key = key;
        
        StringBuilder plainTextBuilder = new StringBuilder();
        
        for (char character :
                cipherText.toCharArray()) {
            int cipherCharInt = CharUtils.CharToMod26(character);
            int plainCharInt = decryptCharacter(cipherCharInt, key); // E(x) = x + key (mod 26)
            plainTextBuilder.append(CharUtils.Mod26ToChar(plainCharInt));
        }
        
        this.plainText = plainTextBuilder.toString();
        
        return this.plainText;
    }
    
    public String encrypt(String plainText, int key) {
        this.key = key;
        this.plainText = plainText;

        StringBuilder cipherTextBuilder = new StringBuilder();

        for (char character :
                plainText.toCharArray()) {
            int cipherCharInt = CharUtils.CharToMod26(character);
            int plainCharInt = encryptCharacter(cipherCharInt, key); // E(x) = x + key (mod 26)
            cipherTextBuilder.append(CharUtils.Mod26ToChar(plainCharInt));
        }

        this.cipherText = cipherTextBuilder.toString();

        return this.cipherText;
    }
    
    public static Decrypted crack(String cipherText, HashMap<Character, Double> correctFrequencies) {
        cipherText = cipherText.toLowerCase();
        int textLength = cipherText.length();
        
        HashMap<Character, Integer> characterCountMap = new HashMap<>();
        for (int i = 0; i < 26; i++) {
            characterCountMap.put(CharUtils.Mod26ToChar(i), StringUtils.countMatches(cipherText, CharUtils.Mod26ToChar(i)));
        }
        
        int bestKey = 0;
        double bestFrequencyDelta = Double.MAX_VALUE;
        for (int i = 0; i < 26; i++) {
            HashMap<Character, Integer> currentCountMap = new HashMap<>();
            for (int j = 0; j < 26; j++) {
                currentCountMap.put(CharUtils.Mod26ToChar(j), characterCountMap.get(CharUtils.Mod26ToChar(Modulo.performModulo(j + i, 26))));
            }
            
            double currentFrequencyDelta = VigenereCipherObject.getFrequencyDelta(
                    VigenereCipherObject.createFrequencies(currentCountMap, textLength), 
                    correctFrequencies);
            
            if(currentFrequencyDelta < bestFrequencyDelta) {
                bestFrequencyDelta = currentFrequencyDelta;
                bestKey = i;
            }
        }
        
        return new Decrypted("caesar", cipherText, new CaesarsCipherObject(cipherText).decrypt(bestKey), bestKey + "");
    }

    public String getCipherText() {
        return cipherText;
    }

    public String getPlainText() {
        return plainText;
    }

    public int getKey() {
        return key;
    }
    
    public static int decryptCharacter(int cipherChar, int keyInt) {
        return Math.abs(Modulo.performModulo(cipherChar - keyInt, 26)); // E(x) = x - key (mod 26)
    }
    
    public static int encryptCharacter(int plainChar, int keyInt) {
        return Math.abs(Modulo.performModulo(plainChar + keyInt, 26)); // E(x) = x - key (mod 26)
    }
}
