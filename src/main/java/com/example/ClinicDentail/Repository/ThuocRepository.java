package com.example.ClinicDentail.Repository;

import com.example.ClinicDentail.Enity.Thuoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThuocRepository extends JpaRepository<Thuoc, Integer> {

}
