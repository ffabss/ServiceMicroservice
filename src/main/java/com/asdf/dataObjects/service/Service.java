package com.asdf.dataObjects.service;

import lombok.Data;

import java.util.Date;

@Data
public class Service {
    private int id;
    private String name;
    private int employeeId;
    private Date date;
    private String longitude;
    private String latitude;
}

