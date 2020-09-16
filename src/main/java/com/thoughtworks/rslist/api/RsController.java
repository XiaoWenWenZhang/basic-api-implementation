package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
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

    private List<RsEvent> initRsEventList() {
        User user = new User("xiaowang", "famale", 19, "a@thoughtworks.com", "18888888888");
        List<RsEvent> rsEventList = new ArrayList<>();
        rsEventList.add(new RsEvent("第一条事件", "无标签", user));
        rsEventList.add(new RsEvent("第二条事件", "无标签", user));
        rsEventList.add(new RsEvent("第三条事件", "无标签", user));
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
        if(start<=0||start>rsList.size()) throw new RsEventNotValidException("invalid request param");
        if(end<=0||end>rsList.size()) throw new RsEventNotValidException("invalid request param");
        return ResponseEntity.ok(rsList.subList(start - 1, end));
    }

    @PostMapping("/rs/event")
    public ResponseEntity addRsEvent(@RequestBody @Validated RsEvent rsEvent) throws IOException {
        User rsUser = rsEvent.getUser();
        for (int i = 0; i < userList.size(); i++) {
            if (!userList.get(i).getName().equals(rsUser.getName())) {
                userList.add(rsUser);
            }
        }
        rsList.add(rsEvent);
        return ResponseEntity.created(null).header("index", String.valueOf(userList.indexOf(rsUser))).build();
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
    public ResponseEntity deleteRsEvent(@RequestParam int index) {
        rsList.remove(index);
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
