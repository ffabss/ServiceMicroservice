package com.asdf;


import com.asdf.dataObjects.service.ServiceDto;
import com.asdf.dataObjects.service.ServiceResource;
import com.asdf.dataServices.ServiceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/serviceBackend")
public class RESTController {

    @Autowired
    private ServiceDataService serviceDataService;

    @RequestMapping(value ="/services", method = RequestMethod.GET)
    public HttpEntity<List<ServiceResource>> getAllServices() {
        return new HttpEntity<>(serviceDataService.getServiceResources());
    }

    @RequestMapping(value ="/services", method = RequestMethod.POST)
    public ServiceResource addService(@RequestBody ServiceDto serviceDto){
        return serviceDataService.addServiceDto(serviceDto);
    }

    @RequestMapping(value ="/services/{serviceId}", method = RequestMethod.GET)
    public ServiceResource getService(@PathVariable int serviceId){
        return serviceDataService.getServiceResource(serviceId);
    }

    @RequestMapping(value ="/services/{serviceId}", method = RequestMethod.DELETE)
    public ServiceResource deleteService(@PathVariable int serviceId){
        return serviceDataService.deleteService(serviceId);
    }

    @RequestMapping(value ="/services/{serviceId}", method = RequestMethod.PUT)
    public ServiceResource deleteService(@PathVariable int serviceId,@RequestBody ServiceDto serviceDto){
        return serviceDataService.putService(serviceId,serviceDto);
    }

    @RequestMapping(value ="/services/{serviceId}/address", method = RequestMethod.GET)
    public String getAddressOfService(@PathVariable int serviceId){
        return serviceDataService.getAddressOfService(serviceId);
    }
}