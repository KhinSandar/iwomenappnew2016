package org.undp_iwomen.iwomen.model;

public class Download {
	private Boolean status;
	private Integer totalSize;
	private Integer finishedSize;
	private Integer downloadPercent;
	public Download(Boolean status, Integer totalSize, Integer finishedSize,
					Integer downloadPercent) {
		super();
		this.status = status;
		this.totalSize = totalSize;
		this.finishedSize = finishedSize;
		this.downloadPercent = downloadPercent;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public Integer getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(Integer totalSize) {
		this.totalSize = totalSize;
	}
	public Integer getFinishedSize() {
		return finishedSize;
	}
	public void setFinishedSize(Integer finishedSize) {
		this.finishedSize = finishedSize;
	}
	public Integer getDownloadPercent() {
		return downloadPercent;
	}
	public void setDownloadPercent(Integer downloadPercent) {
		this.downloadPercent = downloadPercent;
	}
	
	
	
}
