package com.vincentq.experiment6;

import javax.persistence.*;

public class CalendarObj {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //@Column(name="id")
    private int id;

    private boolean secure;

    private String name;


}
