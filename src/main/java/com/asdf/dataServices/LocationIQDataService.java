package com.asdf.dataServices;

import com.asdf.dataObjects.location.AddressResource;
import com.asdf.dataObjects.location.LocationResource;
import com.asdf.dataObjects.location.LongitudeLatitude;
import com.asdf.exceptions.rest.InternalServerException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Component
public class LocationIQDataService {
    private static final String LOCATION_IQ_URL = "https://eu1.locationiq.com/v1/search.php?key={apiKey}&q={searchString}&format=json&limit=1";
    private static final String LOCATION_IQ_REVERSE_URL = "https://eu1.locationiq.com/v1/reverse.php?key={apiKey}&lat={lat}&lon={lon}&format=json";
    private static final String[] API_KEYs = {
            "34f5a525bf298a", "b06efbe4532f08", "0aa44a6f78974e", "dc255a1a5eeb65",
            "201e218b2b48a4", "1d6c03484794a5", "6ddac64cdf0664", "4f7a4411e0fcbb",
            "a45f0df77338d9", "21fe6b3316a50b", "e2107f8b1b287f", "d881a7874b8938",
            "87f6511faef5e2", "ccf626b97d62f8", "4b9894bd230d4e", "8b2d77f56a8faa",
            "fd92119e00154b", "c0a8349a8ca075", "b09b6456dbf35c", "4f9628e261ccb8",
    };
    private static int currentKeyID = 0;
    private final int MAX_TRIES = 100;

    private static void changeKey() {
        synchronized (API_KEYs) {
            currentKeyID++;
            if (currentKeyID >= API_KEYs.length) {
                currentKeyID = 0;
            }
        }
    }

    private String getCurrentKey() {
        return API_KEYs[currentKeyID];
    }

    public LongitudeLatitude getLongitudeLatitudeByAddress(AddressResource address) {
        String query = "";
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(AddressResource.class);
            for (PropertyDescriptor propertyDesc : beanInfo.getPropertyDescriptors()) {
                Object value = propertyDesc.getReadMethod().invoke(address);
                query += value;
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return getLongitudeLatitudeByAddress(query, MAX_TRIES);
    }

    public LongitudeLatitude getLongitudeLatitudeByAddress(String address) {
        return getLongitudeLatitudeByAddress(address, MAX_TRIES);
    }

    public LongitudeLatitude getLongitudeLatitudeByAddress(String address, int triesLeft) {
        changeKey();
        if (triesLeft < MAX_TRIES) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        } else {
            try {
                Thread.sleep((int) ((500 / API_KEYs.length) * 1.2));
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }

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
            if ((e.getRawStatusCode() == 429 || e.getRawStatusCode() == 404) && triesLeft > 0) {
                return getLongitudeLatitudeByAddress(address, triesLeft - 1);
            } else {
                throw new InternalServerException(e);
            }
        }
    }

    public AddressResource getAddress(String lon, String lat) {
        LongitudeLatitude lola = new LongitudeLatitude();
        lola.setLatitude(lat);
        lola.setLongitude(lon);
        return getAddress(lola);
    }

    public AddressResource getAddress(LongitudeLatitude longitudeLatitude, int triesLeft) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> vars = new HashMap<>();
        vars.put("apiKey", getCurrentKey());
        vars.put("lon", longitudeLatitude.getLongitude());
        vars.put("lat", longitudeLatitude.getLatitude());
        try {
            LocationResource response = restTemplate.getForObject(
                    LOCATION_IQ_REVERSE_URL,
                    LocationResource.class, vars);
            return response.getAddress();
        } catch (RestClientResponseException e) {
            if (e.getRawStatusCode() == 429 && triesLeft > 0) {
                changeKey();
                return getAddress(longitudeLatitude, triesLeft - 1);
            } else {
                throw new InternalServerException(e);
            }
        }
    }

    public AddressResource getAddress(LongitudeLatitude longitudeLatitude) {
        return getAddress(longitudeLatitude, MAX_TRIES);
    }
}
