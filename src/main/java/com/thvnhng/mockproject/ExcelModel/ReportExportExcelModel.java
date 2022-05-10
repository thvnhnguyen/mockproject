package com.thvnhng.mockproject.ExcelModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportExportExcelModel extends BaseExportExcelModel {

    private String username;
    private String courseName;
    private String subjectName;
    private String scoreName;
    private Double score;
    private String scoreType;

    @Override
    public List<MetadataExcelModel> getListMetadata() {
        return Arrays.asList(
                new MetadataExcelModel(1, "username", String.class, "Tên tài khoản"),
                new MetadataExcelModel(2, "courseName", String.class, "Tên lớp"),
                new MetadataExcelModel(3, "subjectName", String.class, "Tên môn học"),
                new MetadataExcelModel(4, "reportName", String.class, "Tên report"),
                new MetadataExcelModel(5, "mark15m", Integer.class, "Điểm 15 phút"),
                new MetadataExcelModel(6, "mark45m", Integer.class, "Điểm 45 phút"),
                new MetadataExcelModel(7, "markFinal", Integer.class, "Điểm thi cuối kỳ"),
                new MetadataExcelModel(8, "markSummary", Double.class, "Điểm tổng kết")
        );
    }
}
