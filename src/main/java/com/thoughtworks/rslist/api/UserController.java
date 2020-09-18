package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;



//    @PostMapping("/user")
//    public ResponseEntity addUser(@RequestBody @Valid User user) {
//        UserPO userPO = new UserPO();
//        userPO.setAge(user.getAge());
//        userPO.setEmail(user.getEmail());
//        userPO.setGender(user.getGender());
//        userPO.setName(user.getName());
//        userPO.setPhone(user.getPhone());
//        userPO.setVoteNumber(user.getVoteNumber());
//        userRepository.save(userPO);
//        userList.add(user);
//        return ResponseEntity.created(null).header("index", String.valueOf(userList.indexOf(user))).build();
////        userList.add(user);
////        return ResponseEntity.created(null).header("index",String.valueOf(userList.indexOf(user))).build();
//    }

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







//    @GetMapping("/user")
//    public ResponseEntity<List<User>> getUserList() {
//
//        return ResponseEntity.ok(userList);
//    }

//    @GetMapping("/users")
//    public ResponseEntity getUsers() throws JsonProcessingException {
//        List<String> userStringList = new ArrayList<>();
//        ObjectMapper objectMapper = new ObjectMapper();
//        for(int i=0;i<userList.size();i++){
//            User user = userList.get(i);
//            userStringList.add(objectMapper.writeValueAsString(user));
//        }
//        return ResponseEntity.ok(userStringList);
//    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity rsExceptionHandler(Exception e) {
//        Error error = new Error();
//        error.setError("invalid user");
//        return ResponseEntity.badRequest().body(error);
//    }

}





