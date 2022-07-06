package com.nft.common.googleauth;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import cn.hutool.core.codec.Base32;
import cn.hutool.core.codec.Base64;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class GoogleAuthenticator {

	// 生成的key长度( Generate secret key length)
	public static final int SECRET_SIZE = 10;

	public static final String SEED = "22150146801713967E8g";
	// Java实现随机数算法
	public static final String RANDOM_NUMBER_ALGORITHM = "SHA1PRNG";
	// 最多可偏移的时间
	static int window_size = 3; // default 3 - max 17

	public static String generateSecretKey() {
		SecureRandom sr = null;
		try {
			sr = SecureRandom.getInstance(RANDOM_NUMBER_ALGORITHM);
			sr.setSeed(Base64.decode(SEED));
			byte[] buffer = sr.generateSeed(SECRET_SIZE);
			String encodedKey = Base32.encode(buffer);
			return encodedKey;
		} catch (NoSuchAlgorithmException e) {
			// should never occur... configuration error
		}
		return null;
	}

	/**
	 * 根据user和secret生成二维码的密钥
	 *
	 * @param user
	 * @param host
	 * @param secret
	 * @return
	 */
	public static String getQRBarcodeURL(String user, String host, String secret) {
		String format = "http://www.google.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=otpauth://totp/%s@%s?secret=%s";
		return String.format(format, user, host, secret);
	}

	/**
	 * 这个format不可以修改，身份验证器无法识别二维码
	 *
	 * @param user
	 * @param secret
	 * @return
	 */
	public static String getQRBarcode(String user, String secret) {
		String format = "otpauth://totp/%s?secret=%s";
		return String.format(format, user, secret);
	}

	public static boolean checkCode(String secret, String code, long timeMsec) {
		byte[] decodedKey = Base32.decode(secret);
		// convert unix msec time into a 30 second "window"
		// this is per the TOTP spec (see the RFC for details)
		long t = (timeMsec / 1000L) / 30L;
		// Window is used to check codes generated in the near past.
		// You can use this value to tune how far you're willing to go.
		for (int i = -window_size; i <= window_size; ++i) {
			long hash;
			try {
				hash = verify_code(decodedKey, t + i);
			} catch (Exception e) {
				// Yes, this is bad form - but
				// the exceptions thrown would be rare and a static
				// configuration problem
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
				// return false;
			}
			if (code.equals(addZero(hash))) {
				return true;
			}
			/*
			 * if (code==hash ) { return true; }
			 */
		}
		// The validation code is invalid.
		return false;
	}

	private static int verify_code(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {
		byte[] data = new byte[8];
		long value = t;
		for (int i = 8; i-- > 0; value >>>= 8) {
			data[i] = (byte) value;
		}
		SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(signKey);
		byte[] hash = mac.doFinal(data);
		int offset = hash[20 - 1] & 0xF;
		// We're using a long because Java hasn't got unsigned int.
		long truncatedHash = 0;
		for (int i = 0; i < 4; ++i) {
			truncatedHash <<= 8;
			// We are dealing with signed bytes:
			// we just keep the first byte.
			truncatedHash |= (hash[offset + i] & 0xFF);
		}
		truncatedHash &= 0x7FFFFFFF;
		truncatedHash %= 1000000;
		return (int) truncatedHash;
	}

	/*
	 * private String addZero ( long code ) { System.out.println ( "addZero code:" +
	 * code ); String codeString = String.valueOf ( code ); System.out.println (
	 * "addZero codeString" + codeString ); int codeLength = codeString.length ();
	 * StringBuffer sb = new StringBuffer ( codeString ); if (codeLength < 6) { for
	 * (int i = 0; i < (6 - codeLength); i++) { sb.insert ( 0 , "0" ); } } return
	 * sb.toString (); }
	 */

	private static String addZero(long code) {
		return String.format("%06d", code);
	}

}
