package com.example.assignment;

public class History {

    String destination_name, origin_name, seat_type, seat_no, train_date, seat_coach, arrival_time, depart_time;

    public History(){}

    public History(String destination_name, String origin_name, String seat_type, String seat_no, String train_date, String seat_coach, String arrival_time, String depart_time) {
        this.destination_name = destination_name;
        this.origin_name = origin_name;
        this.seat_type = seat_type;
        this.seat_no = seat_no;
        this.train_date = train_date;
        this.seat_coach = seat_coach;
        this.depart_time = depart_time;
        this.arrival_time = arrival_time;
    }

    public String getDestination_name() {
        return destination_name;
    }

    public void setDestination_name(String destination_name) {
        this.destination_name = destination_name;
    }

    public String getOrigin_name() {
        return origin_name;
    }

    public void setOrigin_name(String origin_name) {
        this.origin_name = origin_name;
    }

    public String getSeat_type() {
        return seat_type;
    }

    public void setSeat_type(String seat_type) {
        this.seat_type = seat_type;
    }

    public String getSeat_no() {
        return seat_no;
    }

    public void setSeat_no(String seat_no) {
        this.seat_no = seat_no;
    }

    public String getTrain_date() { return train_date; }

    public void setTrain_date(String train_date) { this.train_date = train_date; }

    public String getSeat_coach() { return seat_coach; }

    public void setSeat_coach(String seat_coach) { this.seat_coach = seat_coach; }

    public String getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(String arrival_time) {
        this.arrival_time = arrival_time;
    }

    public String getDepart_time() {
        return depart_time;
    }

    public void setDepart_time(String depart_time) {
        this.depart_time = depart_time;
    }
}
