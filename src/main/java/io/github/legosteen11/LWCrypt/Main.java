package io.github.legosteen11.LWCrypt;

import com.google.common.base.Stopwatch;
import io.github.legosteen11.LWCrypt.Encryption.CaesarsCipherObject;
import io.github.legosteen11.LWCrypt.Encryption.Decrypted;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Created by wouter on 2-2-17.
 */
public class Main {
    public static final int AMOUNT_OF_ALGOS = 1;
    // Voorbeeldtekst: ditberichtisversleuteldmeteenbestwelgoedesleutelmaardevraagisofdesleutelgoedgenoegiswiezoudatweteniknietiniedergeval
    // Voorbeeldtekst versleuteld met caesar -5: inygjwnhmynxajwxqjzyjqirjyjjsgjxybjqltjijxqjzyjqrffwijawfflnxtkijxqjzyjqltjiljstjlnxbnjetzifybjyjsnpsnjynsnjijwljafq
    
    public static void main(String[] args) {
        if(args.length > 0) {
            String option = args[0];
            switch (option) {
                case "decrypt":
                    if(args.length == 4) {
                        String algo = args[1];
                        String key = args[2];
                        String cipherText = args[3];
                        if(decrypt(algo, key, cipherText)) {
                           return; 
                        }
                    }
                    System.out.println("Usage: java -jar LWCrypt.jar decrypt <algorithm> <key> <cipher>");
                    System.out.println("You can use these algorithms: caesar");
                    return;
                case "crack":
                    if(args.length == 5) {
                        String algo = args[1];
                        String cipherText = args[4];
                        String language = args[2];
                        int correctWordsNeeded; 
                        try {
                            correctWordsNeeded = Integer.parseInt(args[3]);
                        } catch (NumberFormatException e) {
                            correctWordsNeeded = 10;
                        }
                        System.out.println("Algorithm: " + algo);
                        System.out.println("Language: " + language);
                        System.out.println("Correct words needed: " + correctWordsNeeded);
                        if(crack(algo, cipherText, language, correctWordsNeeded)) {
                            return;
                        }
                    }
                    System.out.println("Usage: java -jar LWCrypt.jar crack <algorithm> <language> <correct words needed> <cipher>");
                    System.out.println("You can use these algorithms: GUESS, caesar");
                    System.out.println("You can use these languages: nl, en");
            }
        }
        System.out.println("Use like this: java -jar LWCrypt.jar <option>");
    }
    
    public static boolean decrypt(String algo, String key, String cipherText) {
        String result = null;
        switch (algo) {
            case "caesar":
                CaesarsCipherObject caesarsCipherObject = new CaesarsCipherObject(cipherText); // Create a new object with the cipher text
                result = caesarsCipherObject.decrypt(Integer.parseInt(key)); // Set the result with the decryption key
        }

        if (result == null) { // Something went wrong
            System.out.println("Decryption failed!");
            return false;
        } else {
            System.out.println("Decryption successful, from: ");
            System.out.println(cipherText);
            System.out.println("To: ");
            System.out.println(result);
        }
        return true;
    }
    
    public static boolean crack(String algo, String cipherText, String language, int correctWordsNeeded) {
        String dictionaryPath = "en_dict.txt";
        Decrypted decrypted = null;
        switch (language) {
            case "nl":
                dictionaryPath = "nl_dict.txt";
                break;
            case "en":
                dictionaryPath = "en_dict.txt";
                break;
        }
        //File inputDictionary;
        InputStream inputDictionary;
        String[] dictionaryArray = null;
        try {
            //inputDictionary = new File(Main.class.getClassLoader().getResource(dictionaryPath).getFile());
            inputDictionary = Main.class.getClassLoader().getResourceAsStream(dictionaryPath);
        } catch (NullPointerException e) {
            return false;
        }
        
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputDictionary))) {
            dictionaryArray = bufferedReader.lines().toArray(String[]::new);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stopwatch stopwatch = Stopwatch.createStarted();
        
        switch (algo) {
            case "caesar":
                System.out.println("Using Caesar algorithm.");
                decrypted = decryptCaesarsCipher(cipherText, dictionaryArray, correctWordsNeeded);
                break;
            case "GUESS":
                System.out.println("Guessing algorithm... ");
                decrypted = new Decrypted("GUESS", cipherText);
                for(int i = 0; i < AMOUNT_OF_ALGOS && !decrypted.isDecrypted(); i++) {
                    switch(i) {
                        case 0:
                            decrypted = decryptCaesarsCipher(cipherText, dictionaryArray, correctWordsNeeded);
                            break;
                    }
                }
        }
        
        if(decrypted == null) {
            return false;
        } else if(!decrypted.isDecrypted()) {
            System.out.println("Unable to decrypt your cipher!");
            return false;
        }
        stopwatch.stop();

        System.out.println("Decrypted ciphertext!");
        System.out.println("Algorithm: " + decrypted.getAlgorithm());
        System.out.println("Cipher text: " + decrypted.getCipherText());
        System.out.println("Plain text: " + decrypted.getPlainText());
        System.out.println("Key: " + decrypted.getKey());
        System.out.println("Time needed in milliseconds: " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        
        
        return true;
    }
    
    public static boolean isCorrect(String plainText, String[] dictionary, int minimumCorrect) {
        boolean cracked = false;
        int correctWords = 0;

        for (String word :
                dictionary) {
            if (word.length() > 3) {
                if (plainText.contains(word)) {
                    correctWords++;
                    if (correctWords >= minimumCorrect) {
                        cracked = true;
                        break;
                    }
                }
            }
        }
        
        return cracked;
    }
    
    public static Decrypted decryptCaesarsCipher(String cipherText, String[] dictionary, int minimumCorrect) {
        CaesarsCipherObject caesarsCipherObject = new CaesarsCipherObject(cipherText);
        boolean cracked = false;
        for(int i = 0; i < 26; i++) {
            if(i == 0) {
                continue; // CipherText would already be plaintext...
            }
            String currentTry = caesarsCipherObject.decrypt(i);

            if(isCorrect(currentTry, dictionary, minimumCorrect)) cracked = true;

            if(cracked) {
                return new Decrypted("caesar", cipherText, currentTry, i + "");
            }
        }
        return new Decrypted("caesar", cipherText);
    }
}
