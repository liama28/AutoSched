package com.example.AutoSched.Websocket;

import com.example.AutoSched.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepo extends JpaRepository<Message, Long>{
}
