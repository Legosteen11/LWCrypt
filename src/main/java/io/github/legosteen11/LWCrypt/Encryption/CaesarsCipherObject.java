package io.github.legosteen11.LWCrypt.Encryption;

import io.github.legosteen11.LWCrypt.Util.CharUtils;
import io.github.legosteen11.LWCrypt.Util.Modulo;

import java.util.ArrayList;

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
