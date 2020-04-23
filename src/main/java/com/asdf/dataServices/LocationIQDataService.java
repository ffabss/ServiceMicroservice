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
    private int currentKeyID = 0;
    private static final String[] API_KEYs = {"34f5a525bf298a", "b06efbe4532f08", "201e218b2b48a4", "1d6c03484794a5", "6ddac64cdf0664"};
    private static final String LOCATION_IQ_REVERSE_URL = "https://eu1.locationiq.com/v1/reverse.php?key={apiKey}&lat={lat}&lon={lon}&format=json";

    private String getCurrentKey() {
        return API_KEYs[currentKeyID];
    }

    private void changeKey() {
        currentKeyID++;
        if (currentKeyID == API_KEYs.length) {
            currentKeyID = 0;
        }
    }

    public LongitudeLatitude getLongitudeLatitudeByAddress(String address) {
        return getLongitudeLatitudeByAddress(address, 5);
    }

    public LongitudeLatitude getLongitudeLatitudeByAddress(String address, int triesLeft) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> vars = new HashMap<>();
        vars.put("apiKey", getCurrentKey());
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
            if (e.getRawStatusCode() == 429 && triesLeft > 0) {
                changeKey();
                return getLongitudeLatitudeByAddress(address, triesLeft - 1);
            } else {
                throw new InternalServerException(e);
            }
        }
    }

    public String getAddress(String lon, String lat) {
        LongitudeLatitude lola = new LongitudeLatitude();
        lola.setLatitude(lat);
        lola.setLongitude(lon);
        return getAddress(lola);
    }

    public String getAddress(LongitudeLatitude longitudeLatitude, int triesLeft) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> vars = new HashMap<>();
        vars.put("apiKey", getCurrentKey());
        vars.put("lon", longitudeLatitude.getLongitude());
        vars.put("lat", longitudeLatitude.getLatitude());
        try {
            LocationResource response = restTemplate.getForObject(
                    LOCATION_IQ_REVERSE_URL,
                    LocationResource.class, vars);
            String result = response.getDisplay_name();
            return result;
        } catch (RestClientResponseException e) {
            if (e.getRawStatusCode() == 429 && triesLeft > 0) {
                changeKey();
                return getAddress(longitudeLatitude, triesLeft - 1);
            } else {
                throw new InternalServerException(e);
            }
        }
    }

    public String getAddress(LongitudeLatitude longitudeLatitude) {
        return getAddress(longitudeLatitude, 5);
    }
}
