package com.experiment5.jpa.Excel;

import com.experiment5.jpa.locations.Locations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin("http://localhost:8081")
@Controller
@RequestMapping("/excel")
public class ExcelController {

        @Autowired
        ExcelService fileService;

        @PostMapping("/upload")
        public ResponseEntity<Response> uploadFile(@RequestParam("file")MultipartFile file) {
            String msg = "";

            if(ExcelImport.isExcelFormat(file)) {
                try {
                    fileService.save(file);

                    msg = "Uploaded Successfully: " + file.getOriginalFilename();
                    return ResponseEntity.status(HttpStatus.OK).body(new Response(msg));
                } catch (Exception e) {
                    msg = "Error Could not upload file: " + file.getOriginalFilename();
                    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new Response(msg));
                }
            }
            msg = "Please upload Excel file";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(msg));
        }

        @GetMapping("/locations")
        public ResponseEntity<List<Locations>> getAllLocations() {
            try {
                List<Locations> locations = fileService.getAllLocations();

                if (locations.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }

                return new ResponseEntity<>(locations, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
}

