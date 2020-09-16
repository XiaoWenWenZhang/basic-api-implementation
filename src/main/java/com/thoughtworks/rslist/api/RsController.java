package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
    private List<RsEvent> rsList = initRsEventList();

    private List<RsEvent> initRsEventList() {
        List<RsEvent> rsEventList = new ArrayList<>();
        rsEventList.add(new RsEvent("第一条事件", "无标签"));
        rsEventList.add(new RsEvent("第二条事件", "无标签"));
        rsEventList.add(new RsEvent("第三条事件", "无标签"));
        return rsEventList;
    }


    @GetMapping("/rs/{index}")
    public RsEvent getOneRsEvent(@PathVariable int index) {
        return rsList.get(index - 1);
    }

    @GetMapping("/rs/list")
    public List<RsEvent> getRsEventBetween(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
        if (start == null || end == null) {
            return rsList;
        }
        return rsList.subList(start - 1, end);
    }

    @PostMapping("/rs/event")
    public void addRsEvent(@RequestBody String rsEvent) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        RsEvent event = objectMapper.readValue(rsEvent, RsEvent.class);
        rsList.add(event);
    }

    @PatchMapping("/rs/update/{index}")
    public void updateRsEvent(@PathVariable Integer index, @RequestBody RsEvent rsEvent) {
        RsEvent event = rsList.get(index);
        if(rsEvent.getEventName()!=null){
            event.setEventName(rsEvent.getEventName());
        }
        if(rsEvent.getKeyWord()!=null){
            event.setKeyWord(rsEvent.getKeyWord());
        }
    }

    @DeleteMapping("/rs/delete")
    public void deleteRsEvent(@RequestParam int index){
        rsList.remove(index);
    }

}
