package com.wimpy.dao;

import com.wimpy.dao.entity.MtgCard;
import com.wimpy.dao.entity.MtgHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MtgCardCrudDao extends CrudRepository<MtgCard, Long> {


    List<MtgCard> findAll();


    Optional<MtgCard> findByName(String name);

}