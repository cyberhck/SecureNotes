package com.fossdevs.securenotes;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.util.Base64;

public class SHA1 {
	public String SHAHash;
	public SHA1(String text){
		MessageDigest mdSha1 = null;
		try {
			mdSha1 = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
		}
		try {
			mdSha1.update(text.getBytes("ASCII"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
		}
		byte[] data = mdSha1.digest();
        String SHAHash=convertToHex(data);
        this.SHAHash=SHAHash;
	}

	private String convertToHex(byte[] data) {
		// TODO Auto-generated method stub
		int NO_OPTIONS=0;
		StringBuffer sb = new StringBuffer();
        String hex=null;
        hex=Base64.encodeToString(data, 0, data.length, NO_OPTIONS);
        sb.append(hex);
        return sb.toString();
	}
}
