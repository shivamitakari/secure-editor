package com.masterminds.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Cipher;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.masterminds.entity.FileInfo;

/**
 * 
 * @author DENNIS 
 * 			this class contains the all the methods to implement the
 *         operations on the pdf document from the name of the methods of the
 *         class you can easily find out what the method is for
 */
public class PdfSecurity {

	public Boolean saveDigitalSignature(String filePath, String data) {
		Boolean result = true;
		try {
			FileOutputStream fout = new FileOutputStream(filePath);
			ObjectOutputStream out = new ObjectOutputStream(fout);
			out.writeObject(data);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	public boolean isFileProtected(FileInfo fileInfo) {

		boolean flag = false;
		try {
			PdfReader pdfReader = new PdfReader(fileInfo.getFilePath());
			System.out.println(fileInfo.getFilePath());
		} catch (BadPasswordException badPasswordException) {
			flag = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out
					.println("Something wrong happened while reading the file");
			e.printStackTrace();
		}
		return flag;
	}

	public boolean validatePassword(FileInfo fileInfo) {

		boolean flag = true;
		try {

			PdfReader pdfReader = new PdfReader(fileInfo.getFilePath(),
					fileInfo.getPassword().getBytes());
		} catch (BadPasswordException badPasswordException) {
			flag = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out
					.println("Something wrong happened while reading the file kdkdkddkdkdkdk");
			e.printStackTrace();
		}
		return flag;
	}

	public FileInfo readFile(FileInfo fileInfo) {

		System.out.println("inside");
		// FileInfo newFileInfo=new FileInfo();
		try {
			PdfReader pdfReader = null;
			if (fileInfo.getPassword() != null) {
				pdfReader = new PdfReader(fileInfo.getFilePath(), fileInfo
						.getPassword().getBytes());
			} else {
				pdfReader = new PdfReader(fileInfo.getFilePath());
			}

			int noOfPages = pdfReader.getNumberOfPages();
			System.out.println(noOfPages);
			String s = "";
			for (int i = 1; i <= noOfPages; i++) {
				s += PdfTextExtractor.getTextFromPage(pdfReader, i);
				System.out.println(s);
			}
			System.out.println(s);
			fileInfo.setFileData(s);
			fileInfo.setFilePath(fileInfo.getFilePath());
			pdfReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return fileInfo;
	}

	public Boolean writeFile(FileInfo fileInfo) {

		try {

			OutputStream file = new FileOutputStream(new File(
					fileInfo.getFilePath()));
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, file);

			// TODO set this parameters from the fileInfo Object
			document.addAuthor("masterminds");
			document.addCreationDate();
			document.addTitle("Sample document");
			document.addSubject("masterminds subject");
			document.addKeywords("Pdf keywords");

			String pass = fileInfo.getPassword();
			System.out.println(fileInfo);
			int copyAndPrintEnable = 0;
			/*
			 * if (fileInfo.getIsPrintable().equals(true)) { copyAndPrintEnable
			 * = PdfWriter.ALLOW_PRINTING; }
			 */
			if (fileInfo.getisCopyEnabled() == false) {
				copyAndPrintEnable = copyAndPrintEnable | ~PdfWriter.ALLOW_COPY;
			}

			if (fileInfo.getPassword() != null) {
				writer.setEncryption(pass.getBytes(), pass.getBytes(),
						copyAndPrintEnable, PdfWriter.ENCRYPTION_AES_128);
			}
			document.open();
			document.add(new Paragraph(fileInfo.getFileData()));
			System.out.println(fileInfo);
			if (fileInfo.getExpiryDate() != null) {
				System.out.println("########################"
						+ fileInfo.getExpiryDate());
				Date expiryDate = fileInfo.getExpiryDate();
			//	Date expiryDate = getOldDate();
				long integer = expiryDate.getTime();
				// System.out.println(fileInfo);
				long time = new Date().getTime();
				String expDateToSaveInPdf = expiryDate.toString();
				
				writer.addJavaScript("var date=new Date();var time=date.getTime();if("
						+ integer
						+ "<time){app.alert('Ohhhh....!! This file is expired.The content of the file may not be valid.Please  check for the updated file. This file was valid till "
						+ expDateToSaveInPdf + " only.');this.closeDoc(true);}");
				System.out.println("today=" + time + "\tuser data=" + integer
						+ "!!!!!!!" + (time - integer));
			}

			document.close();
			file.close();
			System.out.println(fileInfo);
			System.out.println("done");
		} catch (Exception exception) {
			System.out.println("something wrong happened while genrating file");
			exception.printStackTrace();
			return false;
		}

		return true;
	}

	public Date getOldDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date d=null;
		try {
			d = sdf.parse("01-03-2014");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}
	public Date getNextDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date d=null;
		try {
			d = sdf.parse("05-03-2014");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	} 

	public Boolean isCopyEnabled(FileInfo fileInfo) {
		Boolean flag = true;
		try {
			PdfReader pdfReader = new PdfReader(fileInfo.getFilePath(),
					fileInfo.getPassword().getBytes());
			int permissions = pdfReader.getPermissions();
			if ((permissions & PdfWriter.ALLOW_COPY) == 0) {
				flag = false;
			}
			pdfReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	public Boolean isPrintable(FileInfo fileInfo) {
		Boolean flag = true;
		try {
			PdfReader pdfReader = new PdfReader(fileInfo.getFilePath(),
					fileInfo.getPassword().getBytes());
			int permissions = pdfReader.getPermissions();
			if ((permissions & PdfWriter.ALLOW_PRINTING) == 0) {
				flag = false;
			}
			pdfReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @param args
	 */
	public KeyPair genrateKeyPair(Integer keyLength) {
		// TODO Auto-generated method stub

		KeyPair keyPair = null;
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(keyLength);
			keyPair = generator.generateKeyPair();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return keyPair;
	}

	public void writeKeyInFile(Key key, String fileName) {
		try {
			FileOutputStream fout = new FileOutputStream(fileName);
			ObjectOutputStream out = new ObjectOutputStream(fout);
			out.writeObject(key);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Key readKeyFromFile(String fileName) {
		Key key = null;
		try {
			FileInputStream fin = new FileInputStream(fileName);
			@SuppressWarnings("resource")
			ObjectInputStream objectInputStream = new ObjectInputStream(fin);
			key = (Key) objectInputStream.readObject();
			return key;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return key;
	}

	public PrivateKey getPrivateKey(KeyPair keyPair) {
		PrivateKey privateKey = keyPair.getPrivate();
		return privateKey;
	}

	public PublicKey getPublicKey(KeyPair keyPair) {
		PublicKey publicKey = keyPair.getPublic();
		return publicKey;
	}

	public byte[] encryptData(Key key, String data) {
		byte[] cipherData =null;
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			cipherData = cipher.doFinal(data.getBytes());
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return cipherData ;
	}

	public String decryptData(Key key, byte[] data) {
		
		try {
			Cipher cipher2 = Cipher.getInstance("RSA");
			cipher2.init(Cipher.DECRYPT_MODE, key);
			return new String(cipher2.doFinal(data));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getDigitalSignature(String filePath) {
		String data = "data";
		try {
			FileInputStream fin = new FileInputStream(filePath);
			@SuppressWarnings("resource")
			ObjectInputStream objectInputStream = new ObjectInputStream(fin);
			data = (String) objectInputStream.readObject();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return data;
	}
}
