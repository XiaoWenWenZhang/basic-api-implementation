package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
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
    public ResponseEntity<Optional<RsEventPO>> getOneRsEvent(@PathVariable int id) {
        Optional<RsEventPO> rsEventPO = rsEventRepository.findById(id);
        if(!rsEventPO.isPresent()) throw new RsEventNotValidException("invalid index");
        return ResponseEntity.ok(rsEventPO);
    }

    @GetMapping("/rs/list")
    public ResponseEntity<List<RsEventPO>> getRsEvents() {
        return ResponseEntity.ok(rsEventRepository.findAll());
    }

    @PostMapping("/rs/event")
    public ResponseEntity<Void> addRsEvent(@RequestBody @Validated RsEvent rsEvent) throws IOException {
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



    @PatchMapping("/rs/update/{id}")
    public ResponseEntity<Void> updateRsEvent(@PathVariable int id, @RequestBody RsEvent rsEvent) {
        Optional<RsEventPO> currentRsEventPO = rsEventRepository.findById(id);
        if(!currentRsEventPO.isPresent()) throw new RsEventNotValidException("invalid index");
        if(currentRsEventPO.get().getUserPO().getId()!=rsEvent.getUserId()){
            return ResponseEntity.badRequest().build();
        }
        rsEventRepository.deleteById(id);
        if (rsEvent.getEventName() != null) {
            currentRsEventPO.get().setEventName(rsEvent.getEventName());
        }
        if (rsEvent.getKeyWord() != null) {
            currentRsEventPO.get().setKeyWord(rsEvent.getKeyWord());
        }
        rsEventRepository.save(currentRsEventPO.get());
        return ResponseEntity.ok().build();
    }


}
