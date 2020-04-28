package com.asdf.database;

import com.asdf.dataObjects.service.ServiceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ServiceRepository extends CrudRepository<ServiceEntity, Integer> {
    @Query("SELECT s FROM ServiceEntity s ORDER BY s.id ASC")
    List<ServiceEntity> findAllOrderById();
}