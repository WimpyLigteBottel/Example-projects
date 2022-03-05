package com.wimpy.dao;

import com.wimpy.dao.entity.MtgCard;
import com.wimpy.dao.entity.MtgHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MtgHistoryCrudDao extends CrudRepository<MtgHistory, Long> {


    List<MtgHistory> findByMtgCard(MtgCard mtgCard);

}