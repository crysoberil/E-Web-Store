package com.ewebstore.entity;

/**
 * The InterBranchProductTransfer class is an entity encapsulating necessary
 * information on inter-branch product transfers.
 * 
 * @author ewebstore.com
 *
 */
public class InterBranchProductTransfer {
	private String transferID;
	private String fromBranchID;
	private String fromBranchName;
	private String toBranchID;
	private String toBranchName;
	private String productID;
	private String productName;
	private int quantity;

	public InterBranchProductTransfer(String transferID, String fromBranchID,
			String fromBranchName, String toBranchID, String toBranchName,
			String productID, String productName, int quantity) {
		this.transferID = transferID;
		this.fromBranchID = fromBranchID;
		this.fromBranchName = fromBranchName;
		this.toBranchID = toBranchID;
		this.toBranchName = toBranchName;
		this.productID = productID;
		this.productName = productName;
		this.quantity = quantity;
	}

	public String getTransferID() {
		return transferID;
	}

	public String getFromBranchID() {
		return fromBranchID;
	}

	public String getFromBranchName() {
		return fromBranchName;
	}

	public String getToBranchID() {
		return toBranchID;
	}

	public String getToBranchName() {
		return toBranchName;
	}

	public String getProductID() {
		return productID;
	}

	public String getProductName() {
		return productName;
	}

	public int getQuantity() {
		return quantity;
	}
}
