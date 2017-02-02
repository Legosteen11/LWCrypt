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
    
    public String decrypt(int key) {
        this.key = key;
        
        StringBuilder plainTextBuilder = new StringBuilder();
        ArrayList<Integer> charIntArray = new ArrayList<>();
        
        for (char character :
                cipherText.toCharArray()) {
            int cipherCharInt = CharUtils.CharToMod26(character);
            int plainCharInt = Math.abs(Modulo.performModulo(cipherCharInt - key, 26)); // E(x) = x + key (mod 26)
            plainTextBuilder.append(CharUtils.Mod26ToChar(plainCharInt));
        }
        
        this.plainText = plainTextBuilder.toString();
        
        return this.plainText;
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
}
