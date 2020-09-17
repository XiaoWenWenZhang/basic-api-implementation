package com.thoughtworks.rslist.domain;

import com.thoughtworks.rslist.po.UserPO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


public class RsEvent {
    @NotNull
    private String eventName;
    @NotNull
    private String keyWord;
    @Valid
    @NotNull
    private UserPO userPO;

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

    public UserPO getUserPO() {
        return userPO;
    }

    public void setUserPO(UserPO userPO) {
        this.userPO = userPO;
    }

    public RsEvent(@NotNull String eventName, @NotNull String keyWord, @Valid @NotNull UserPO userPO) {
        this.eventName = eventName;
        this.keyWord = keyWord;
        this.userPO = userPO;
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
