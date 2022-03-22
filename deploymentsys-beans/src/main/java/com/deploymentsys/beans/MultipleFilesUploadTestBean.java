package com.deploymentsys.beans;

public class MultipleFilesUploadTestBean extends BaseBean {
	private int id;
	private String fileLocalPath;

	public MultipleFilesUploadTestBean() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFileLocalPath() {
		return fileLocalPath;
	}

	public void setFileLocalPath(String fileLocalPath) {
		this.fileLocalPath = fileLocalPath;
	}

}
