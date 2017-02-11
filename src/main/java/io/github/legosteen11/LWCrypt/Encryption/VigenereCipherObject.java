package io.github.legosteen11.LWCrypt.Encryption;

import io.github.legosteen11.LWCrypt.Util.CharUtils;
import io.github.legosteen11.LWCrypt.Util.Modulo;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by wouter on 2-2-17.
 */
public class VigenereCipherObject {
    public static final HashMap<Character, Double> NL_LETTER_FREQUENCY = nlLetterFrequencyMap();
    public static final HashMap<Character, Double> EN_LETTER_FREQUENCY = enLetterFrequencyMap();
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
        plainText = plainText.toLowerCase();
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
            for (int positionNext : positions) {
                int difference = positionNext - position;

                if (difference <= 0) continue;

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
    
    public static ArrayList<Integer> possibleKeySizes(int distanceBetweenPositions) {
        ArrayList<Integer> possibleKeySizes = new ArrayList<>();
        for(double i = 1; i <= distanceBetweenPositions; i++) {
            if(distanceBetweenPositions / i % 1 == 0) possibleKeySizes.add((int) i);
        }
        
        return possibleKeySizes;
    }
    
    public static int mostLikelyKeySize(String string) {
        String[] triplets = stringToTriplets(string);
        ArrayList<Integer> possibleKeySizesList = new ArrayList<>();
        for (String triplet :
                triplets) {
            int[] distancesBetweenPositions = distanceBetweenPositions(positionsInString(triplet, string));
            for (int distanceBetweenPositions :
                    distancesBetweenPositions) {
                possibleKeySizesList.addAll(possibleKeySizes(distanceBetweenPositions));
            }
        }
        int[] keySizes = {0,0,0};
        int[] keySizeFreq = {0,0,0};

        for (int i = 0; i < 20; i++) {
            if(i < 3) continue;
            int frequency = Collections.frequency(possibleKeySizesList, i);
            if(frequency > keySizeFreq[2]) {
                if(frequency > keySizeFreq[1]) {
                    if (frequency > keySizeFreq[0]) {
                        keySizeFreq[2] = keySizeFreq[1];
                        keySizes[2] = keySizes[1];
                        keySizeFreq[1] = keySizeFreq[0];
                        keySizes[1] = keySizes[0];
                        keySizeFreq[0] = frequency;
                        keySizes[0] = i;
                    } else {
                        keySizeFreq[2] = keySizeFreq[1];
                        keySizes[2] = keySizes[1];
                        keySizeFreq[1] = frequency;
                        keySizes[1] = i;
                    }
                } else {
                    keySizeFreq[2] = frequency;
                    keySizes[2] = i;
                }
            }
        }
        
        if(keySizeFreq[0] == keySizeFreq[1]) {
            if(keySizes[0] < keySizes[1]) {
                int oldFirstKeySize = keySizes[0];
                keySizes[0] = keySizes[1];
                keySizes[1] = oldFirstKeySize;
                
            }
        }
        
        System.out.println("These are the three most likely key sizes: ");
        for (int i = 0; i < 3; i++) {
            System.out.println("Key size " + keySizes[i] + " occurs " + keySizeFreq[i] + " times.");
        }
        return keySizes[0];
    }
    
    public static double getFrequencyDelta(HashMap<Character, Double> charsWithFrequencies, HashMap<Character, Double> correctFrequencies) {
        double delta = 0;
        for (int i = 0; i < 26; i++) {
            delta += Math.abs((double) charsWithFrequencies.get(CharUtils.Mod26ToChar(i)) - correctFrequencies.get(CharUtils.Mod26ToChar(i)));
        }
        return delta;
    }
    
    public static HashMap<Character, Double> createFrequencies(HashMap<Character, Integer> characterIntegerHashMap, int mapSizeTotal) {
        HashMap<Character, Double> frequencyMap = new HashMap<>();
        for (int i = 0; i < 26; i++) {
            int charAmount = characterIntegerHashMap.getOrDefault(CharUtils.Mod26ToChar(i), 0);
            double frequency = (charAmount / (double) mapSizeTotal) * 100;
            frequencyMap.put(CharUtils.Mod26ToChar(i), frequency);
        }
        return frequencyMap;
    }
    
    public static HashMap<Character, Integer> charAmounts(String string) {
        HashMap<Character, Integer> charAmountMap = new HashMap<>();
        for (int i = 0; i < string.length(); i++) {
            char character = string.charAt(i);
            Integer value = charAmountMap.get(character);
            if (value == null) {
                charAmountMap.put(character, 1);
            } else {
                charAmountMap.put(character, value + 1);
            }
        }
        return charAmountMap;
    }
    
    public static char mostLikelyKey(HashMap<Character, Integer> characterIntegerHashMap, HashMap<Character, Double> correctFrequencies, int totalSize) {
        char mostLikelyKey = 'a';
        double delta = Integer.MAX_VALUE;
        for (int i = 0; i < 26; i++) {
            HashMap<Character, Integer> currentCharAmounts = new HashMap<>();
            int totalChars = 0;
            for (int j = 0; j < 26; j++) {
                int amount = characterIntegerHashMap.getOrDefault(CharUtils.Mod26ToChar(Modulo.performModulo(j + i, 26)), 0);
                currentCharAmounts.put(CharUtils.Mod26ToChar(j), amount);
                totalChars+=amount;
            }
            double frequencyDelta = getFrequencyDelta(createFrequencies(currentCharAmounts, totalChars), correctFrequencies);
            if(frequencyDelta < delta) {
                delta = frequencyDelta;
                mostLikelyKey = CharUtils.Mod26ToChar(i);
            }
        }
        
        return mostLikelyKey;
    }
    
    public static Decrypted crack(String cipherText, HashMap<Character, Double> correctFrequencies) {
        cipherText = cipherText.toLowerCase();
        int mostLikelyKeySize = mostLikelyKeySize(cipherText);
        return crack(cipherText, correctFrequencies, mostLikelyKeySize);
    }

    public static Decrypted crack(String cipherText, HashMap<Character, Double> correctFrequencies, int keySize) {
        cipherText = cipherText.toLowerCase();
        String key = "";
        for(int i = 0; i < keySize; i++) {
            ArrayList<Character> currentChars = new ArrayList<>();
            for (int j = i; j < cipherText.length(); j+= keySize) {
                currentChars.add(cipherText.charAt(j));
            }
            HashMap<Character, Integer> charAmounts = charAmounts(currentChars.stream().map(Object::toString).collect(Collectors.joining()));
            key += mostLikelyKey(charAmounts, correctFrequencies, cipherText.length());
        }
        VigenereCipherObject vigenereCipherObject = new VigenereCipherObject(cipherText);
        String plainText = vigenereCipherObject.decrypt(key);
        return new Decrypted("vigenere", cipherText, plainText, key);
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
    
    private static HashMap<Character, Double> nlLetterFrequencyMap() { // Create a map with all the dutch letter frequencies.
        HashMap<Character, Double> frequencies = new HashMap<>();
        frequencies.put('a', 7.49);
        frequencies.put('b', 1.58);
        frequencies.put('c', 1.24);
        frequencies.put('d', 5.93);
        frequencies.put('e', 18.91);
        frequencies.put('f', 0.81);
        frequencies.put('g', 3.40);
        frequencies.put('h', 2.38);
        frequencies.put('i', 6.50);
        frequencies.put('j', 1.46);
        frequencies.put('k', 2.25);
        frequencies.put('l', 3.57);
        frequencies.put('m', 2.21);
        frequencies.put('n', 10.03);
        frequencies.put('o', 6.06);
        frequencies.put('p', 1.57);
        frequencies.put('q', 0.009);
        frequencies.put('r', 6.41);
        frequencies.put('s', 3.73);
        frequencies.put('t', 6.79);
        frequencies.put('u', 1.99);
        frequencies.put('v', 2.85);
        frequencies.put('w', 1.52);
        frequencies.put('x', 0.04);
        frequencies.put('y', 0.035);
        frequencies.put('z', 1.39);
        return frequencies;
    }
    
    private static HashMap<Character, Double> enLetterFrequencyMap() { // Create a map with all the dutch letter frequencies.
        HashMap<Character, Double> frequencies = new HashMap<>();
        frequencies.put('a', 8.167);
        frequencies.put('b', 1.492);
        frequencies.put('c', 2.782);
        frequencies.put('d', 4.253);
        frequencies.put('e', 12.702);
        frequencies.put('f', 2.228);
        frequencies.put('g', 2.015);
        frequencies.put('h', 6.094);
        frequencies.put('i', 6.966);
        frequencies.put('j', 0.153);
        frequencies.put('k', 0.772);
        frequencies.put('l', 4.025);
        frequencies.put('m', 2.406);
        frequencies.put('n', 6.749);
        frequencies.put('o', 7.507);
        frequencies.put('p', 1.929);
        frequencies.put('q', 0.095);
        frequencies.put('r', 5.987);
        frequencies.put('s', 6.327);
        frequencies.put('t', 9.056);
        frequencies.put('u', 2.758);
        frequencies.put('v', 0.978);
        frequencies.put('w', 2.360);
        frequencies.put('x', 0.150);
        frequencies.put('y', 1.974);
        frequencies.put('z', 0.074);
        return frequencies;
    }
    
    
}
