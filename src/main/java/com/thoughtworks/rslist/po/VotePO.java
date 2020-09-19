package com.thoughtworks.rslist.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vote")
public class VotePO {
    @Id
    @GeneratedValue
    private int id;
    private int voteNum;
    private LocalDateTime voteTime;
    @ManyToOne
    private UserPO userPO;
    @ManyToOne
    private RsEventPO rsEventPO;

}
