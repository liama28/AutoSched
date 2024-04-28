package com.vincentq.experiment6;


import javax.persistence.*;

@Entity
public class Occasions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    private String startDate;

    private int hour;

    private int minute;

    private boolean daily;
    private boolean weekly;
    private boolean monthly;

  //  @OneToOne
  //  private Calendar calendar;
    /*
    public Occasions(String name, int startDate, int hour, int minute, boolean daily, boolean weekly, boolean monthly){
        this.name = name;
        this.startDate = startDate;
        this.hour = hour;
        this.minute = minute;
        this.daily = daily;
        this.weekly = weekly;
        this.monthly = monthly;
    }
    */


    public void setName(String name) {this.name = name;}

    public String getName() {return name;}

    public void setStartDate(String startDate) {this.startDate = startDate;}

    public String getStartDate() {return startDate;}

    public void setHour(int hour) {this.hour = hour;}

    public int getHour() {return hour;}

    public void setMinute(int minute) {this.minute = minute;}

    public int getMinute() {return minute;}

    public void setDaily(boolean daily) {this.daily = daily;}

    public boolean getDaily() {return daily;}

    public void setWeekly(boolean weekly) {this.weekly = weekly;}

    public boolean getWeekly() {return weekly;}

    public void setMonthly(boolean monthly) {this.monthly = monthly;}

    public boolean getMonthly() {return monthly;}




}
