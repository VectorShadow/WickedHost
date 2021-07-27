package vsdl.whost.data.provider;

import vsdl.datavector.crypto.CryptoUtilities;
import vsdl.wl.elements.SecurityConstants;

public class DataProvider {
    public boolean queryAccount(String username) {
        return false;
    }

    public boolean loginAccount(String username, String hashedPassword) {
        return false;
    }

    public void createAccount(String username, String decryptedPassword) {
        final String SALT = CryptoUtilities.randomAlphaNumericString(SecurityConstants.SALT_LENGTH);
        final String HASHED_PASS = CryptoUtilities.hash(CryptoUtilities.salt(decryptedPassword, SALT));
        System.out.println("SALT = " + SALT + " HASHED = " + HASHED_PASS);
    }
}
