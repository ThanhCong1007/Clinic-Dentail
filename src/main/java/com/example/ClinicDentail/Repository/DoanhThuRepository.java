package com.example.ClinicDentail.Repository;

import com.example.ClinicDentail.Enity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoanhThuRepository extends JpaRepository<HoaDon, Integer> {

    @Query(value = "CALL sp_TongKetDoanhThu(:loaiThongKe, :nam, :thang, :quy)",
            nativeQuery = true)
    List<Object[]> getTongKetDoanhThu(
            @Param("loaiThongKe") String loaiThongKe,
            @Param("nam") Integer nam,
            @Param("thang") Integer thang,
            @Param("quy") Integer quy
    );

    @Query(value = "CALL sp_DoanhThuTheoBacSi(:loaiThongKe, :nam, :thang, :quy, :maBacSi)",
            nativeQuery = true)
    List<Object[]> getDoanhThuTheoBacSi(
            @Param("loaiThongKe") String loaiThongKe,
            @Param("nam") Integer nam,
            @Param("thang") Integer thang,
            @Param("quy") Integer quy,
            @Param("maBacSi") Integer maBacSi
    );
}
