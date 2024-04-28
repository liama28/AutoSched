package com.experiment5.jpa.Excel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.experiment5.jpa.locations.Locations;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;


public class ExcelImport {

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = { "id", "lat", "lon", "name"};
    static String SHEET = "Locations";

    public static boolean isExcelFormat(MultipartFile file){

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public static List<Locations> excelToLocations(InputStream inputStream){
        try {
            Workbook wb = new XSSFWorkbook(inputStream);

            Sheet sheet = wb.getSheet(SHEET);

            Iterator<Row> rows = sheet.iterator();

            List<Locations> locations = new ArrayList<Locations>();

            int rowNum = 0;
            while(rows.hasNext()){
                Row curRow = rows.next();

                if(rowNum == 0) {
                    rowNum++;
                    continue;
                }

                Iterator<Cell> cellInRow = curRow.iterator();

                Locations location = new Locations();

                int cellIdx = 0;
                while(cellInRow.hasNext()) {
                    Cell curCell = cellInRow.next();

                    switch(cellIdx) {

                        case 0:
                            location.setId((int) curCell.getNumericCellValue());
                            break;
                        case 1:
                            location.setLat(curCell.getNumericCellValue());
                            break;
                        case 2:
                            location.setLon(curCell.getNumericCellValue());
                            break;
                        case 3:
                            location.setName(curCell.getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                locations.add(location);
            }
            wb.close();

            return locations;
        }   catch (IOException e) {
            throw new RuntimeException("failed to parse Excel file: " + e.getMessage());
        }
    }



}
