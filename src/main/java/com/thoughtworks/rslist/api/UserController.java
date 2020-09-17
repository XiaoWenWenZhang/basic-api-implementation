package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    //List<User> userList = new ArrayList<>();
    private List<User> userList = initUserList();
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;


        private List<User> initUserList() {
        List<User> rsUserList = new ArrayList<>();
        rsUserList.add(new User("xiaowang", "famale", 19, "a@thoughtworks.com", "18888888888"));
        rsUserList.add(new User("wendy", "male", 30, "a@thoughtworks.com", "18888888888"));
        return rsUserList;
    }
    @PostMapping("/user")
    public ResponseEntity addUser(@RequestBody @Valid User user) {
        UserPO userPO = new UserPO();
        userPO.setAge(user.getAge());
        userPO.setEmail(user.getEmail());
        userPO.setGender(user.getGender());
        userPO.setName(user.getName());
        userPO.setPhone(user.getPhone());
        userPO.setVoteNumber(user.getVoteNumber());
        userRepository.save(userPO);
        userList.add(user);
        return ResponseEntity.created(null).header("index", String.valueOf(userList.indexOf(user))).build();
//        userList.add(user);
//        return ResponseEntity.created(null).header("index",String.valueOf(userList.indexOf(user))).build();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity getUserById(@PathVariable int id) {
        Optional<UserPO> byId = userRepository.findById(id);
        return ResponseEntity.ok(byId);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity deleteUser(@PathVariable int id) {
        userRepository.deleteById(id);
//        rsEventRepository.deleteAllByUserId(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user")
    public ResponseEntity<List<User>> getUserList() {

        return ResponseEntity.ok(userList);
    }

    @GetMapping("/users")
    public ResponseEntity getUsers() throws JsonProcessingException {
        List<String> userStringList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        for(int i=0;i<userList.size();i++){
            User user = userList.get(i);
            userStringList.add(objectMapper.writeValueAsString(user));
        }
        return ResponseEntity.ok(userStringList);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity rsExceptionHandler(Exception e) {
        Error error = new Error();
        error.setError("invalid user");
        return ResponseEntity.badRequest().body(error);
    }

}





