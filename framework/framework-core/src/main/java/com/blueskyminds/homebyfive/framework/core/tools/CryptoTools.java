package com.blueskyminds.homebyfive.framework.core.tools;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import sun.misc.BASE64Encoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Cryptography related mothods
 *
 * Date Started: 14/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class CryptoTools {

    private static final Log LOG = LogFactory.getLog(CryptoTools.class);
    private static final String SHA = "SHA";
    private static final String UTF_8 = "UTF-8";

    /**
     * Calculate the SHA hash of a string
     *
     * @param input     string to be hashed.  The string is converted to bytes by assuming its can be encoded in
     *   UTF-8.
     * @return          hashed result
     */
    public static String hashSHA(String input) throws CryptoException {
        String hash;

        try {
            MessageDigest messageDigest = MessageDigest.getInstance(SHA);
            messageDigest.update(input.getBytes(UTF_8));

            byte[] raw = messageDigest.digest();
            hash = (new BASE64Encoder()).encode(raw);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        } catch (UnsupportedEncodingException e) {
            throw new CryptoException(e);
        }

        return hash;
    }

    // ------------------------------------------------------------------------------------------------------

    public static void main(String[] args) {
        System.out.println("SHA tool.");
        System.out.print("Enter the string to hash: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            String inputString = reader.readLine();

            if (inputString != null) {
                String outputString = hashSHA(inputString);
               System.out.println("Result:"+outputString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CryptoException e) {
            e.printStackTrace();
        }

    }
}
