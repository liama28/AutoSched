package com.example.AutoSched.excel;

import com.example.AutoSched.locations.Locations;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin("http://localhost:8081")
@Api(value = "Excel", description = "REST APIs related to excel file handling")
@Controller
@RequestMapping("/excel")
public class ExcelController {

        @Autowired
        ExcelService fileService;

        @ApiOperation(value = "Upload a file for locations")
        @ApiResponses(value = {
                @ApiResponse(code = 200, message = "Uploaded Successfully"),
                @ApiResponse(code = 417, message = "Error Could not upload file"),
                @ApiResponse(code = 400, message = "Please upload Excel file")
        })
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
        @ApiOperation(value = "List locations in the location database")
        @ApiResponses(value = {
                @ApiResponse(code = 204, message = "No Content"),
                @ApiResponse(code = 200, message = "Return locations"),
                @ApiResponse(code = 500, message = "Internal Server Error")
         })
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

