package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    //List<User> userList = new ArrayList<>();
    private List<User> userList = initUserList();

        private List<User> initUserList() {
        List<User> rsUserList = new ArrayList<>();
        rsUserList.add(new User("xiaowang", "famale", 19, "a@thoughtworks.com", "18888888888"));
        rsUserList.add(new User("wendy", "male", 30, "a@thoughtworks.com", "18888888888"));
        return rsUserList;
    }
    @PostMapping("/user")
    public ResponseEntity addUser(@RequestBody @Valid User user) {
        if(user.getName().length()>8) throw new RsEventNotValidException("invalid user");
        if(user.getAge()<18||user.getAge()>100) throw new RsEventNotValidException("invalid user");
        if(user.getEmail().matches("^[a-z0-9A-Z]+[- | a-z0-9A-Z . _]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\\\.)+[a-z]{2,}$")) throw new RsEventNotValidException("invalid user");
        if(user.getPhone().matches("1\\d{10}")) throw new RsEventNotValidException("invalid user");
        userList.add(user);
        return ResponseEntity.created(null).header("index",String.valueOf(userList.indexOf(user))).build();
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

    @ExceptionHandler({RsEventNotValidException.class, MethodArgumentNotValidException.class})
    public ResponseEntity rsExceptionHandler(Exception e) {
        String errorMessage;
        if (e instanceof MethodArgumentNotValidException) {
            errorMessage = "invalid user";
        } else {
            errorMessage = e.getMessage();
        }
        Error error = new Error();
        error.setError(errorMessage);
        return ResponseEntity.badRequest().body(error);
    }

}





