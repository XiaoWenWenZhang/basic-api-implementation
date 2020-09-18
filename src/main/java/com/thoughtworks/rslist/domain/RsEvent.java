package com.thoughtworks.rslist.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


public class RsEvent {
    @NotNull
    private String eventName;
    @NotNull
    private String keyWord;
    @Valid
    @NotNull
    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
////    @JsonIgnore
//    public User getUser() {
//        return user;
//    }
//
////    @JsonProperty
//    public void setUser(User user) {
//        this.user = user;
//    }



    public RsEvent() {
    }

    public RsEvent(@NotNull String eventName, @NotNull String keyWord, @Valid @NotNull int userId) {
        this.eventName = eventName;
        this.keyWord = keyWord;
        this.userId = userId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
