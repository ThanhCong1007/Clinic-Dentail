package com.example.ClinicDentail.Repository;

import com.example.ClinicDentail.Enity.BacSi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BacSiRepository extends JpaRepository<BacSi,Integer> {
}
