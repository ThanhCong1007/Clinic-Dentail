package com.example.ClinicDentail.Repository;

import com.example.ClinicDentail.Enity.BenhAn;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;


@Repository
public interface BenhAnrepository extends JpaRepository<BenhAn,Integer> {
    Page<BenhAn> findByBenhNhan(Integer maBenhNhan, Pageable pageable);
}
