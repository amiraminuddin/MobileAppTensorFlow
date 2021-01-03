package com.example.myapplication.model;

public class rate {

    private String average, counter, totalrate;

    public rate(String average, String counter,String totalrate)
    {
        this.average = average;
        this.counter = counter;
        this.totalrate = totalrate;
    }

    public rate(){}

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public String getTotalrate() {
        return totalrate;
    }

    public void setTotalrate(String totalrate) {
        this.totalrate = totalrate;
    }



}
