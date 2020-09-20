package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;

import java.util.Optional;

public class RsService {
    final UserRepository userRepository;
    final RsEventRepository rsEventRepository;
    final VoteRepository voteRepository;

    public RsService(UserRepository userRepository, RsEventRepository rsEventRepository, VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.rsEventRepository = rsEventRepository;
        this.voteRepository = voteRepository;
    }

    public void vote(int rsEventId, Vote vote) {
        Optional<RsEventPO> rsEventPO= rsEventRepository.findById(rsEventId);
        Optional<UserPO> userPO = userRepository.findById(vote.getUserId());
        if (!rsEventPO.isPresent() || !userPO.isPresent()
                || vote.getVoteNum() > userPO.get().getVoteNumber()) {
            throw new RsEventNotValidException("invalid index");
        }
        VotePO votePo =
                VotePO.builder()
                        .userPO(userPO.get())
                        .rsEventPO(rsEventPO.get())
                        .voteTime(vote.getVoteTime())
                        .voteNum(vote.getVoteNum())
                        .build();
        voteRepository.save(votePo);
        UserPO newUserPo = userPO.get();
        newUserPo.setVoteNumber(newUserPo.getVoteNumber()-vote.getVoteNum());
        userRepository.save(newUserPo);
        RsEventPO newRsEventPo = rsEventPO.get();
        newRsEventPo.setVoteNum(newRsEventPo.getVoteNum()+vote.getVoteNum());
        rsEventRepository.save(newRsEventPo);
    }
}
