package com.ewebstore.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class ShoppingCart {
	private HashMap<String, Integer> cartItems;
	private String customerID = null;
	private String customerName = null;

	public ShoppingCart(String customerID, String customerName) {
		this.customerID = customerID;
		this.customerName = customerName;
		cartItems = new HashMap<String, Integer>();
	}

	public ShoppingCart() {
		cartItems = new HashMap<String, Integer>();
	}

	public void addToCart(String productID) {
		Integer count = cartItems.get(productID);

		if (count != null)
			cartItems.put(productID, count + 1);
		else
			cartItems.put(productID, 1);
	}

	public void removeFromCart(String productID) {
		Integer count = cartItems.get(productID);

		if (count != null && count > 1)
			cartItems.put(productID, count - 1);
		else
			cartItems.remove(productID);
	}

	public void removeProduct(String productID) {
		cartItems.remove(productID);
	}

	public ArrayList<CartItem> getCartItems() {
		ArrayList<CartItem> cartItems = new ArrayList<CartItem>();

		for (Entry<String, Integer> cartItem : this.cartItems.entrySet())
			cartItems.add(new CartItem(cartItem.getKey(), cartItem.getValue()));

		return cartItems;
	}
	
	public boolean isEmpty() {
		return cartItems.isEmpty();
	}

	public String getCustomerID() {
		return customerID;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("Customer ID = " + getCustomerID()).append("\n");
		builder.append("Customer name = " + getCustomerName()).append("\nCart Items:\n");
		
		ArrayList<CartItem> cartItems = getCartItems();
		
		for (CartItem cartItem : cartItems)
			builder.append(cartItem.toString()).append("\n");
		
		return builder.toString();
	}
}
