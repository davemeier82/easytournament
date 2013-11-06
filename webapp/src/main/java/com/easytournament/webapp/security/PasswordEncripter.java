package com.easytournament.webapp.security;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;

import java.security.NoSuchAlgorithmException;

/**
 * Encryptor Verschlüsselt ein Passwort per Hash
 * 
 */
public class PasswordEncripter {

  /**
   * Hash-Algorithmus
   */
  private static final String ALGORITHM = "SHA-512";

  /**
   * Verschlüsselt ein Passwort per Hash
   * @param password
   * @return
   * @throws NoSuchAlgorithmException
   */
  public static String encript(String password, byte[] salt) throws NoSuchAlgorithmException {
    MessageDigest md = null;
    md = MessageDigest.getInstance(ALGORITHM);
    md.reset();
    md.update(salt);
    return Base64.encodeBase64String(md.digest(password.getBytes()));
  }

}
