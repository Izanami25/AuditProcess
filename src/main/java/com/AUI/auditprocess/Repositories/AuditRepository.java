package com.AUI.auditprocess.Repositories;

import com.AUI.auditprocess.Entities.AuditEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuditRepository extends CrudRepository<AuditEntity, Integer> {
    @Override
    List<AuditEntity> findAll();
}