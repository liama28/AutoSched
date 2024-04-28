package com.vincentq.experiment4;

import com.vincentq.experiment4.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}
