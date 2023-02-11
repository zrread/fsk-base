package com.fsk.framework.extend.utils.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具
 * 
 * @author xiel
 * @date 2019年1月17日
 * 
 */
public class MD5Tool {

	private MD5Tool() {
	}

	public static boolean toMD5(String white, String black) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(white.getBytes("UTF8"));
			byte[] byteDigest = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < byteDigest.length; offset++) {
				i = byteDigest[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			// 32位加密
			white = buf.toString();
			if (black.equals(white)) {
				return true;
			} else {
				return false;
			}
			// 16位的加密
			// return buf.toString().substring(8, 24);
		} catch (NoSuchAlgorithmException e) {
			return false;
		} catch (UnsupportedEncodingException e) {
			return false;
		}

	}
	
	public static String getMD5(String data){
		StringBuffer buf = new StringBuffer("");
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");//【runqian 2017-07-17】sonar 空引用 bug 修复
			
			try {
				md.update(data.getBytes("UTF8"));
			} catch (UnsupportedEncodingException e) {
			}
			
			byte[] byteDigest=null;
			if(md!=null){
				byteDigest = md.digest();
			}
			
			int i;
			if(byteDigest!=null && byteDigest.length>0){
				for (int offset = 0; offset < byteDigest.length; offset++) {
					i = byteDigest[offset];
					if (i < 0)
						i += 256;
					if (i < 16)
						buf.append("0");
					buf.append(Integer.toHexString(i));
				}
			}
		} catch (NoSuchAlgorithmException e) {
		}
		
		// 32位加密
		return buf.toString();
	}

}
