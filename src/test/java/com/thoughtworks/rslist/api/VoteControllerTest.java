package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class VoteControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    VoteRepository voteRepository;
    UserPO userPO1;
    UserPO userPO2;
    RsEventPO rsEventPO1;
    RsEventPO rsEventPO2;
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
        voteRepository.deleteAll();

        userPO1 = UserPO.builder()
                .age(20)
                .email("a@thoughtworks.com")
                .gender("male")
                .phone("18888888888")
                .name("amy")
                .voteNumber(10)
                .build();
        userPO2 = UserPO.builder()
                .age(30)
                .email("a@b.com")
                .gender("female")
                .phone("17777777777")
                .name("xiaowang")
                .voteNumber(10)
                .build();
        rsEventPO1 = RsEventPO.builder()
                .eventName("小学生放假了")
                .keyWord("社会时事")
                .userPO(userPO1)
                .voteNum(3)
                .build();
        rsEventPO2 = RsEventPO.builder()
                .eventName("猪肉涨价了")
                .keyWord("经济")
                .userPO(userPO2)
                .voteNum(4)
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void should_get_vote_record() throws Exception {
        userRepository.save(userPO1);
        rsEventRepository.save(rsEventPO1);
        for (int i = 0; i < 8; i++) {
            VotePO votePO = VotePO.builder().voteTime(LocalDateTime.now()).rsEventPO(rsEventPO1).userPO(userPO1).voteNum(4).build();
            voteRepository.save(votePO);
        }

        mockMvc.perform(get("/voteRecord")
                .param("userId", String.valueOf(userPO1.getId()))
                .param("rsEventId", String.valueOf(rsEventPO1.getId()))
                .param("pageIndex", "1"))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].userId", is(userPO1.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventPO1.getId())))
                .andExpect(jsonPath("$[0].voteNum", is(4)))
                .andExpect(jsonPath("$[1].voteNum", is(4)))
                .andExpect(jsonPath("$[2].voteNum", is(4)))
                .andExpect(jsonPath("$[3].voteNum", is(4)))
                .andExpect(jsonPath("$[4].voteNum", is(4)));

    }


}
