package com.thvnhng.mockproject.ExcelModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserExportExcelModel extends BaseExportExcelModel{

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String contactNumber;
    private String address;
    private Date birthDate;
    private LocalDateTime deletedAt;

    @Override
    public List<MetadataExcelModel> getListMetadata() {
        return Arrays.asList(
                new MetadataExcelModel(1, "username", String.class, "Tên tài khoản"),
                new MetadataExcelModel(2, "firstName", String.class, "Họ"),
                new MetadataExcelModel(3, "lastName", String.class, "Tên"),
                new MetadataExcelModel(4, "email", String.class, "Email"),
                new MetadataExcelModel(5, "gender", Character.class, "Giới tính"),
                new MetadataExcelModel(6, "contactNumber", String.class, "Số liên lạc"),
                new MetadataExcelModel(7, "address", String.class, "Địa chỉ"),
                new MetadataExcelModel(8, "birthDate", Date.class, "Ngày sinh"),
                new MetadataExcelModel(9, "deletedAt", LocalDateTime.class, "Đã xóa tại")
        );
    }

}
