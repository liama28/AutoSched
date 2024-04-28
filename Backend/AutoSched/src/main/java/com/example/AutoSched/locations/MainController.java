package com.example.AutoSched.locations;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@Api(value = "Location Object Controller", description = "REST APIs related to location object handling")
@RequestMapping(path="/locations")

public class MainController {
    @Autowired
    private com.example.AutoSched.locations.LocationRepository locationRepository;

    @ApiOperation(value = "Gets all data found in the Locations database on the MySQL server")
    @GetMapping(path = "/getAllLocations")
    public @ResponseBody Iterable<com.example.AutoSched.locations.Locations> getAllLocations() {return locationRepository.findAll();}

    @ApiOperation(value = "Gets the data of the given id found in the Locations database on the MySQL server")
    @GetMapping(path = "/getLocation/{id}")
    @ResponseBody
    com.example.AutoSched.locations.Locations getLocationFromID(@PathVariable int id) {
        return locationRepository.findById(id);
    }
}
