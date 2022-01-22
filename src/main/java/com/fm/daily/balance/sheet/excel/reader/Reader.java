package com.fm.daily.balance.sheet.excel.reader;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

public class Reader {
    private static final Logger log = LoggerFactory.getLogger(Reader.class);

    public void readExcel(String filepath) {
        File file = new File(filepath);

        try {
            FileInputStream fis = new FileInputStream(file);

            try {
                XSSFWorkbook wb = new XSSFWorkbook(fis);

                try {
                    XSSFSheet sheet = wb.getSheetAt(0);
                    Iterator var6 = sheet.iterator();

                    while (var6.hasNext()) {
                        Row row = (Row) var6.next();
                        Iterator cellIterator = row.cellIterator();

                        while (cellIterator.hasNext()) {
                            Cell cell = (Cell) cellIterator.next();
                            switch (cell.getCellType()) {
                                case STRING:
                                    log.info("the string value of this cell is {}", cell.getStringCellValue() + "\t\t\t");
                                    break;
                                case NUMERIC:
                                    log.info("the numeric value of this cell is {}", cell.getNumericCellValue() + "\t\t\t");
                            }
                        }

                        log.info("done reading the row");
                    }
                } catch (Throwable var12) {
                    try {
                        wb.close();
                    } catch (Throwable var11) {
                        var12.addSuppressed(var11);
                    }

                    throw var12;
                }

                wb.close();
            } catch (Throwable var13) {
                try {
                    fis.close();
                } catch (Throwable var10) {
                    var13.addSuppressed(var10);
                }

                throw var13;
            }

            fis.close();
        } catch (Exception var14) {
            var14.printStackTrace();
        }

    }
}
