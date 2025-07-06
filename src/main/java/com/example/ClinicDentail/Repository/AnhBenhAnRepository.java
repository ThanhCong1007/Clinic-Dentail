package com.example.ClinicDentail.Repository;

import com.example.ClinicDentail.Enity.AnhBenhAn;
import com.example.ClinicDentail.Enity.BenhAn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnhBenhAnRepository extends JpaRepository<AnhBenhAn,Integer> {
    List<AnhBenhAn> findByBenhAn_MaBenhAn(Integer maBenhAn);

    List<AnhBenhAn> findByBenhAn(BenhAn benhAn);
}
