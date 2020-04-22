package com.asdf.managers;

import com.asdf.dataObjects.service.Service;
import com.asdf.dataObjects.service.ServiceEntity;
import com.asdf.database.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ServiceManager {

    @Autowired
    private ServiceRepository serviceRepository;

    public Service addService(Service service) {
        ServiceEntity sere = convertServiceToEntity(service);
        serviceRepository.save(sere);
        return convertEntityToService(sere);
    }

    public List<Service> getServices() {
        List<Service> sers = new ArrayList<>();
        for (ServiceEntity sere : serviceRepository.findAll()) {
            sers.add(convertEntityToService(sere));
        }
        return sers;
    }

    public Service getService(int serId) {
        return convertEntityToService(serviceRepository.findById(serId).get());
    }

    public Service deleteService(int serId) {
        Service dele = convertEntityToService(serviceRepository.findById(serId).get());
        serviceRepository.deleteById(serId);
        return dele;
    }

    public Service putService(int serviceId, Service service) {
        ServiceEntity sere = convertServiceToEntity(service);
        sere.setId(serviceId);
        serviceRepository.save(sere);
        return convertEntityToService(sere);
    }

    public boolean serviceExists(int serviceId) {
        return serviceRepository.existsById(serviceId);
    }

    public String getAddressOfService(int serviceId) {
        Optional<ServiceEntity> sere = serviceRepository.findById(serviceId);
        if (!sere.isPresent()) return "invalid";
        Service ser = convertEntityToService(sere.get());
        return String.format("Some place in %s : %s.", ser.getLongitude(), ser.getLatitude());
    }

    public Service convertEntityToService(ServiceEntity sere) {
        Service ser = new Service();

        ser.setId(sere.getId());
        ser.setDate(sere.getDate());
        ser.setEmployeeId(sere.getEmployeeId());
        ser.setLatitude(sere.getLatitude());
        ser.setLongitude(sere.getLongitude());
        ser.setName(sere.getName());
        return ser;
    }

    public ServiceEntity convertServiceToEntity(Service ser) {
        ServiceEntity sere = new ServiceEntity();
        if (ser.getId() != -1) {
            sere.setId(ser.getId());
        }
        sere.setDate(ser.getDate());
        sere.setEmployeeId(ser.getEmployeeId());
        sere.setLatitude(ser.getLatitude());
        sere.setLongitude(ser.getLongitude());
        sere.setName(ser.getName());
        return sere;
    }

}
