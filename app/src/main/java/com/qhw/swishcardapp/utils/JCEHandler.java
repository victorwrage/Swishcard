package com.qhw.swishcardapp.utils;

import com.socks.library.KLog;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * @author dj
 * 
 */
public class JCEHandler {

	static final String ALG_DES = "DES";
	static final String ALG_TRIPLE_DES = "DESede";
	static final String DES_MODE_ECB = "ECB";
	static final String DES_MODE_CBC = "CBC";
	static final String DES_NO_PADDING = "NoPadding";

	public byte[] encryptDESKey(short keyLength, Key clearDESKey,
			Key encryptingKey) {
		byte[] encryptedDESKey = null;
		byte[] clearKeyBytes = extractDESKeyMaterial(keyLength, clearDESKey);
		// enforce correct (odd) parity before encrypting the key
		adjustDESParity(clearKeyBytes);
		encryptedDESKey = doCryptStuff(clearKeyBytes, encryptingKey,
				Cipher.ENCRYPT_MODE);
		return encryptedDESKey;
	}

	/**
	 * Extracts the DES/DESede key material
	 * 
	 * @param keyLength
	 *            bit length (key size) of the DES key. (LENGTH_DES,
	 *            LENGTH_DES3_2KEY or LENGTH_DES3_3KEY)
	 * @param clearDESKey
	 *            DES/Triple-DES key whose format is "RAW"
	 * @return encoded key material
	 * @throws
	 */
	protected byte[] extractDESKeyMaterial(short keyLength, Key clearDESKey) {
		byte[] clearKeyBytes = null;
		String keyAlg = clearDESKey.getAlgorithm();
		String keyFormat = clearDESKey.getFormat();
		if (keyFormat.compareTo("RAW") != 0) {
		}
		if (!keyAlg.startsWith(ALG_DES)) {
		}
		clearKeyBytes = clearDESKey.getEncoded();
		clearKeyBytes = trim(clearKeyBytes, getBytesLength(keyLength));
		return clearKeyBytes;
	}

	/**
	 * Forms the clear DES key given its "RAW" encoded bytes Does the inverse of
	 * extractDESKeyMaterial
	 * 
	 * @param keyLength
	 *            bit length (key size) of the DES key. (LENGTH_DES,
	 *            LENGTH_DES3_2KEY or LENGTH_DES3_3KEY)
	 * @param clearKeyBytes
	 *            the RAW DES/Triple-DES key
	 * @return clear key
	 * @throws
	 */
	public static Key formDESKey(short keyLength, byte[] clearKeyBytes) {
		Key key = null;
		switch (keyLength) {
		case 64: {
			key = new SecretKeySpec(clearKeyBytes, ALG_DES);
		}
			break;
		case 128: {
			// make it 3 components to work with JCE
			clearKeyBytes = concat(clearKeyBytes, 0,
					getBytesLength(Short.parseShort(128 + "")), clearKeyBytes,
					0, getBytesLength(Short.parseShort(64 + "")));
		}
		case 192: {
			key = new SecretKeySpec(clearKeyBytes, ALG_TRIPLE_DES);
		}
		}
		if (key == null) {
		}
		return key;
	}

	public static String encryptData(String data, String key) {
		;
		return Utils.bcd2Str(encryptData(Utils.str2Bcd(data),
				Utils.str2Bcd(key)));
	}

