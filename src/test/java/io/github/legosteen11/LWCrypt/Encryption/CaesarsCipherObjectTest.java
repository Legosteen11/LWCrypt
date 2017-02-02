package io.github.legosteen11.LWCrypt.Encryption;

import org.junit.Before;

import static org.junit.Assert.*;

/**
 * Created by wouter on 2-2-17.
 */
public class CaesarsCipherObjectTest {
    CaesarsCipherObject caesarsCipherObjectPlain;
    CaesarsCipherObject caesarsCipherObjectCipher;
    public static final String TEST_STRING = "hoiikbenwouterenditismijntestberichtjeikhoopdatditwerktdatzouwelprettigzijneigenlijk";
    public static final int TEST_KEY = 25;
    
    @Before
    public void setUp() throws Exception {
        caesarsCipherObjectPlain = new CaesarsCipherObject();
        caesarsCipherObjectPlain.encrypt(TEST_STRING, TEST_KEY);
        caesarsCipherObjectCipher = new CaesarsCipherObject(caesarsCipherObjectPlain.getCipherText());
    }

    @org.junit.Test
    public void decrypt() throws Exception {
        assertEquals(TEST_STRING, caesarsCipherObjectPlain.decrypt(TEST_KEY));
        assertEquals(TEST_STRING, caesarsCipherObjectCipher.decrypt(TEST_KEY));
    }

    @org.junit.Test
    public void encrypt() throws Exception {
        assertEquals("gnhhjadmvntsdqdmchshrlhimsdrsadqhbgsidhjgnnoczschsvdqjsczsyntvdkoqdsshfyhimdhfdmkhij", caesarsCipherObjectPlain.encrypt(TEST_STRING, TEST_KEY));
        assertEquals("gnhhjadmvntsdqdmchshrlhimsdrsadqhbgsidhjgnnoczschsvdqjsczsyntvdkoqdsshfyhimdhfdmkhij", caesarsCipherObjectCipher.encrypt(TEST_STRING, TEST_KEY));
    }

}