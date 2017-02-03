package io.github.legosteen11.LWCrypt.Encryption;

import io.github.legosteen11.LWCrypt.Util.CharUtils;

import java.util.ArrayList;

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
