package com.fm.daily.balance.sheet;

import com.fm.daily.balance.sheet.excel.reader.Reader;

public class DailyBalanceSheetApp {

    public static void main(String[] args) {
        String filepath = "D:\\dev\\daily-balance-sheet\\src\\main\\resources\\example clients.xlsx";

        Reader excelReader = new Reader();
        excelReader.readExcel(filepath);
    }
}