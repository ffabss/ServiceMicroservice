package com.asdf.dataObjects.employee;

import com.asdf.dataObjects.location.AddressResource;
import lombok.Data;

@Data
public class EmployeeResource {
    private int id;
    private String name;
    private AddressResource address;
    private String latitude;
    private String longitude;
}
