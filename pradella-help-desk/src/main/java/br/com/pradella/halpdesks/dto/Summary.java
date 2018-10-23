package br.com.pradella.halpdesks.dto;

import java.io.Serializable;

public class Summary implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer amountNew;
	private Integer amountResolved;
	private Integer amountApproved;
	private Integer amountDisaproved;
	private Integer amountAssigned;
	private Integer amountClose;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Integer getAmountNew() {
		return amountNew;
	}
	public void setAmountNew(Integer amountNew) {
		this.amountNew = amountNew;
	}
	public Integer getAmountResolved() {
		return amountResolved;
	}
	public void setAmountResolved(Integer amountResolved) {
		this.amountResolved = amountResolved;
	}
	public Integer getAmountApproved() {
		return amountApproved;
	}
	public void setAmountApproved(Integer amountApproved) {
		this.amountApproved = amountApproved;
	}
	public Integer getAmountDisaproved() {
		return amountDisaproved;
	}
	public void setAmountDisaproved(Integer amountDisaproved) {
		this.amountDisaproved = amountDisaproved;
	}
	public Integer getAmountAssigned() {
		return amountAssigned;
	}
	public void setAmountAssigned(Integer amountAssigned) {
		this.amountAssigned = amountAssigned;
	}
	public Integer getAmountClose() {
		return amountClose;
	}
	public void setAmountClose(Integer amountClose) {
		this.amountClose = amountClose;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
