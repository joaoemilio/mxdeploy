package com.mxsecurity.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public final class PWSec {  
     private static SecretKey skey;  
     private static KeySpec ks;  
     private static PBEParameterSpec ps;  
     private static final String algorithm = "PBEWithMD5AndDES";  
     private static BASE64Encoder enc = new BASE64Encoder();  
     private static BASE64Decoder dec = new BASE64Decoder();  
     static {  
         try {  
             SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);  
             ps = new PBEParameterSpec (new byte[]{3,1,4,1,5,9,2,6}, 20);  
    
             ks = new PBEKeySpec ("41254157g5o9s7h8m3e612awpmon!5jk".toCharArray()); // esta � a chave que voc� quer manter secreta.  
             // Obviamente quando voc� for implantar na sua empresa, use alguma outra coisa - por exemplo,  
             // "05Bc5hswRWpwp1sew+MSoHcj28rQ0MK8". Nao use caracteres especiais (como �) para nao dar problemas.  
             
             skey = skf.generateSecret (ks);  
         } catch (java.security.NoSuchAlgorithmException ex) {  
             ex.printStackTrace();  
         } catch (java.security.spec.InvalidKeySpecException ex) {  
             ex.printStackTrace();  
         }  
     }  
     
     public static final String encrypt(final String text)  
         throws  
         BadPaddingException,  
         NoSuchPaddingException,  
         IllegalBlockSizeException,  
         InvalidKeyException,  
         NoSuchAlgorithmException,  
         InvalidAlgorithmParameterException {  
               
         final Cipher cipher = Cipher.getInstance(algorithm);  
         cipher.init(Cipher.ENCRYPT_MODE, skey, ps);  
         return enc.encode (cipher.doFinal(text.getBytes()));  
     }  
     
     public static final String decrypt(final String text)  
         throws  
         BadPaddingException,  
         NoSuchPaddingException,  
         IllegalBlockSizeException,  
         InvalidKeyException,  
         NoSuchAlgorithmException,  
         InvalidAlgorithmParameterException {  
               
         final Cipher cipher = Cipher.getInstance(algorithm);  
         cipher.init(Cipher.DECRYPT_MODE, skey, ps);  
         String ret = null;  
         try {  
             ret = new String(cipher.doFinal(dec.decodeBuffer (text)));  
         } catch (Exception ex) {  
         }  
         return ret;  
     }  
     
     public static void main(String[] args) throws Exception {  
    	 String password = "1TZNrEBwrVPu81ualKM5aw=="; 
    	 
    	 if(args.length>0){ 
    		 password=null;
    		 if(args[0].equals("-h")){
    		     System.out.println("-d <password encrypted>");
    		     System.out.println("-c <password decrypted>");
    		 } else if(args[0].equals("-e")){
    			 password=args[1];
    	         String encoded = PWSec.encrypt(password);  
    	         System.out.println ("Password Encrypted :"+encoded);  
    	         System.out.println ("Password Decrypted :"+PWSec.decrypt(encoded));    			 
    		 } else if(args[0].equals("-d")){
    			 password=args[1];
    	         String encoded = PWSec.decrypt(password);  
    	         System.out.println ("Password Decrypted :"+encoded);  
    	         System.out.println ("Password Encrypted :"+PWSec.decrypt(encoded));    			 
    		 } else {
    			 password=args[0];
    	         String encoded = PWSec.encrypt(password);  
    	         System.out.println ("Password Encrypted :"+encoded);  
    	         System.out.println ("Password Decrypted :"+PWSec.decrypt(encoded));   			 
    		 }
    	 } else {
    		 String encoded = PWSec.decrypt(password);  
    		 System.out.println (encoded);  
    		 System.out.println (PWSec.encrypt (encoded));
    	 }
     }  
 }  
