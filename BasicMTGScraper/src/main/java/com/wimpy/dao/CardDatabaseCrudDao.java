package com.wimpy.dao;

import com.wimpy.dao.entity.MtgHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardDatabaseCrudDao extends CrudRepository<MtgHistory, Long> {

}