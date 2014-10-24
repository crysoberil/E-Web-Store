package com.ewebstore.entity;

/**
 * The ProductTransferEntity class is an entity encapsulating necessary
 * information on product transfers.
 * 
 * @author ewebstore.com
 *
 */
public class ProductTransferEntity {
	private String inventoryTransferID;
	private String productID;
	private String productName;
	private String sourceBranchID;
	private String sourceBranchName;
	private String destinationBranchID;
	private String destinationBranchName;
	private int transferQuantity;

	public ProductTransferEntity(String inventoryTransferID, String productID,
			String productName, String sourceBranchID, String sourceBranchName,
			String destinationBranchID, String destinationBranchName,
			int transferQuantity) {
		this.inventoryTransferID = inventoryTransferID;
		this.productID = productID;
		this.productName = productName;
		this.sourceBranchID = sourceBranchID;
		this.sourceBranchName = sourceBranchName;
		this.destinationBranchID = destinationBranchID;
		this.destinationBranchName = destinationBranchName;
		this.transferQuantity = transferQuantity;
	}

	public String getInventoryTransferID() {
		return inventoryTransferID;
	}

	public String getProductID() {
		return productID;
	}

	public String getProductName() {
		return productName;
	}

	public String getSourceBranchID() {
		return sourceBranchID;
	}

	public String getSourceBranchName() {
		return sourceBranchName;
	}

	public String getDestinationBranchID() {
		return destinationBranchID;
	}

	public String getDestinationBranchName() {
		return destinationBranchName;
	}

	public int getTransferQuantity() {
		return transferQuantity;
	}
}
