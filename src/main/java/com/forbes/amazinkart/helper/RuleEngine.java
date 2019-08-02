package com.forbes.amazinkart.helper;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.forbes.amazinkart.entities.Discount;
import com.forbes.amazinkart.entities.Product;
import com.forbes.amazinkart.entities.Promotion;
import com.forbes.amazinkart.entities.PromotionSet;

public class RuleEngine {

	public void applyPromotions(List<PromotionSet> promotionSetList, ArrayList<Product> productList) {
		for(PromotionSet eachPrmotionSet : promotionSetList) {
			for(Promotion eachPrmotion : eachPrmotionSet.getPromotionList()) {
				for(Product eachProduct: productList) {
					calculateFinalProductPrice(eachProduct, eachPrmotion, eachPrmotionSet.getName());
				}
			}
		}
	}
	
	
	public void calculateFinalProductPrice(Product eachProduct,Promotion eachPromotion, String promotionSetName) {
		try {
			Boolean isPromoApplied = true;
			for (Field field : eachPromotion.getClass().getDeclaredFields()) {
				field.setAccessible(true); 
				Object promotionAttribute = field.get(eachPromotion);
				if (promotionAttribute != null) {
					if(!(field.getName().equalsIgnoreCase("discount") || field.getName().equalsIgnoreCase("discountType"))) {
						Object productAttribute = new PropertyDescriptor(field.getName(), Product.class).getReadMethod().invoke(eachProduct);
						if(promotionAttribute instanceof HashMap) {
							HashMap promotionAttributeHm = (HashMap) promotionAttribute;
							isPromoApplied = isPromoApplied && assertCondition(promotionAttributeHm,((Number)productAttribute).doubleValue());
						}else if(promotionAttribute instanceof ArrayList) {
							ArrayList promotionAttributeHm = (ArrayList) promotionAttribute;
							isPromoApplied = isPromoApplied && assertCondition(promotionAttributeHm,((String)productAttribute));
						}
					}
				}
			}
			if(isPromoApplied)
				calculateDiscount(eachProduct,eachPromotion,promotionSetName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	public boolean assertCondition(HashMap promotionAttributeHm, Double productAttribute) {
		try {
			String operation = (String) promotionAttributeHm.get("operation");
			//Double value = ((Long)Long.parseLong(promotionAttributeHm.get("value").toString())).doubleValue();
			Double value = ((Number)promotionAttributeHm.get("value")).doubleValue();
			switch(operation){    
			case "equal":
				if(productAttribute.equals(value))
					return true;
				break;   
			case "lesser":
				if(productAttribute < value)
					return true;
				break;   
			case "greater":
				if(productAttribute > value)
					return true;
				break;	
			default:    
				return false;
			}    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean assertCondition(ArrayList promotionAttributeHm, String productAttribute) {
		try {
			if(promotionAttributeHm.contains(productAttribute)) {
				return true;
			}    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public void calculateDiscount(Product eachProduct,Promotion eachPromotion, String promotionSetName) {
		String discountType = eachPromotion.getDiscountType();
		Double discount = eachPromotion.getDiscount();
		Discount objDiscount = new Discount();
		if(discountType.equalsIgnoreCase("FLAT")){
			objDiscount.setAmount(discount);
			objDiscount.setDiscountTag("get Rs "+ discount + " off");
		}else if(discountType.equalsIgnoreCase("PERCENTAGE")){
			Double discountAmount = (discount*eachProduct.getPrice())/100;
			objDiscount.setAmount(discountAmount);
			objDiscount.setDiscountTag("get "+ String.format("%.0f", discount) + "% off");
		}
		if(eachProduct.getDiscount()==null) {
			eachProduct.setDiscount(objDiscount);
		}else{
			Discount existingDiscount = eachProduct.getDiscount();
			if(existingDiscount.getAmount() < objDiscount.getAmount() && !promotionSetName.equalsIgnoreCase("default")) {
				eachProduct.setDiscount(objDiscount);
			}
		}
	}
}
