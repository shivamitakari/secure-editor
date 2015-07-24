package com.masterminds.entity;

import java.util.Date;

public class FileInfo {
	private String filePath;
	private Boolean isCopyEnabled;
	private Boolean isPrintable;
	private String fileData;
	private String password;
	private Date expiryDate;

	@Override
	public String toString() {
		return "FileInfo [filePath=" + filePath + ", isCopyProtected="
				+ isCopyEnabled + ", isPrintable=" + isPrintable
				+ ", fileData=" + fileData + ", password=" + password
				+ ", expiryDate=" + expiryDate + "]";
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Boolean getisCopyEnabled() {
		return isCopyEnabled;
	}

	public void setisCopyEnabled(Boolean isCopyEnabled) {
		this.isCopyEnabled = isCopyEnabled;
	}

	public Boolean getIsPrintable() {
		return isPrintable;
	}

	public void setIsPrintable(Boolean isPrintable) {
		this.isPrintable = isPrintable;
	}

	public String getFileData() {
		return fileData;
	}

	public void setFileData(String fileData) {
		this.fileData = fileData;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
}
