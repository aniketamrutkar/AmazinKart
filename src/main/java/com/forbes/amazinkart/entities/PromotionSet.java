package com.forbes.amazinkart.entities;

import java.util.ArrayList;

public class PromotionSet{
	private ArrayList<Promotion> promotionList;
	private String name;

	public ArrayList<Promotion> getPromotionList() {
		return promotionList;
	}

	public void setPromotionList(ArrayList<Promotion> promotionList) {
		this.promotionList = promotionList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
