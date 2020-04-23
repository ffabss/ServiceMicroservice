package com.asdf.dataObjects.employee;

import lombok.Data;

@Data
public class EmployeeResource {
    private int id;
    private String name;
    private String address;
    private String latitude;
    private String longitude;
}
