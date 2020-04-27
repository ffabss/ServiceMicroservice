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

    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public HttpEntity<List<ServiceResource>> resetEmployees(@RequestParam(defaultValue = "false") boolean address) {
        return resetEmployees(10, address);
    }

    @RequestMapping(value = "/reset/{amount}", method = RequestMethod.GET)
    public HttpEntity<List<ServiceResource>> resetEmployees(@PathVariable int amount, @RequestParam(defaultValue = "false") boolean address) {
        return new HttpEntity<>(serviceDataService.resetServiceResources(amount, address));
    }

    @RequestMapping(value = "/countServices", method = RequestMethod.GET)
    public HttpEntity<Long> countServices() {
        return new HttpEntity<>(serviceDataService.countServices());
    }

    @RequestMapping(value = "/services", method = RequestMethod.GET)
    public HttpEntity<List<ServiceResource>> getAllServices(@RequestParam(defaultValue = "true") boolean address) {
        return new HttpEntity<>(serviceDataService.getServiceResources(address));
    }

    @RequestMapping(value = "/services", method = RequestMethod.DELETE)
    public HttpEntity<List<ServiceResource>> deleteAllEmployees(@RequestParam(defaultValue = "true") boolean address) {
        return new HttpEntity<>(serviceDataService.deleteServiceResources(address));
    }


    @RequestMapping(value = "/services/{skip}/{amount}", method = RequestMethod.GET)
    public HttpEntity<List<ServiceResource>> getServices(@PathVariable int skip, @PathVariable int amount, @RequestParam(defaultValue = "true") boolean address) {
        return new HttpEntity<>(serviceDataService.getServiceResources(skip, amount, address));
    }

    @RequestMapping(value = "/services", method = RequestMethod.POST)
    public ServiceResource addService(@RequestBody ServiceDto serviceDto, @RequestParam(defaultValue = "true") boolean address) {
        return serviceDataService.addServiceDto(serviceDto, address);
    }

    @RequestMapping(value = "/services/{serviceId}", method = RequestMethod.GET)
    public ServiceResource getService(@PathVariable int serviceId, @RequestParam(defaultValue = "true") boolean address) {
        return serviceDataService.getServiceResource(serviceId, address);
    }

    @RequestMapping(value = "/services/{serviceId}", method = RequestMethod.DELETE)
    public ServiceResource deleteService(@PathVariable int serviceId, @RequestParam(defaultValue = "true") boolean address) {
        return serviceDataService.deleteService(serviceId, address);
    }

    @RequestMapping(value = "/services/{serviceId}", method = RequestMethod.PUT)
    public ServiceResource deleteService(@PathVariable int serviceId, @RequestBody ServiceResource service, @RequestParam(defaultValue = "true") boolean address) {
        return serviceDataService.putService(serviceId, service, address);
    }
}
