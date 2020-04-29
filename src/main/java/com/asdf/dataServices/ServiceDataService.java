package com.asdf.dataServices;

import com.asdf.dataObjects.employee.EmployeeResource;
import com.asdf.dataObjects.location.AddressResource;
import com.asdf.dataObjects.location.LongitudeLatitude;
import com.asdf.dataObjects.service.Service;
import com.asdf.dataObjects.service.ServiceDto;
import com.asdf.dataObjects.service.ServiceResource;
import com.asdf.exceptions.ResourceNotFoundException;
import com.asdf.exceptions.rest.InvalidDataExceptionMS;
import com.asdf.exceptions.rest.ResourceNotFoundExceptionMS;
import com.asdf.managers.ServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

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

    public ServiceResource addServiceDto(ServiceDto serviceDto, boolean address) {
        checkServiceDto(serviceDto);

        Service service = serDtoToService(serviceDto);

        Service res = serviceManager.addService(service);
        return serToRes(res, address);
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

    private Service serResToService(ServiceResource serviceResource) {
        Service ser = new Service();

        ser.setDate(serviceResource.getDate());
        ser.setEmployeeId(serviceResource.getEmployee().getId());
        ser.setLatitude(serviceResource.getLatitude());
        ser.setLongitude(serviceResource.getLongitude());
        ser.setName(serviceResource.getName());

        return ser;
    }

    public List<ServiceResource> getServiceResources(boolean address) {
        List<ServiceResource> sers = new ArrayList<>();
        for (Service ser : serviceManager.getServices()) {
            sers.add(serToRes(ser, address));
        }
        return sers;
    }


    public List<ServiceResource> getServiceResources(int skip, int amount, boolean address) {
        List<ServiceResource> sers = new ArrayList<>();
        for (Service ser : serviceManager.getServices(skip, amount)) {
            sers.add(serToRes(ser, address));
        }
        return sers;
    }

    public List<ServiceResource> resetServiceResources(int amount, boolean address) {
        List<ServiceResource> old = deleteServiceResources(address);
        if (amount > 100) amount = 100;

        serviceManager.addServices_Mock(amount, employeeDataService.getValidIds());

        return old;
    }

    public List<ServiceResource> deleteServiceResources(boolean address) {
        List<ServiceResource> old = getServiceResources(address);
        serviceManager.deleteAllServiceResources();
        return old;
    }

    private ServiceResource serToRes(Service ser, boolean address) {
        ServiceResource serres = new ServiceResource();

        serres.setDate(ser.getDate());
        try {
            serres.setEmployee(employeeDataService.getEmployee(ser.getEmployeeId()));
        } catch (ResourceNotFoundException | HttpClientErrorException e) {
            serres.setEmployee(new EmployeeResource());
        }
        serres.setId(ser.getId());
        serres.setAddress(address ? locationIQDataService.getAddress(ser.getLongitude(), ser.getLatitude()) : new AddressResource());
        serres.setLongitude(ser.getLongitude());
        serres.setLatitude(ser.getLatitude());
        serres.setName(ser.getName());

        return serres;
    }

    public ServiceResource getServiceResource(int serId, boolean address) {
        if (!serviceManager.serviceExists(serId)) {
            throw new ResourceNotFoundExceptionMS(String.format("The service with the id %d could not be found", serId));
        }
        return serToRes(serviceManager.getService(serId), address);
    }

    public ServiceResource deleteService(int serId, boolean address) {
        if (!serviceManager.serviceExists(serId)) {
            throw new ResourceNotFoundExceptionMS(String.format("The service with the id %d could not be found", serId));
        }
        return serToRes(serviceManager.deleteService(serId), address);
    }

    public ServiceResource putService(int serviceId, ServiceDto serviceDto, boolean address) {
        checkServiceDto(serviceDto);

        Service service = serDtoToService(serviceDto);
        service.setId(serviceId);
        ServiceResource old = new ServiceResource();
        if (serviceManager.serviceExists(serviceId)) {
            old = serToRes(serviceManager.getService(serviceId), address);
        }

        serviceManager.putService(serviceId, service);

        return old;
    }

    private void checkServiceResource(ServiceResource serviceResource) {
        if (isNullOrEmpty(serviceResource.getName())) {
            throw new InvalidDataExceptionMS("The name of the service must be set");
        }
        if (serviceResource.getDate() == null) {
            throw new InvalidDataExceptionMS("The date of the service must be set");
        }
        if (isNullOrEmpty(serviceResource.getLatitude())) {
            throw new InvalidDataExceptionMS("The latitude of the service must be set");
        }
        if (isNullOrEmpty(serviceResource.getLongitude())) {
            throw new InvalidDataExceptionMS("The longitude of the service must be set");
        }
    }

    private void checkServiceDto(ServiceDto serviceDto) {
        if (isNullOrEmpty(serviceDto.getName())) {
            throw new InvalidDataExceptionMS("The name of the service must be set");
        }
        if (serviceDto.getDate() == null) {
            throw new InvalidDataExceptionMS("The date of the service must be set");
        }
        if (isNullOrEmpty(serviceDto.getAddress())) {
            throw new InvalidDataExceptionMS("The address of the service must be set");
        }
        if (serviceDto.getName().length() < 4) {
            throw new InvalidDataExceptionMS("The name must be minimum 4 long");
        }
        if (serviceDto.getAddress().length() < 5) {
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

    public long countServices() {
        return serviceManager.countServices();
    }
}
