package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
    private List<RsEvent> rsList = initRsEventList();
    private List<User> userList = initUserList();
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    UserRepository userRepository;


    private List<RsEvent> initRsEventList() {
        User user = new User("xiaowang", "famale", 19, "a@thoughtworks.com", "18888888888");
        UserPO userPO = UserPO.builder().age(30).email("a@thoughtworks.com")
                .gender("male").phone("18888888888").name("amy").voteNumber(10).build();
        List<RsEvent> rsEventList = new ArrayList<>();
        rsEventList.add(new RsEvent("第一条事件", "无标签", userPO));
        rsEventList.add(new RsEvent("第二条事件", "无标签", userPO));
        rsEventList.add(new RsEvent("第三条事件", "无标签", userPO));
        return rsEventList;
    }

    private List<User> initUserList() {
        List<User> rsUserList = new ArrayList<>();
        rsUserList.add(new User("xiaowang", "famale", 19, "a@thoughtworks.com", "18888888888"));
        rsUserList.add(new User("wendy", "male", 30, "a@thoughtworks.com", "18888888888"));
        return rsUserList;
    }


    @GetMapping("/rs/{index}")
    public ResponseEntity getOneRsEvent(@PathVariable int index) {
        if (index <= 0 || index > rsList.size()) {
            throw new RsEventNotValidException("invalid index");
        }
        return ResponseEntity.ok(rsList.get(index - 1));
    }

    @GetMapping("/rs/list")
    public ResponseEntity<List<RsEvent>> getRsEventBetween(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {

        if (start == null || end == null) {
            return ResponseEntity.ok(rsList);
        }
        if (start <= 0 || start > rsList.size()) throw new RsEventNotValidException("invalid request param");
        if (end <= 0 || end > rsList.size()) throw new RsEventNotValidException("invalid request param");
        return ResponseEntity.ok(rsList.subList(start - 1, end));
    }

    @PostMapping("/rs/event")
    public ResponseEntity addRsEvent(@RequestBody @Validated RsEvent rsEvent) throws IOException {
        if (!userRepository.findById(rsEvent.getUserPO().getId()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        RsEventPO rsEventPO = RsEventPO.builder().eventName(rsEvent.getEventName()).keyWord(rsEvent.getKeyWord()).userPO(rsEvent.getUserPO()).build();
        rsEventRepository.save(rsEventPO);
        return ResponseEntity.created(null).header("index", String.valueOf(rsEvent.getUserPO().getId())).build();
//        User rsUser = rsEvent.getUser();
//        for (int i = 0; i < userList.size(); i++) {
//            if (!userList.get(i).getName().equals(rsUser.getName())) {
//                userList.add(rsUser);
//            }
//        }
//        rsList.add(rsEvent);
//        return ResponseEntity.created(null).header("index", String.valueOf(userList.indexOf(rsUser))).build();
    }

    @PatchMapping("/rs/update/{index}")
    public ResponseEntity updateRsEvent(@PathVariable Integer index, @RequestBody RsEvent rsEvent) {
        RsEvent event = rsList.get(index);
        if (rsEvent.getEventName() != null) {
            event.setEventName(rsEvent.getEventName());
        }
        if (rsEvent.getKeyWord() != null) {
            event.setKeyWord(rsEvent.getKeyWord());
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/rs/delete")
    public ResponseEntity deleteRsEvent(@RequestParam int id) {
        rsEventRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler({RsEventNotValidException.class, MethodArgumentNotValidException.class})
    public ResponseEntity rsExceptionHandler(Exception e) {
        String errorMessage;
        if (e instanceof MethodArgumentNotValidException) {
            errorMessage = "invalid param";
        } else {
            errorMessage = e.getMessage();
        }
        Error error = new Error();
        error.setError(errorMessage);
        return ResponseEntity.badRequest().body(error);
    }

}
