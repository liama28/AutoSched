package com.vincentq.experiment6;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Calendar;

@Controller
@RequestMapping(path="/Calendar")
public class OccasionController {
    @Autowired
    private OccasionRepository occasionRepository;

    private String success = "{\"code\":0,\"message\":\"success\"}";

    @PostMapping(path="/addEvent")
    public @ResponseBody String addNewEvent (@RequestParam String name, @RequestParam String startDate, @RequestParam int hour, @RequestParam int minute, @RequestParam boolean daily, @RequestParam boolean weekly, @RequestParam boolean monthly) {
        Occasions event = new Occasions();
        event.setName(name);
        event.setStartDate(startDate);
        event.setHour(hour);
        event.setMinute(minute);
        event.setDaily(daily);
        event.setWeekly(weekly);
        event.setMonthly(monthly);
        occasionRepository.save(event);

        return success;
    }
}
