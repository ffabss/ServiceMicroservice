package com.asdf.dataObjects.service;

import lombok.Data;

import java.util.Date;

@Data
public class ServiceDto {
    private String name;
    private int employeeId;
    private Date date;
    private String address;
}