	/**
	 * Encrypts data
	 * 
	 * @param data
	 * @param key
	 * @return encrypted data
	 * @exception
	 */
	public static byte[] encryptData(byte[] data, byte[] key) {
		int len = key.length * 8;
		Key keay;
		byte[] encryptedData = null;
		try {
			keay = formDESKey(Short.parseShort(len + ""), key);
			encryptedData = doCryptStuff(data, keay, Cipher.ENCRYPT_MODE);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return encryptedData;
	}

	static public String decryptData(String encryptedData, String key) {
		return Utils.bcd2Str(decryptData(Utils.str2Bcd(encryptedData),
				Utils.str2Bcd(key)));

	}

	/**
	 * Decrypts data
	 * 
	 * @param encryptedData
	 * @param key
	 * @return clear data
	 * @exception
	 */
	static public byte[] decryptData(byte[] encryptedData, byte[] key) {
		byte[] clearData = null;
		int len = key.length * 8;
		Key keay = formDESKey(Short.parseShort(len + ""), key);
		clearData = doCryptStuff(encryptedData, keay, Cipher.DECRYPT_MODE);
		return clearData;
	}

	/**
	 * Performs cryptographic DES operations (encryption/decryption) in ECB mode
	 * using JCE Cipher
	 * 
	 * @param data
	 * @param key
	 * @param cipherMode
	 *            Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE
	 * @return result of the cryptographic operations
	 * @throws
	 */
	static byte[] doCryptStuff(byte[] data, Key key, int cipherMode) {
		return doCryptStuff(data, key, cipherMode, DES_MODE_ECB, null);
	}

	/**
	 * performs cryptographic operations (encryption/decryption) using JCE
	 * Cipher
	 * 
	 * @param data
	 * @param key
	 * @param CipherMode
	 *            Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE
	 * @return result of the cryptographic operations
	 * @throws
	 */
	static byte[] doCryptStuff(byte[] data, Key key, int CipherMode,
			String desMode, byte[] iv) {
		byte[] result = null;
		String transformation;
		if (key.getAlgorithm().startsWith(ALG_DES)) {
			transformation = key.getAlgorithm() + "/" + desMode + "/"
					+ DES_NO_PADDING;
		} else {
			transformation = key.getAlgorithm();
		}
		AlgorithmParameterSpec aps = null;
		try {
			Cipher c1 = Cipher.getInstance(transformation);
			if (DES_MODE_CBC.equals(desMode)) {
				aps = new IvParameterSpec(iv);
			}
			c1.init(CipherMode, key, aps);
			result = c1.doFinal(data);
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * Calculates the length of key in bytes
	 * 
	 * @param keyLength
	 *            bit length (key size) of the DES key. (LENGTH_DES,
	 *            LENGTH_DES3_2KEY or LENGTH_DES3_3KEY)
	 * @return keyLength/8
	 * @throws
	 *             if unknown key length
	 */
	static int getBytesLength(short keyLength) {
		int bytesLength = 0;
		switch (keyLength) {
		case 64:
			bytesLength = 8;
			break;
		case 128:
			bytesLength = 16;
			break;
		case 192:
			bytesLength = 24;
			break;
		default:
		}
		return bytesLength;
	}

	/**
	 * 二磁道加密
	 * 
	 * @param track2Data
	 * @param tdk
	 * @return
	 */
	public static String encryptTrack2Data(String track2Data, String tdk) {
		int length = track2Data.length();
		track2Data = track2Data.replace('=', 'D');
		track2Data += track2Data.length() % 2 == 0 ? "" : "0";
		String temp = encryptData(
				track2Data.substring(track2Data.length() - 18,
						track2Data.length() - 2), tdk);
		char[] chars = track2Data.toCharArray();
		System.arraycopy(temp.toCharArray(), 0, chars, chars.length - 18, 16);
		String _track2Data = new String(chars);
		return _track2Data.substring(0, length);
	}

	/**
	 * 芯片卡二磁道加密
	 *
	 * @param track2Data
	 * @param tdk
	 * @return
	 */
	public static String encryptTrack2Data2(String track2Data, String tdk) {
		int length = track2Data.length();
		KLog.v("encryptTrack2Data2--"+length +"--track2Data--"+track2Data+"--tdk--"+tdk);
	//	track2Data = track2Data.replace('=', 'D');
	//	track2Data += track2Data.length() % 2 == 0 ? "" : "0";
		String temp = encryptData(
				track2Data.substring(track2Data.length() - 16,
						track2Data.length() - 2), tdk);
		char[] chars = track2Data.toCharArray();
		System.arraycopy(temp.toCharArray(), 0, chars, chars.length - 16, 16);
		String _track2Data = new String(chars);
		return _track2Data.substring(0, length);
	}

	/**
	 * 二磁道解密
	 * 
	 * @param track2Data
	 * @param tdk
	 * @return
	 */
	public static String decryptTrack2Data(String track2Data, String tdk) {
		int length = track2Data.length();
		track2Data = track2Data.replace('=', 'D');
		track2Data += track2Data.length() % 2 == 0 ? "" : "0";
		String temp = decryptData(
				track2Data.substring(track2Data.length() - 18,
						track2Data.length() - 2), tdk);
		char[] chars = track2Data.toCharArray();
		System.arraycopy(temp.toCharArray(), 0, chars, chars.length - 18, 16);
		String _track2Data = new String(chars);
		return _track2Data.substring(0, length);
	}

	/**
	 * Trims a byte[] to a certain length
	 * 
	 * @param array
	 *            the byte[] to be trimmed
	 * @param length
	 *            the wanted length
	 * @return the trimmed byte[]
	 */
	public static byte[] trim(byte[] array, int length) {
		byte[] trimmedArray = new byte[length];
		System.arraycopy(array, 0, trimmedArray, 0, length);
		return trimmedArray;
	}

	public static byte[] concat(byte[] array1, int beginIndex1, int length1,
			byte[] array2, int beginIndex2, int length2) {
		byte[] concatArray = new byte[length1 + length2];
		System.arraycopy(array1, beginIndex1, concatArray, 0, length1);
		System.arraycopy(array2, beginIndex2, concatArray, length1, length2);
		return concatArray;
	}

	public static void adjustDESParity(byte[] bytes) {
		for (int i = 0; i < bytes.length; i++) {
			int b = bytes[i];
			bytes[i] = (byte) (b & 0xfe | (b >> 1 ^ b >> 2 ^ b >> 3 ^ b >> 4
					^ b >> 5 ^ b >> 6 ^ b >> 7 ^ 0x01) & 0x01);
		}
	}
}
