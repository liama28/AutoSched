package com.example.AutoSched.excel;

import com.example.AutoSched.locations.LocationRepository;
import com.example.AutoSched.locations.Locations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {
    @Autowired
    LocationRepository repo;

    public void save(MultipartFile file){
        try {
            List<Locations> locationsList = ExcelImport.excelToLocations(file.getInputStream());
            repo.saveAll(locationsList);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store: " + e.getMessage());
        }
    }

    public List<Locations> getAllLocations() {
        return repo.findAll();
    }

}
