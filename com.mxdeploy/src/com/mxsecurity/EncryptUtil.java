package com.mxsecurity;

import javax.crypto.SecretKeyFactory;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.DESKeySpec;

public class EncryptUtil {
  private static String encryptionAlgorithm = "DES";
  private static String encryptionMode = "ECB";
  private static String encryptionPadding = "PKCS5Padding";
  //private static String encryptionkey = SystemProperties.getInstance().getEncryptionkey();

  private EncryptUtil() {
  }
    
  public static String decryptText(String encryptedText) {
	  return decryptText(encryptedText,"41254157g5o9s7h8m3e612awpmon!5jk");
  }
  
  public static String decryptText(String encryptedText,String encryptionkey) {
    String cipherParameters = encryptionAlgorithm + "/" + encryptionMode + "/" +  encryptionPadding;

    sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
    try {
      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(encryptionAlgorithm);
      byte[] desKeyData = encryptionkey.getBytes();
      DESKeySpec desKeySpec = new DESKeySpec(desKeyData);
      SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

      byte decodedEncryptedText[] = decoder.decodeBuffer(encryptedText);
      Cipher c1 = Cipher.getInstance(cipherParameters);
      c1.init(c1.DECRYPT_MODE, secretKey);
      byte[] decryptedText = c1.doFinal(decodedEncryptedText);
      String decryptedTextString = new String(decryptedText);
      return decryptedTextString;
    }
    catch (Exception e) {
      System.out.println("Error: " + e);
      System.out.println("encryptedText:" + encryptedText);
      e.printStackTrace();
      return null;
    }
  }
  
  public static String encryptText(String text) {
	  return encryptText(text, "41254157g5o9s7h8m3e612awpmon!5jk");
  }
  
  public static String encryptText(String text,String encryptionkey) {
    String cipherParameters = encryptionAlgorithm + "/" + encryptionMode + "/" +  encryptionPadding;

    sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
    try {
      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(encryptionAlgorithm);
      byte[] desKeyData = encryptionkey.getBytes();
      DESKeySpec desKeySpec = new DESKeySpec(desKeyData);
      SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

      Cipher c1 = Cipher.getInstance(cipherParameters);
      if (secretKey != null) {
        c1.init(c1.ENCRYPT_MODE, secretKey);
        byte clearTextBytes[];
        clearTextBytes = text.getBytes();
        byte encryptedText[] = c1.doFinal(clearTextBytes);
        String encryptedEncodedText = encoder.encode(encryptedText);
        return encryptedEncodedText;

      } else {
         System.out.println("ERROR! >> SecretKey not generated ....");
         System.out.println("ERROR! >> you are REQUIRED to specify the encryptionKey in the config xml");
         return null;
      }
    }
    catch (Exception e) {
      System.out.println("Error: " + e);
      System.out.println("encryptedText:" + text);
      e.printStackTrace();
      return null;
    }

  }

  public static void main(String[] args) {

    //System.out.println("Key Size:"+encryptionkey.length());

    String key = "1TZNrEBwrVPu81ualKM5aw==";
    String encoder = EncryptUtil.encryptText(key);
    //String decoder = EncryptUtil.decryptText("");
    String decoder = EncryptUtil.decryptText(key);

    System.out.println("key:"+key);
    System.out.println("encoder:"+encoder);
    System.out.println("decoder:"+decoder);
  }

}


