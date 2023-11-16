package com.example.assignment;

public class North {
    private String name;
    private String duration;
    private String departureTime;
    private String arrivalTime;

    public North(String name, String duration) {
        this.name = name;
        this.duration = duration;
    }

    public North(String name, String duration, String departureTime, String arrivalTime) {
        this.name = name;
        this.duration = duration;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public String getName() {
        return name;
    }

    public String getDuration() {
        return duration;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

}