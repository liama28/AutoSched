package com.experiment3.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity

public class Groupies {
    @Id
    @GeneratedValue
    private int id;
    private String groupName;

    @OneToMany(targetEntity = User.class,cascade = CascadeType.ALL)
    @JoinColumn(name = "gu_fk",referencedColumnName = "id")
    private List<User> userList;


}
