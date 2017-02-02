package io.github.legosteen11.LWCrypt;

import com.google.common.base.Stopwatch;
import io.github.legosteen11.LWCrypt.Encryption.CaesarsCipherObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Created by wouter on 2-2-17.
 */
public class Main {
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
                    System.out.println("You can use these algorithms: caesar");
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
        String key = null;
        String plainText = null;
        String dictionaryPath = "en_dict.txt";
        String timeNeededInMilliseconds = "";
        Stopwatch stopwatch = Stopwatch.createStarted();
        switch (language) {
            case "nl":
                dictionaryPath = "nl_dict.txt";
                break;
            case "en":
                dictionaryPath = "en_dict.txt";
                break;
        }
        File dictionary;
        try {
            dictionary = new File(Main.class.getClassLoader().getResource(dictionaryPath).getFile());
        } catch (NullPointerException e) {
            return false;
        }
        switch (algo) {
            case "caesar":
                CaesarsCipherObject caesarsCipherObject = new CaesarsCipherObject(cipherText);
                boolean cracked = false;
                for(int i = 0; i < 26 && !cracked; i++) {
                    if(i == 0) {
                        continue; // CipherText would already be plaintext...
                    }                    
                    String currentTry = caesarsCipherObject.decrypt(i);
                    
                    if(isCorrect(currentTry, dictionary, correctWordsNeeded)) cracked = true;
                    
                    if(cracked) {
                        stopwatch.stop();
                        timeNeededInMilliseconds = "" + stopwatch.elapsed(TimeUnit.MILLISECONDS);
                        key = "" + i;
                        plainText = currentTry;
                    }
                }
        }
        
        if(plainText == null) {
            return false;
        }
        System.out.println("Ciphertext: " + cipherText);
        System.out.println("Plaintext: " + plainText);
        System.out.println("Key: " + key);
        System.out.println("Time needed in milliseconds: " + timeNeededInMilliseconds);
        
        return true;
    }
    
    public static boolean isCorrect(String plainText, File dictionary, int minimumCorrect) {
        boolean cracked = false;
        int correctWords = 0;
        
        try(Scanner scanner = new Scanner(dictionary)) {
            while(scanner.hasNextLine() && !cracked) {
                String line = scanner.nextLine();
                if (line.length() > 3) {
                    if (plainText.contains(line)) {
                        correctWords++;
                        if (correctWords >= minimumCorrect) {
                            cracked = true;
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            e.printStackTrace();
            return false;
        }
        
        return cracked;
    }
}
