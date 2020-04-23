package com.asdf.dataServices;

import com.asdf.dataObjects.employee.EmployeeResource;
import com.asdf.exceptions.rest.InternalServerException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class EmployeeDataService {
    private static final String EMPLOYEE_URL = "https://employee-microservice.herokuapp.com/serviceBackend/employees/";

    public EmployeeResource getEmployee(int emp_id) {
        RestTemplate restTemplate = new RestTemplate();
        String url = EMPLOYEE_URL + emp_id;
        try {
            EmployeeResource response = restTemplate.getForObject(
                    url,
                    EmployeeResource.class);
            return response;
        } catch (RestClientResponseException e) {
            throw new InternalServerException(e);
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
}
