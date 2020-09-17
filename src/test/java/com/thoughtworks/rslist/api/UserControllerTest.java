package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.UserPO;
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

    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void should_register_user() throws Exception {
//        User user = new User("xiaowang", "famale", 19, "a@thoughtworks.com", "18888888888");
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonString = objectMapper.writeValueAsString(user);
//        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());
//
//        mockMvc.perform(get("/user"))
//                .andExpect(jsonPath("$", hasSize(3)))
//                .andExpect(jsonPath("$[2].name", is("xiaowang")))
//                .andExpect(jsonPath("$[2].gender", is("famale")))
//                .andExpect(jsonPath("$[2].age", is(19)))
//                .andExpect(jsonPath("$[2].email", is("a@thoughtworks.com")))
//                .andExpect(jsonPath("$[2].phone", is("18888888888")))
//                .andExpect(status().isOk());

        User user = new User("xiaowang", "famale", 19, "a@thoughtworks.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<UserPO> userRepositoryAll = userRepository.findAll();
        assertEquals(1,userRepositoryAll.size());
        assertEquals("xiaowang",userRepositoryAll.get(0).getName());
        assertEquals("a@thoughtworks.com",userRepositoryAll.get(0).getEmail());
    }

    @Test
    public void should_get_user_when_give_id() throws Exception {
        userRepository.save(UserPO.builder().age(30).email("a@thoughtworks.com")
                .gender("male").phone("18888888888").name("amy").voteNumber(10).id(3).build());
        mockMvc.perform(get("/user/3"))
                .andExpect(jsonPath("$.name", is("amy")))
                .andExpect(jsonPath("$.age",is(30)))
                .andExpect(jsonPath("$.email",is("a@thoughtworks.com")))
                .andExpect(jsonPath("$.gender",is("male")))
                .andExpect(jsonPath("$.phone",is("18888888888")))
                .andExpect(jsonPath("$.voteNumber",is(10)))
                .andExpect(status().isOk());

    }

    @Test
    public void should_delete_user_when_give_id() throws Exception {
        userRepository.save(UserPO.builder().age(30).email("a@thoughtworks.com")
                .gender("male").phone("18888888888").name("amy").voteNumber(10).id(13).build());
        mockMvc.perform(delete("/user/13"));
        List<UserPO> userRepositoryAll = userRepository.findAll();
        assertEquals(0,userRepositoryAll.size());
    }

    @Test
    @Order(2)
    public void should_name_suit_format() throws Exception {
        User user = new User("xiaowangwang", "famale", 19, "a@thoughtworks.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    public void should_age_between_18_and_100() throws Exception {
        User user = new User("xiaowang", "famale", 10, "a@thoughtworks.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(4)
    public void should_email_suit_format() throws Exception {
        User user = new User("xiaowang", "famale", 19, "thoughtworks.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(5)
    public void should_phone_suit_format() throws Exception {
        User user = new User("xiaowang", "famale", 19, "a@thoughtworks.com", "1888888888899");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    @Order(6)
    public void should_get_users() throws Exception {
        User user1 = new User("xiaowang", "famale", 19, "a@thoughtworks.com", "18888888888");
        User user2 = new User("wendy", "male", 30, "a@thoughtworks.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString1 = objectMapper.writeValueAsString(user1);
        String jsonString2 = objectMapper.writeValueAsString(user2);
        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]", is(jsonString1)))
                .andExpect(jsonPath("$[1]", is(jsonString2)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    public void should_throw_rs_user_not_valid_exception() throws Exception {
        User user = new User("xiaowangwang", "famale", 19, "a@thoughtworks.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));

    }


}