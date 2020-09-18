package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class RsController {

    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/rs/{id}")
    public ResponseEntity getOneRsEvent(@PathVariable int id) {
        return ResponseEntity.ok(rsEventRepository.findById(id));
    }

    @GetMapping("/rs/list")
    public ResponseEntity<List<RsEventPO>> getRsEvents() {
        return ResponseEntity.ok(rsEventRepository.findAll());
    }

    @PostMapping("/rs/event")
    public ResponseEntity addRsEvent(@RequestBody @Validated RsEvent rsEvent) throws IOException {
        Optional<UserPO> userPO = userRepository.findById(rsEvent.getUserId());

        if (!userPO.isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        RsEventPO rsEventPO = RsEventPO.builder()
                .eventName(rsEvent.getEventName())
                .keyWord(rsEvent.getKeyWord())
                .userPO(userPO.get())
                .build();
        rsEventRepository.save(rsEventPO);
        return ResponseEntity.created(null).header("index", String.valueOf(rsEventPO.getId())).build();
    }

    @DeleteMapping("/rs/delete/{id}")
    public ResponseEntity deleteRsEvent(@PathVariable int id) {
        rsEventRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }



//    @PatchMapping("/rs/update/{index}")
//    public ResponseEntity updateRsEvent(@PathVariable Integer index, @RequestBody RsEvent rsEvent) {
//        RsEvent event = rsList.get(index);
//        if (rsEvent.getEventName() != null) {
//            event.setEventName(rsEvent.getEventName());
//        }
//        if (rsEvent.getKeyWord() != null) {
//            event.setKeyWord(rsEvent.getKeyWord());
//        }
//        return ResponseEntity.ok().build();
//    }


//
//    @ExceptionHandler({RsEventNotValidException.class, MethodArgumentNotValidException.class})
//    public ResponseEntity rsExceptionHandler(Exception e) {
//        String errorMessage;
//        if (e instanceof MethodArgumentNotValidException) {
//            errorMessage = "invalid param";
//        } else {
//            errorMessage = e.getMessage();
//        }
//        Error error = new Error();
//        error.setError(errorMessage);
//        return ResponseEntity.badRequest().body(error);
//    }
//
}
