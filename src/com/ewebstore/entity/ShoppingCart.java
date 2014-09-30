package com.ewebstore.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ShoppingCart {
	private HashMap<String, Integer> cartItems;
	private String customerID;
	private String customerName;

	public ShoppingCart(String customerID, String customerName) {
		this.customerID = customerID;
		this.customerName = customerName;
		cartItems = new HashMap<String, Integer>();
	}

	public void addToCart(String productID) {
		if (cartItems.containsKey(productID))
			cartItems.put(productID, cartItems.get(productID) + 1);
		else
			cartItems.put(productID, 1);
	}

	public void removeFromCart(String productID) {
		Integer count = cartItems.get(productID);

		if (count != null && count > 1)
			cartItems.put(productID, cartItems.get(productID) - 1);
		else
			cartItems.remove(productID);
	}

	public ArrayList<CartItem> getCartItems() {
		ArrayList<CartItem> cartItems = new ArrayList<CartItem>();

		for (Entry<String, Integer> cartItem : ((Map<String, Integer>) cartItems)
				.entrySet())
			cartItems.add(new CartItem(cartItem.getKey(), cartItem.getValue()));

		return cartItems;
	}
	
	public String getCustomerID() {
		return customerID;
	}
	
	public String getCustomerName() {
		return customerName;
	}
}
