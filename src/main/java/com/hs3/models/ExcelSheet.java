package com.hs3.models;

import java.util.List;

public class ExcelSheet {
    private String sheetName;
    private List<List<ExcelData>> eds;

    public String getSheetName() {
        return this.sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public List<List<ExcelData>> getEds() {
        return this.eds;
    }

    public void setEds(List<List<ExcelData>> eds) {
        this.eds = eds;
    }
}
