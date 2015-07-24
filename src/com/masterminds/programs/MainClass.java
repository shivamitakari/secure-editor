package com.masterminds.programs;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class MainClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(723);
			KeyPair keyPair = generator.generateKeyPair();
			PrivateKey privateKey = keyPair.getPrivate();
			PublicKey publicKey = keyPair.getPublic();
			writeKeyInFile(publicKey, "D://publicKey.pub");
			writeKeyInFile(privateKey, "D://privateKey.pri");
			Key key2 = readKeyFromFile("D://publicKey.pub");
			Key key1 = readKeyFromFile("D://privateKey.pri");
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, key1);
			
			byte[] cipherData = cipher.doFinal("shiva".getBytes());
			String s = new String(cipherData);
			Cipher cipher2 = Cipher.getInstance("RSA");
			cipher2.init(Cipher.DECRYPT_MODE, key2);
			byte[] deCipherData = cipher2.doFinal(s.getBytes());
			System.out.println("\nDec of data is done\t"+new String(deCipherData));
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void writeKeyInFile(Key key, String fileName) {
		try {
			FileOutputStream fout = new FileOutputStream(fileName);
			ObjectOutputStream out = new ObjectOutputStream(fout);
			out.writeObject(key);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Key readKeyFromFile(String fileName) {
		Key key = null;
		try {
			FileInputStream fin = new FileInputStream(fileName);
			ObjectInputStream objectInputStream = new ObjectInputStream(fin);
			key = (Key) objectInputStream.readObject();
			return key;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return null;
	}
}
