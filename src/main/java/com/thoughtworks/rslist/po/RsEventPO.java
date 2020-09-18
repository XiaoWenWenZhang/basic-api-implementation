package com.thoughtworks.rslist.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rsEvent")
public class RsEventPO {
    @Id
    @GeneratedValue
    private int id;
    private String eventName;
    private String keyWord;
    @ManyToOne
    @JsonIgnore
    private UserPO userPO;
}
