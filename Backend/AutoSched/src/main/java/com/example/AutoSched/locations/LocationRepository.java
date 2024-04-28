package com.example.AutoSched.locations;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Locations, Integer> {

    Locations findById(int id);

}
