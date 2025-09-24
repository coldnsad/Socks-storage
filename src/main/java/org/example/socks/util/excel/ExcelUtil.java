package org.example.socks.util.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.socks.model.Socks;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelUtil {
    public static String FILE_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public static boolean isExcelFile(MultipartFile file) {
        return FILE_TYPE.equals(file.getContentType());
    }

    public static List<Socks> getSocksFromFile(MultipartFile file) {
        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            List<Socks> result = new ArrayList<>();
            while (rowIterator.hasNext()) {
                Row currentRow = rowIterator.next();
                if(currentRow.getRowNum() == 0){
                    continue;
                }
                Iterator<Cell> cellIterator = currentRow.cellIterator();
                Socks socks = new Socks();
                while (cellIterator.hasNext()) {
                    Cell currentCell = cellIterator.next();
                    switch (currentCell.getColumnIndex()) {
                        case 0 -> socks.setColor(currentCell.getStringCellValue());
                        case 1 -> socks.setCottonPercentage((int)currentCell.getNumericCellValue());
                        case 2 -> socks.setCount((int)currentCell.getNumericCellValue());
                    }
                }
                result.add(socks);
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
