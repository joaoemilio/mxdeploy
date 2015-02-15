package com.mxdeploy;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.mxsecurity.util.PWSec;

public class PWSec2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println( System.getProperty("os.name") );
		try {
			System.out.println(PWSec.decrypt("FfMQxNc48b/hqDWyLqoYEA=="));
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
