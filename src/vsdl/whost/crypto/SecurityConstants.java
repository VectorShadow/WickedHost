package vsdl.whost.crypto;

import vsdl.datavector.crypto.CryptoUtilities;

public class SecurityConstants {
    public static final int SALT_LENGTH = 8;


    public static String generateSalt() {
        return CryptoUtilities.randomAlphaNumericString(SecurityConstants.SALT_LENGTH);
    }
}
