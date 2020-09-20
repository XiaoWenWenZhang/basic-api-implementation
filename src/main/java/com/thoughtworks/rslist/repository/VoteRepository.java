package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.po.VotePO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface VoteRepository extends PagingAndSortingRepository<VotePO, Integer> {
    @Override
    List<VotePO> findAll();

    @Query("select v from VotePO v where v.userPO.id = :userId and v.rsEventPO.id = :rsEventId")
    List<VotePO> findAcoordingToUserIdAndRsEventId(int userId, int rsEventId, Pageable pageable);
}
