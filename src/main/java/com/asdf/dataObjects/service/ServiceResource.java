package com.asdf.dataObjects.service;

import com.asdf.dataObjects.employee.EmployeeResource;
import com.asdf.dataObjects.location.AddressResource;
import lombok.Data;

import java.util.Date;

@Data
public class ServiceResource {
    private int id;
    private String name;
    private EmployeeResource employee;
    private Date date;
    private AddressResource address;
    private String latitude;
    private String longitude;
}
