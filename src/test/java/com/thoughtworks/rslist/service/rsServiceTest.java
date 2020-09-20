package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class rsServiceTest {
    RsService rsService;
    @Mock
    RsEventRepository rsEventRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    VoteRepository voteRepository;
    UserPO userPo;
    RsEventPO rsEventPo;
    Vote vote;


    @BeforeEach
    public void setUp() {
        userPo = UserPO.builder()
                .name("amy")
                .age(20)
                .email("a@b.com")
                .gender("female")
                .phone("12345678987")
                .voteNumber(10)
                .build();
        rsEventPo = RsEventPO.builder()
                .userPO(userPo)
                .eventName("国庆节放八天假")
                .keyWord("节日")
                .voteNum(0)
                .build();
        rsService = new RsService(userRepository, rsEventRepository, voteRepository);


    }

    @Test
    public void should_vote_success() {
        vote = Vote.builder()
                .voteNum(2)
                .userId(userPo.getId())
                .rsEventId(rsEventPo.getId())
                .voteTime(LocalDateTime.now())
                .build();
        when(rsEventRepository.findById(anyInt())).thenReturn(Optional.of(rsEventPo));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(userPo));
        rsService.vote(rsEventPo.getId(),vote);

        verify(voteRepository).save(VotePO.builder()
                .userPO(userPo)
                .rsEventPO(rsEventPo)
                .voteTime(vote.getVoteTime())
                .voteNum(2)
                .build());
        verify(rsEventRepository).save(rsEventPo);
        verify(userRepository).save(userPo);
        assertEquals(userPo.getVoteNumber(),8);
        assertEquals(rsEventPo.getVoteNum(), 2);
    }


}
