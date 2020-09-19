package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;


    @PostMapping("/user")
    public ResponseEntity<Void> addUser(@RequestBody @Valid User user) {
        UserPO userPO = new UserPO();
        userPO.setAge(user.getAge());
        userPO.setEmail(user.getEmail());
        userPO.setGender(user.getGender());
        userPO.setName(user.getName());
        userPO.setPhone(user.getPhone());
        userPO.setVoteNumber(user.getVoteNumber());
        userRepository.save(userPO);
        return ResponseEntity.created(null).header("index", String.valueOf(userPO.getId())).build();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Optional<UserPO>> getUserById(@PathVariable int id) {
        Optional<UserPO> userPO = userRepository.findById(id);
        if (!userPO.isPresent()) throw new RsEventNotValidException("invalid index");
        return ResponseEntity.ok(userPO);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserPO>> getUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }



    @DeleteMapping("/user/{id}")
    public ResponseEntity deleteUser(@PathVariable int id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }




//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity rsExceptionHandler(Exception e) {
//        Error error = new Error();
//        error.setError("invalid user");
//        return ResponseEntity.badRequest().body(error);
//    }

}




