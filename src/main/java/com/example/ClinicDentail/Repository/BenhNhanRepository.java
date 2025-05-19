package com.example.ClinicDentail.Repository;

import com.example.ClinicDentail.Enity.BenhNhan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BenhNhanRepository extends JpaRepository<BenhNhan,Integer> {
}
