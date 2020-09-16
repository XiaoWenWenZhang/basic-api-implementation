package com.thoughtworks.rslist.domain;

import javax.validation.constraints.NotNull;


public class RsEvent {
    @NotNull
    private String eventName;
    @NotNull
    private String keyWord;
    @NotNull
    private User user;

 //   @JsonIgnore
    public User getUser() {
        return user;
    }

  //  @JsonProperty
    public void setUser(User user) {
        this.user = user;
    }

    public RsEvent() {
    }

    public RsEvent(String eventName, String keyWord, User user) {
        this.eventName = eventName;
        this.keyWord = keyWord;
        this.user = user;
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
