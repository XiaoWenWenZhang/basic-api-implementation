package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    UserPO userPO1;
    UserPO userPO2;
    RsEventPO rsEventPO1;
    RsEventPO rsEventPO2;
    RsEventPO rsEventPO3;
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
        rsEventPO3 = RsEventPO.builder()
                .eventName("天冷了")
                .keyWord("天气")
                .userPO(userPO1)
                .build();
        objectMapper = new ObjectMapper();

    }

    @Test
    @Order(1)
    public void should_register_user() throws Exception {
        User user = new User("lili", "famale", 19, "a@thoughtworks.com", "18888888888");
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<UserPO> userRepositoryAll = userRepository.findAll();
        assertEquals(1, userRepositoryAll.size());
        assertEquals("lili", userRepositoryAll.get(0).getName());
        assertEquals("a@thoughtworks.com", userRepositoryAll.get(0).getEmail());
    }

    @Test
    @Order(2)
    public void should_get_user_when_give_id() throws Exception {
        userRepository.save(userPO1);
        mockMvc.perform(get("/user/{id}", userPO1.getId()))
                .andExpect(jsonPath("$.name", is("amy")))
                .andExpect(jsonPath("$.age", is(20)))
                .andExpect(jsonPath("$.email", is("a@thoughtworks.com")))
                .andExpect(jsonPath("$.gender", is("male")))
                .andExpect(jsonPath("$.phone", is("18888888888")))
                .andExpect(jsonPath("$.voteNumber", is(10)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void should_get_user_fail_when_give_id_is_not_exist() throws Exception {
        userRepository.save(userPO1);
        mockMvc.perform(get("/user/{id}", userPO2.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid index")));

    }

    @Test
    @Order(4)
    public void should_get_users() throws Exception {
        userRepository.save(userPO1);
        userRepository.save(userPO2);
        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].age", is(20)))
                .andExpect(jsonPath("$[0].name", is("amy")))
                .andExpect(jsonPath("$[1].gender", is("female")))
                .andExpect(jsonPath("$[1].email", is("a@b.com")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    public void should_delete_user_when_give_id() throws Exception {
        userRepository.save(userPO1);
        rsEventRepository.save(rsEventPO1);
        rsEventRepository.save(rsEventPO3);
        mockMvc.perform(delete("/user/{id}", userPO1.getId()));
        assertEquals(0, userRepository.findAll().size());
        assertEquals(0, rsEventRepository.findAll().size());
    }

    @Test
    @Order(6)
    public void should_name_suit_format() throws Exception {
        User user = new User("xiaowangwang", "famale", 19, "a@thoughtworks.com", "18888888888");
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid user")));
    }

    @Test
    @Order(7)
    public void should_age_between_18_and_100() throws Exception {
        User user = new User("xiaowang", "famale", 10, "a@thoughtworks.com", "18888888888");
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid user")));
    }

    @Test
    @Order(8)
    public void should_email_suit_format() throws Exception {
        User user = new User("xiaowang", "famale", 19, "thoughtworks.com", "18888888888");
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid user")));
    }

    @Test
    @Order(9)
    public void should_phone_suit_format() throws Exception {
        User user = new User("xiaowang", "famale", 19, "a@thoughtworks.com", "1888888888899");
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid user")));
    }




}