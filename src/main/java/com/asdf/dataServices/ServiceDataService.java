package com.asdf.dataServices;

import com.asdf.dataObjects.location.LongitudeLatitude;
import com.asdf.dataObjects.service.Service;
import com.asdf.dataObjects.service.ServiceDto;
import com.asdf.dataObjects.service.ServiceResource;
import com.asdf.exceptions.rest.InvalidDataExceptionMS;
import com.asdf.exceptions.rest.ResourceNotFoundExceptionMS;
import com.asdf.managers.ServiceManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class ServiceDataService {

    @Autowired
    private ServiceManager serviceManager;

    @Autowired
    private EmployeeDataService employeeDataService;

    @Autowired
    private LocationIQDataService locationIQDataService;

    public ServiceResource addServiceDto(ServiceDto serviceDto) {
        checkServiceDto(serviceDto);

        Service service = serDtoToService(serviceDto);

        Service res = serviceManager.addService(service);
        return serToRes(res);
    }

    private Service serDtoToService(ServiceDto serviceDto) {
        Service ser = new Service();

        ser.setDate(serviceDto.getDate());
        ser.setEmployeeId(serviceDto.getEmployeeId());
        LongitudeLatitude location = locationIQDataService.getLongitudeLatitudeByAddress(serviceDto.getAddress());
        ser.setLatitude(location.getLatitude());
        ser.setLongitude(location.getLongitude());
        ser.setName(serviceDto.getName());

        return ser;
    }


    public List<ServiceResource> getServiceResources() {
        List<ServiceResource> sers = new ArrayList<>();
        for (Service ser : serviceManager.getServices()) {
            sers.add(serToRes(ser));
        }
        return sers;
    }


    public List<ServiceResource> getServiceResources(int skip, int amount) {
        List<ServiceResource> sers = new ArrayList<>();
        for (Service ser : serviceManager.getServices(skip, amount)) {
            sers.add(serToRes(ser));
        }
        return sers;
    }

    public List<ServiceResource> resetServiceResources() {
        List<ServiceResource> old = deleteServiceResources();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ServiceDto[] emps = objectMapper.readValue(new File("src\\main\\resources\\services.json"), ServiceDto[].class);
            for(ServiceDto emp : emps){
                addServiceDto(emp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return old;
    }
    public List<ServiceResource> deleteServiceResources() {
        List<ServiceResource> old = getServiceResources();
        serviceManager.deleteAllServiceResources();
        return old;
    }
    private ServiceResource serToRes(Service ser) {
        ServiceResource serres = new ServiceResource();

        serres.setDate(ser.getDate());
        serres.setEmployee(employeeDataService.getEmployee(ser.getEmployeeId()));
        serres.setId(ser.getId());
        serres.setAddress(locationIQDataService.getAddress(ser.getLongitude(), ser.getLatitude()));
        serres.setLongitude(ser.getLongitude());
        serres.setLatitude(ser.getLatitude());
        serres.setName(ser.getName());

        return serres;
    }

    public ServiceResource getServiceResource(int serId) {
        if (!serviceManager.serviceExists(serId)) {
            throw new ResourceNotFoundExceptionMS(String.format("The service with the id %d could not be found", serId));
        }
        return serToRes(serviceManager.getService(serId));
    }

    public ServiceResource deleteService(int serId) {
        if (!serviceManager.serviceExists(serId)) {
            throw new ResourceNotFoundExceptionMS(String.format("The service with the id %d could not be found", serId));
        }
        return serToRes(serviceManager.deleteService(serId));
    }

    public ServiceResource putService(int serviceId, ServiceDto serviceDto) {
        if (!serviceManager.serviceExists(serviceId)) {
            throw new ResourceNotFoundExceptionMS(String.format("The service with the id %d could not be found", serviceId));
        }
        checkServiceDto(serviceDto);

        Service service = serDtoToService(serviceDto);
        service.setId(serviceId);

        serviceManager.putService(serviceId, service);

        return serToRes(service);
    }

    public String getAddressOfService(int serviceId) {
        if (!serviceManager.serviceExists(serviceId)) {
            throw new ResourceNotFoundExceptionMS(String.format("The service with the id %d could not be found", serviceId));
        }

        return serviceManager.getAddressOfService(serviceId);
    }

    private void checkServiceDto(ServiceDto serviceDto) {
        if (isNullOrEmpty(serviceDto.getName())) {
            throw new InvalidDataExceptionMS("The name of the car must be set");
        }
        if (serviceDto.getDate() == null) {
            throw new InvalidDataExceptionMS("The date of the car must be set");
        }
        if (isNullOrEmpty(serviceDto.getAddress())) {
            throw new InvalidDataExceptionMS("The address of the car must be set");
        }
        if (serviceDto.getName().length() <= 4) {
            throw new InvalidDataExceptionMS("The name must be minimum 4 long");
        }
        if (serviceDto.getAddress().length() <= 5) {
            throw new InvalidDataExceptionMS("The address must be minimum 5 long");
        }
        if (serviceDto.getDate() == null) {
            throw new ResourceNotFoundExceptionMS("The date must be valid(dd.mm.yyy hh:MM)");
        }
    }

    private boolean isDateValid(String date) {
        try {
            DateFormat df = new SimpleDateFormat("dd.mm.yyy hh:MM");
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isNullOrEmpty(String string) {
        return string == null || string.equals("");
    }

}
