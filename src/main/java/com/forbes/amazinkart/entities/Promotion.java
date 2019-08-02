package com.forbes.amazinkart.entities;

import java.util.ArrayList;
import java.util.HashMap;

public class Promotion {
	private double discount;
	private String discountType;
	private HashMap<String,Object> price;
	private HashMap<String,Object> rating;
	private ArrayList<String> origin;
	private ArrayList<String> category;
	private HashMap<String,Object> inventory;
	private ArrayList<String> arrival;


	public HashMap<String, Object> getPrice() {
		return price;
	}

	public void setPrice(HashMap<String, Object> price) {
		this.price = price;
	}

	public HashMap<String, Object> getRating() {
		return rating;
	}

	public void setRating(HashMap<String, Object> rating) {
		this.rating = rating;
	}

	public HashMap<String, Object> getInventory() {
		return inventory;
	}

	public void setInventory(HashMap<String, Object> inventory) {
		this.inventory = inventory;
	}

	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public ArrayList<String> getCategory() {
		return category;
	}

	public void setCategory(ArrayList<String> category) {
		this.category = category;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}



	public ArrayList<String> getOrigin() {
		return origin;
	}

	public void setOrigin(ArrayList<String> origin) {
		this.origin = origin;
	}

	public ArrayList<String> getArrival() {
		return arrival;
	}

	public void setArrival(ArrayList<String> arrival) {
		this.arrival = arrival;
	}
}
