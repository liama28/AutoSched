package com.example.AutoSched.Websocket;


import com.example.AutoSched.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "msgs")
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

  //  @ManyToOne
  //  @JoinColumn(name = "user_id")
  //  @NotNull
  //  private User user;

    @NotNull
    @Column
    private String name;

    @NotNull
    @Lob
    private String content;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sent")
    private Date sent = new Date();


    public Message() {};

    public Message( String name, String content) {
  //      this.user = user;
        this.name = name;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSent() {
        return sent;
    }

    public void setSent(Date sent) {
        this.sent = sent;
    }

 //   public User getUser() {
  //      return user;
 //   }

  //  public void setUser(User user) {
  //      this.user = user;
  //  }
}

