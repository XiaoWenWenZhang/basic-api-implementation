package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class RsControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    UserRepository userRepository;
    UserPO userPO1;
    UserPO userPO2;
    RsEventPO rsEventPO1;
    RsEventPO rsEventPO2;
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
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
                .build();
        rsEventPO2 = RsEventPO.builder()
                .eventName("猪肉涨价了")
                .keyWord("经济")
                .userPO(userPO2)
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void should_get_rs_event_when_give_id() throws Exception {
        userRepository.save(userPO1);
        rsEventRepository.save(rsEventPO1);
        mockMvc.perform(get("/rs/{id}",rsEventPO1.getId()))
                .andExpect(jsonPath("$.eventName", is("小学生放假了")))
                .andExpect(jsonPath("$.keyWord", is("社会时事")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_get_rs_events() throws Exception {
        userRepository.save(userPO1);
        userRepository.save(userPO2);
        rsEventRepository.save(rsEventPO1);
        rsEventRepository.save(rsEventPO2);
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("小学生放假了")))
                .andExpect(jsonPath("$[0].keyWord", is("社会时事")))
                .andExpect(jsonPath("$[1].eventName", is("猪肉涨价了")))
                .andExpect(jsonPath("$[1].keyWord", is("经济")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_add_rs_event_when_user_exist() throws Exception {
        userRepository.save(userPO1);
        RsEvent rsEvent = new RsEvent("降温了", "天气", userPO1.getId());
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<RsEventPO> rsEvents = rsEventRepository.findAll();
        assertNotNull(rsEvent);
        assertEquals(1, rsEvents.size());
        assertEquals("降温了", rsEvents.get(0).getEventName());
        assertEquals("天气", rsEvents.get(0).getKeyWord());
    }

    @Test
    public void should_add_rs_event_fail_when_user_not_exist() throws Exception {
//        userRepository.save(userPO1);
        RsEvent rsEvent = new RsEvent("降温了", "天气", userPO1.getId());
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_delete_rs_event() throws Exception {
        userRepository.save(userPO1);
        rsEventRepository.save(rsEventPO1);
        mockMvc.perform(delete("/rs/delete/{id}", rsEventPO1.getId()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk());
    }


//    @Test
//    public void should_update_rs_event() throws Exception {
//        UserPO userPO = UserPO.builder().age(30).email("a@thoughtworks.com")
//            .gender("male").phone("18888888888").name("amy").voteNumber(10).id(30).build();
//        RsEvent rsEvent1 = new RsEvent("牛肉涨价了", null, userPO);
//        RsEvent rsEvent2 = new RsEvent("小学生放假了", "社会时事", userPO);
//        RsEvent rsEvent3 = new RsEvent(null, "政治", userPO);
//
//        String jsonString1 = "{\"eventName\":\"牛肉涨价了\",\"keyWord\":null,\"userId\":1}";
//        String jsonString2 = "{\"eventName\":\"小学生放假了\",\"keyWord\":\"社会时事\",\"userId\":2}";
//        String jsonString3 = "{\"eventName\":null,\"keyWord\":\"政治\",\"userId\":3}";
//
//        mockMvc.perform(patch("/rs/update/0").content(jsonString1).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//        mockMvc.perform(patch("/rs/update/1").content(jsonString2).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//        mockMvc.perform(patch("/rs/update/2").content(jsonString3).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//
////
////        mockMvc.perform(get("/rs/list"))
////                .andExpect(jsonPath("$", hasSize(3)))
////                .andExpect(jsonPath("$[0].eventName", is("牛肉涨价了")))
////                .andExpect(jsonPath("$[0].keyWord", is("无标签")))
////                .andExpect(jsonPath("$[1].eventName", is("小学生放假了")))
////                .andExpect(jsonPath("$[1].keyWord", is("社会时事")))
////                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
////                .andExpect(jsonPath("$[2].keyWord", is("政治")))
////                .andExpect(status().isOk());
//
//
//    }



//    @Test
//    public void should_throw_rs_event_not_valid_exception() throws Exception {
//
//        mockMvc.perform(get("/rs/{id}",))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.error", is("invalid index")));
//    }
//
//    @Test
//    public void should_throw_start_or_end_out_of_range_exception() throws Exception {
//        mockMvc.perform(get("/rs/list?start=0&end=1"))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.error", is("invalid request param")));
//    }
//
//    @Test
//    public void should_throw_method_argument_not_valid_exception() throws Exception {
//        User user = new User("xiaowangwang", "famale", 19, "a@thoughtworks.com", "18888888888");
//        UserPO userPO = UserPO.builder().age(30).email("a@thoughtworks.com")
//                .gender("male").phone("18888888888").name("amy").voteNumber(10).id(30).build();
//        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userPO);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonString = objectMapper.writeValueAsString(rsEvent);
//
//        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.error", is("invalid param")));
//
//    }


}