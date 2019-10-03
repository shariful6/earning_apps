package com.example.shariful.bdcash;

public class Employee {

   private String mobile;
   private String amount;
   private String payment_method;

    public Employee(String mobile, String amount, String payment_method) {
        this.mobile = mobile;
        this.amount = amount;
        this.payment_method = payment_method;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }


}
