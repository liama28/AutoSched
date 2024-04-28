package com.experiment5.jpa.locations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(path="/experiment5")

public class MainController {
    @Autowired
    private LocationRepository locationRepository;

    @GetMapping(path = "/getAllLocations")
    public @ResponseBody Iterable<Locations> getAllLocations() {return locationRepository.findAll();}

    @GetMapping(path = "/getLocation/{id}")
    @ResponseBody
    Locations getLocationFromID(@PathVariable int id) {
        return locationRepository.findById(id);
    }
}
