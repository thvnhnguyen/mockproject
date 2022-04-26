package com.thvnhng.mockproject.ExcelModel;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class BaseExportExcelModel {

    public List<String> getHeaders(){
        List<String> headers = new ArrayList<>();
        headers.add("#");
        List<MetadataExcelModel> listMetadata = getListMetadata();
        for (MetadataExcelModel metadataExcelModel : listMetadata) {
            headers.add(metadataExcelModel.getHeader());
        }
        return headers;
    }

    public abstract List<MetadataExcelModel> getListMetadata();

}
