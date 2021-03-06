package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.po.RsEventPO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RsEventRepository extends CrudRepository<RsEventPO,Integer> {
    @Override
    List<RsEventPO> findAll();

//    @Query("select b from RsEventPO b where b.id = ?1 ")
//    RsEventPO findByRsEventId(int id);

//    @Transactional
//    void deleteAllByUserId(int id);
}
