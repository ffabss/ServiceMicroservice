# REST - API for our [Service-Manager](https://service-manager.web.app/)

The optional request parameter boolean `address` indicates whether the address should be included or not.

## Services - https://service-microservice.herokuapp.com/serviceBackend

### /services

* __GET__ returns all services in form of [ServiceResources](#Service)
* __POST__ adds a service from the request body in form of a [ServiceDto](#Service) and returns the the saved [ServiceResource](#Service)
* __DELETE__ deletes all services and returns them in form of [ServiceResources](#Service)

### /services/{skip}/{amount}

* __GET__ skips the first `skip` services and then returns the `amount` following services in form of [ServiceResources](#Service)

### /services/{id}

* __GET__ returns the service with the specified `id` in form of a [ServiceResource](#Service)
* __PUT__ saves the service from the request body, in form of a [ServiceDto](#Service), with the specified `id` and returns the old service with the specified `id` in form of a [ServiceResource](#Service)
* __DELETE__ removes the service with the specified `id` and returns it in form of a [ServiceResource](#Service)

### /countServices

* __GET__ returns the number of available services 

## Classes

### Address from [LocationIQ](https://locationiq.com/docs-html/index.html#reverse_response)
    public class AddressResource {
        private String house_number;
        private String road;
        private String hamlet;
        private String village;
        private String county;
        private String state;
        private String postcode;
        private String country;
        private String country_code;
        private String town;
        private String street;
        private String city;
    }

### Employee
    public class EmployeeResource {
        private int id;
        private String name;
        private AddressResource address;
        private String latitude;
        private String longitude;
    }

### Service
    public class ServiceDto {
        private String name;
        private int employeeId;
        private Date date;
        private String address;
    }

    public class ServiceResource {
        private int id;
        private String name;
        private EmployeeResource employee;
        private Date date;
        private AddressResource address;
        private String latitude;
        private String longitude;
    }
