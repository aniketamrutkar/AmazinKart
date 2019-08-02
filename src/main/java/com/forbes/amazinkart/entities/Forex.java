package com.forbes.amazinkart.entities;

import java.util.HashMap;

public class Forex {
  private HashMap<String,Double> rates;
  private String base;
  private String date;

  public HashMap<String, Double> getRates() {
    return rates;
  }

  public void setRates(HashMap<String, Double> rates) {
    this.rates = rates;
  }

  public String getBase() {
    return base;
  }

  public void setBase(String base) {
    this.base = base;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }
}

