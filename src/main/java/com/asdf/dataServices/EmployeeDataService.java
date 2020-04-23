package com.asdf.dataServices;

import antlr.collections.impl.LList;
import com.asdf.dataObjects.employee.EmployeeResource;
import com.asdf.exceptions.ResourceNotFoundException;
import com.asdf.exceptions.rest.InternalServerException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class EmployeeDataService {
    private static final String EMPLOYEE_URL = "https://employee-microservice.herokuapp.com/serviceBackend/";

    public EmployeeResource getEmployee(int emp_id) throws ResourceNotFoundException {
        RestTemplate restTemplate = new RestTemplate();
        String url = EMPLOYEE_URL + "employees/" + emp_id;
        try {
            EmployeeResource response = restTemplate.getForObject(
                    url,
                    EmployeeResource.class);
            return response;
        } catch (RestClientResponseException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    public List<EmployeeResource> getAllEmployees() {
        RestTemplate restTemplate = new RestTemplate();
        String url = EMPLOYEE_URL + "employees";
        try {
            EmployeeResource[] response = restTemplate.getForObject(
                    url,
                    EmployeeResource[].class);
            return Arrays.asList(response);
        } catch (RestClientResponseException e) {
            throw new InternalServerException(e);
        }
    }

    public List<Integer> getValidIds() {
        RestTemplate restTemplate = new RestTemplate();
        String url = EMPLOYEE_URL + "validIds";
        try {
            Integer[] response = restTemplate.getForObject(
                    url,
                    Integer[].class);
            return response == null ? new ArrayList() : Arrays.asList(response);
        } catch (RestClientResponseException e) {
            throw new InternalServerException(e);
        }
    }
}
