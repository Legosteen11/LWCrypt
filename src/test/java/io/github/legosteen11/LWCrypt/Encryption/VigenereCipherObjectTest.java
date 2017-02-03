package io.github.legosteen11.LWCrypt.Encryption;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by wouter on 2-2-17.
 */
public class VigenereCipherObjectTest {
    public VigenereCipherObject vigenereCipherObjectPlain;
    public VigenereCipherObject vigenereCipherObjectCipher;
    
    public static final String TEST_STRING = "hoiikbenwouterenditismijntestberichtjeikhoopdatditwerktdatzouwelprettigzijneigenlijk";
    public static final String TEST_STRING_ENCRYPTED = "roiaubefgoulorefnitacmibxtekdbejschlteicroohnatvstwwbktvktzgewedzreldigrsjnwsgefvijc";
    public static final String TEST_KEY = "kaas";

    @Before
    public void setUp() throws Exception {
        vigenereCipherObjectPlain = new VigenereCipherObject();
        vigenereCipherObjectCipher = new VigenereCipherObject(vigenereCipherObjectPlain.encrypt(TEST_STRING, TEST_KEY));
    }

    @Test
    public void decrypt() throws Exception {
        assertEquals(TEST_STRING, vigenereCipherObjectCipher.decrypt(TEST_KEY));
        assertEquals(TEST_STRING, vigenereCipherObjectPlain.getPlainText());
    }

    @Test
    public void encrypt() throws Exception {
        assertEquals(TEST_STRING_ENCRYPTED, vigenereCipherObjectPlain.encrypt(TEST_STRING, TEST_KEY));
        assertEquals(TEST_STRING_ENCRYPTED, vigenereCipherObjectCipher.encrypt(TEST_STRING, TEST_KEY));
    }

}