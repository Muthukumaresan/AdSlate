package com.adslate.models;

/**
 * Created by pooja.b on 01-02-2016.
 */
public class OrderDetails
{

    String orderNo,onsetDate,onsetTime,duration,status,paymentdetails;


    public OrderDetails(String orderNo, String onsetDate, String onsetTime, String duration, String status, String paymentdetails) {
        this.orderNo = orderNo;
        this.onsetDate = onsetDate;
        this.onsetTime = onsetTime;
        this.duration = duration;
        this.status = status;
        this.paymentdetails = paymentdetails;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getOnsetDate() {
        return onsetDate;
    }

    public String getOnsetTime() {
        return onsetTime;
    }

    public String getDuration() {
        return duration;
    }

    public String getStatus() {
        return status;
    }

    public String getPaymentdetails() {
        return paymentdetails;
    }
}
