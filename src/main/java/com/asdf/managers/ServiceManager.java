package com.asdf.managers;

import com.asdf.dataObjects.service.Service;
import com.asdf.dataObjects.service.ServiceDto;
import com.asdf.dataObjects.service.ServiceEntity;
import com.asdf.database.ServiceRepository;
import com.asdf.exceptions.rest.InternalServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ServiceManager {
    @Autowired
    EntityManager entityManager;
    @Autowired
    private ServiceRepository serviceRepository;

    public Service addService(Service service) {
        ServiceEntity sere = convertServiceToEntity(service);
        ServiceEntity res = serviceRepository.save(sere);
        return convertEntityToService(res);
    }

    public List<Service> getServices() {
        List<Service> sers = new ArrayList<>();
        for (ServiceEntity sere : serviceRepository.findAllOrderById()) {
            sers.add(convertEntityToService(sere));
        }
        return sers;
    }


    public List<Service> getServices(int skip, int amount) {
        List<Service> sers = new ArrayList<>();

        CriteriaBuilder criteriaBuilder = entityManager
                .getCriteriaBuilder();
        CriteriaQuery<ServiceEntity> criteriaQuery = criteriaBuilder
                .createQuery(ServiceEntity.class);
        Root<ServiceEntity> from = criteriaQuery.from(ServiceEntity.class);
        CriteriaQuery<ServiceEntity> select = criteriaQuery.select(from)
                .orderBy(criteriaBuilder.asc(from.get("id")));
        TypedQuery<ServiceEntity> typedQuery = entityManager.createQuery(select);
        typedQuery.setFirstResult(skip);
        typedQuery.setMaxResults(amount);
        List<ServiceEntity> resultList = typedQuery.getResultList();

        for (ServiceEntity sere : resultList) {
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
        sere.setDate(ser.getDate());
        sere.setEmployeeId(ser.getEmployeeId());
        sere.setLatitude(ser.getLatitude());
        sere.setLongitude(ser.getLongitude());
        sere.setName(ser.getName());
        return sere;
    }

    public void deleteAllServiceResources() {
        serviceRepository.deleteAll();
    }

    public long countServices() {
        return serviceRepository.count();
    }

    public void addServices_Mock(int amount, List<Integer> validIds) {
        int currIDX = 0;
        String url = String.format("https://api.mockaroo.com/api/73135ab0?count=%d&key=e507b8a0", amount);
        RestTemplate restTemplate = new RestTemplate();
        try {
            Service[] response = restTemplate.getForObject(
                    url,
                    Service[].class);
            for (Service emp : response) {
                if (validIds.size() > 0) {
                    if (emp.getEmployeeId() == -1) {
                        emp.setEmployeeId(validIds.get(currIDX++));
                        if (currIDX >= validIds.size()) {
                            currIDX = 0;
                        }
                    }
                } else {
                    emp.setEmployeeId(-1);
                }
                addService(emp);
            }
        } catch (RestClientResponseException e) {
            throw new InternalServerException(e);
        }
    }
}
