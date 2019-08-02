package com.forbes.amazinkart.entities;

public class Discount {
  private double amount;
  private String discountTag;

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public String getDiscountTag() {
    return discountTag;
  }

  public void setDiscountTag(String discountTag) {
    this.discountTag = discountTag;
  }
}
