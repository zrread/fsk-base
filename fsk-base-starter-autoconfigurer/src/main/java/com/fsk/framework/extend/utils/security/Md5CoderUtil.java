package com.fsk.framework.extend.utils.security;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;


/**
 * Md5加密算法
 * 说明：包含直接md5加密，salt值加密，二次加密。
 */
public abstract class Md5CoderUtil {
	/**
	 * MD5密钥加密
	 * Param data	加密数据
	 * Return
	 */
	public static String getMD5(String data)
	{
		try {
			return getMD5(data.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return  "";
	}
	/**
	 * MD5密钥加密
	 * Param value	加密数据
	 * Return
	 */
	public static String getMD5(byte [] value)
	{
		byte[] encs;
		try 
		{
			encs = encrypt(value);
			return Hex.encodeHexString(encs);
		
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * MD5加密：直接加密
	 * 
	 * Param data	加密数据
	 * Return
	 * throws Exception
	 */
	private static byte[] encrypt(byte[] data) throws Exception {
		return encrypt(data, null, false);
	}

	/**
	 * MD5加密：定位直接加密
	 * Param data		加密数据
	 * Param offset	偏移量
	 * Param length	长度
	 * Return
	 * throws Exception
	 */
	public static byte[] encrypt(byte[] data,int offset,int length) throws Exception {
		return encrypt(data, null, false,offset,length);
	}
 
	/**
	 * MD5加密：是否做偏移加密
	 * Param data			加密数据
	 * Param isTwice		是否做偏移salt加密
	 * Return
	 * throws Exception
	 */
	public static byte[] encrypt(byte[] data, boolean isTwice) throws Exception {
		return encrypt(data, null, isTwice);
	}

	 
	/**
	 * MD5加密：salt加密
	 * Param data	加密数据
	 * Param salt	salt数据
	 * Return
	 * throws Exception
	 */
	private static byte[] encrypt(byte[] data, byte[] salt) throws Exception {
		return encrypt(data, salt, false);
	}

	/**
	 * MD5加密：是否做偏移salt加密
	 * Param data		加密数据
	 * Param salt		salt数据
	 * Param isTwice	是否做偏移salt加密
	 * Return
	 * throws Exception
	 */
	private static byte[] encrypt(byte[] data, byte[] salt, boolean isTwice)
			throws Exception {
		return encrypt(data, salt, isTwice,0,data.length);
	}
	
	/**
	 * MD5加密
	 * Param data		加密数据
	 * Param salt		salt数据
	 * Param isTwice	是否做偏移salt加密
	 * Param offset	偏移量
	 * Param length	长度
	 * Return
	 * throws Exception
	 */
	private static byte[] encrypt(byte[] data, byte[] salt, boolean isTwice,int offset,int length)
			throws Exception {
		MessageDigest md5 = DigestUtils.getMd5Digest();
		md5.update(data,offset,length);
		if ((salt != null) && (salt.length != 0)) {
			md5.update(salt); 
		}
		byte[] md5Bytes = md5.digest();
		if (isTwice) {
			md5.update(md5Bytes, 8, 8);
			byte[] md52 = md5.digest();
			System.arraycopy(md52, 8, md5Bytes, 8, 8);
		}
		return md5Bytes;
	}
	
	/**
	 * 获取MD5
	 * Param file
	 * Return
	 * throws FileNotFoundException
	 */
	public static String getMd5ByFile(File file) throws FileNotFoundException {  
        String value = null;  
        FileInputStream in = new FileInputStream(file);  
	    try {  
	        MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());  
	        MessageDigest md5 = MessageDigest.getInstance("MD5");  
	        md5.update(byteBuffer);  
	        BigInteger bi = new BigInteger(1, md5.digest());  
	        value = bi.toString(16);  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    } finally {  
	        if(null != in) {  
	            try {  
	                in.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            } 
	            in = null;
	        }  
	    }  
	    return value;  
    }  
}
