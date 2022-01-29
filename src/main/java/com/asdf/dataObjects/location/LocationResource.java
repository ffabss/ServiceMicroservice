package com.asdf.dataObjects.location;

import lombok.Data;

import java.util.List;


@Data
public class LocationResource {
    private String place_id;
    private String licence;
    private String osm_type;
    private String osm_id;
    private List<String> boundingbox;
    private String lat;
    private String lon;
    private String display_name;
    private String type;
    private String display_place;
    private AddressResource address;
    private double importance;
    private String icon;
}
