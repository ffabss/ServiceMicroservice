package com.asdf.database;

import com.asdf.dataObjects.service.ServiceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface ServiceRepository extends CrudRepository<ServiceEntity, Integer> {
}