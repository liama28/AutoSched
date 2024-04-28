package com.vincentq.experiment6;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface OccasionRepository extends CrudRepository<Occasions, Integer> {

}
