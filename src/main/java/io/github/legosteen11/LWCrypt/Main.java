package io.github.legosteen11.LWCrypt;

import io.github.legosteen11.LWCrypt.Encryption.CaesarsCipherObject;

import java.util.Arrays;

/**
 * Created by wouter on 2-2-17.
 */
public class Main {
    public static void main(String[] args) {
        if(args.length != 3) {
            System.out.println("Use like this: java -jar LWCrypt.jar <Encryption Method> <Key> <Ciphertext>");
            return;
        }
        String option = args[0];
        String key = args[1];
        String cipherText = args[2];
        
        String result = null;
        switch (option) {
            case "caesar":
                CaesarsCipherObject caesarsCipherObject = new CaesarsCipherObject(cipherText); // Create a new object with the cipher text
                result = caesarsCipherObject.decrypt(Integer.parseInt(key)); // Set the result with the decryption key
        }
        
        if(result == null) { // Something went wrong
            System.out.println("Decryption failed!");
            return;
        } else {
            System.out.println("Decryption successful, from: ");
            System.out.println(cipherText);
            System.out.println("To: ");
            System.out.println(result);
        }
    }
}
