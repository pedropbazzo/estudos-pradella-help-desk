package br.com.pradella.halpdesks.enuns;

public enum StatusEnum {

	NEW, 
	ASSIGNED,
	RESOLVED,
	APPROVED,
	DISAPPROVED,
	CLOSED;
	
	public static StatusEnum getStatus(String status) {
		switch (status) {
		case "new": return NEW; 
		case "assigned": return ASSIGNED;
		case "resolved": return RESOLVED;
		case "approved": return APPROVED;
		case "disaproved": return DISAPPROVED;
		case "closed": return CLOSED;
		default: return NEW;
		}

	}
}
