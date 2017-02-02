package io.github.legosteen11.LWCrypt.Encryption;

/**
 * Created by wouter on 2-2-17.
 */
public class Decrypted {
    private String algorithm;
    private String cipherText;
    private String plainText;
    private String key;
    private boolean decrypted;

    public Decrypted(String algorithm, String cipherText) {
        this.algorithm = algorithm;
        this.cipherText = cipherText;
        this.decrypted = false;
    }

    public Decrypted(String algorithm, String cipherText, String plainText, String key) {
        this.algorithm = algorithm;
        this.cipherText = cipherText;
        this.plainText = plainText;
        this.key = key;
        this.decrypted = true;
    }

    public String getAlgorithm() {
        return algorithm;
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

    public boolean isDecrypted() {
        return decrypted;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public void setPlainText(String plainText) {
        this.plainText = plainText;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setDecrypted(boolean decrypted) {
        this.decrypted = decrypted;
    }
}
