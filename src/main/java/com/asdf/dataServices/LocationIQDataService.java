package com.asdf.dataServices;

import com.asdf.dataObjects.location.LocationResource;
import com.asdf.dataObjects.location.LongitudeLatitude;
import com.asdf.exceptions.rest.InternalServerException;
import com.sun.org.apache.xpath.internal.objects.XString;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class LocationIQDataService {
    private static final String LOCATION_IQ_URL = "https://eu1.locationiq.com/v1/search.php?key={apiKey}&q={searchString}&format=json";
    private static final String API_KEY = "34f5a525bf298a";
    private static final String LOCATION_IQ_REVERSE_URL = "https://eu1.locationiq.com/v1/reverse.php?key={apiKey}&lat={lat}&lon={lon}&format=json";

    public LongitudeLatitude getLongitudeLatitudeByAddress(String address) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> vars = new HashMap<>();
        vars.put("apiKey", API_KEY);
        vars.put("searchString", address);
        try {
            LocationResource[] response = restTemplate.getForObject(
                    LOCATION_IQ_URL,
                    LocationResource[].class, vars);
            LongitudeLatitude result = new LongitudeLatitude();
            result.setLongitude(response[0].getLon());
            result.setLatitude(response[0].getLat());
            return result;
        } catch (RestClientResponseException e) {
            throw new InternalServerException(e);
        }
    }

    public String getAddress(String lon, String lat) {
        LongitudeLatitude lola = new LongitudeLatitude();
        lola.setLatitude(lat);
        lola.setLongitude(lon);
        return getAddress(lola);
    }

    public String getAddress(LongitudeLatitude longitudeLatitude) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> vars = new HashMap<>();
        vars.put("apiKey", API_KEY);
        vars.put("lon", longitudeLatitude.getLongitude());
        vars.put("lat", longitudeLatitude.getLatitude());
        try {
            LocationResource response = restTemplate.getForObject(
                    LOCATION_IQ_REVERSE_URL,
                    LocationResource.class, vars);
            String result = response.getDisplay_name();
            return result;
        } catch (RestClientResponseException e) {
            throw new InternalServerException(e);
        }
    }
}
