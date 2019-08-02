package com.forbes.amazinkart;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jackson.util.DefaultPrettyPrinter;

import com.forbes.amazinkart.entities.Forex;
import com.forbes.amazinkart.entities.Product;
import com.forbes.amazinkart.entities.PromotionSet;
import com.forbes.amazinkart.helper.HttpHelper;
import com.forbes.amazinkart.helper.RuleEngine;

public class AmazinKart 
{
	Properties prop = null;

	public static void main(String[] args) {

		AmazinKart amazinKart = new AmazinKart();
		String promotionSetToApply = "default";
		if(args.length > 0) {
			promotionSetToApply = args[0];
			System.out.println("Starting Amazin Kart Application. Promotion Set to Apply : "+promotionSetToApply);
		}else {
			System.out.println("Starting Amazin Kart Application. No PrmotionSet sent in arguments. Only default propmotions will be applied");
		}
		amazinKart.getProperties();
		amazinKart.start(promotionSetToApply);
		System.out.println("Completed Amazin Kart Application !!!");
	}


	public void start(String promotionSetToApply){

		try {
			// Get PromotionSets
			List<PromotionSet> promotionSetList = getPromotions(promotionSetToApply);

			ArrayList<Product> productList = getProductList();

			// Apply Promotions
			RuleEngine ruleEngine = new RuleEngine();
			ruleEngine.applyPromotions(promotionSetList, productList);

			//Write the output
			ObjectMapper mapper = new ObjectMapper();
			ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());

			String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(productList);
			System.out.println("Final product list : ");
			System.out.println(indented);

			if(prop.get("outputFilePath").equals("")) {
				System.out.println("Output is written to the "+this.getClass().getResource("/output.json"));
				writer.writeValue(new File(this.getClass().getResource("/output.json").getFile()),productList);
			}else {
				System.out.println("Output is written to the : "+prop.get("outputFilePath").toString());
				writer.writeValue(new File(prop.get("outputFilePath").toString()),productList);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getProperties() {
		try {
			prop = new Properties();
			InputStream input = this.getClass().getResourceAsStream("/config.properties");
			prop.load(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<PromotionSet> getPromotions(String promotionSetToApply) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			InputStream input = this.getClass().getResourceAsStream("/promotion.json");
			List<PromotionSet> promotionList = mapper.readValue(convertInputStreamToString(input),new TypeReference<List<PromotionSet>>(){});
			List<PromotionSet> finalList = new ArrayList<>();
			for(PromotionSet eachPromoSet : promotionList) {
				if(eachPromoSet.getName().equalsIgnoreCase(promotionSetToApply) || eachPromoSet.getName().equalsIgnoreCase("default")) {
					finalList.add(eachPromoSet);
				}
			}
			return finalList;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ArrayList();
		}
	}

	public ArrayList<Product> getProductList(){
		try {
			String productUrl = prop.getProperty("productDetailsUrl");
			String output = HttpHelper.get(productUrl);
			ObjectMapper mapper = new ObjectMapper();
			ArrayList<Product> productList = mapper.readValue(output, new TypeReference<List<Product>>() {});
			convertProductsPriceToBaseCurrency(productList);
			return productList;
		}catch(Exception ex){
			System.out.println("Error in fetching product list");
			ex.printStackTrace();
			return new ArrayList();
		}
	}

	public Forex getForex(){
		try {
			String productUrl = prop.getProperty("forexRateUrl");
			String baseCurrency = prop.getProperty("baseCurrency");
			String output = HttpHelper.get(productUrl+"?base="+baseCurrency);
			ObjectMapper mapper = new ObjectMapper();
			Forex forexMap = mapper.readValue(output,Forex.class);
			return forexMap;
		}catch(Exception ex){
			System.out.println("Error in forext list");
			ex.printStackTrace();
			return new Forex();
		}
	}

	public void convertProductsPriceToBaseCurrency(ArrayList<Product> productList) {
		Forex forexMap = getForex();
		for (Product eachProduct : productList){
			if(!eachProduct.getCurrency().equalsIgnoreCase(forexMap.getBase())) {
				Double conversionRate = forexMap.getRates().get(eachProduct.getCurrency());
				eachProduct.setPrice(eachProduct.getPrice()/conversionRate);
				eachProduct.setCurrency(forexMap.getBase());
				System.out.println("Changing currency of product : " + eachProduct.getProduct());
			}
		}
	}

	private String convertInputStreamToString(InputStream inputStream) throws IOException {
		StringWriter writer = new StringWriter();
		IOUtils.copy(inputStream, writer, StandardCharsets.UTF_8);
		return writer.toString();
	}
}
