package com.asdf.dataObjects.service;

import com.asdf.dataObjects.employee.EmployeeResource;
import lombok.Data;

import java.util.Date;

@Data
public class ServiceResource {
    private int id;
    private String name;
    private EmployeeResource employee;
    private Date date;
    private String address;
    private String latitude;
    private String longitude;
}
