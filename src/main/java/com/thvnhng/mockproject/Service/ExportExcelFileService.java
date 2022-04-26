package com.thvnhng.mockproject.Service;

import com.thvnhng.mockproject.ExcelModel.BaseExportExcelModel;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

public interface ExportExcelFileService {

    DateTimeFormatter DATE_FORMATTER =  DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter DATE_TIME_FORMATTER =  DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
    File exportFile(String fileName, String sheetName, List<BaseExportExcelModel> dataExport, Class<? extends BaseExportExcelModel> classType);

}
