package com.example.ClinicDentail.Service;

import com.example.ClinicDentail.Enity.NguoiDung;
import com.example.ClinicDentail.Repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Override
    public UserDetails loadUserByUsername(String tenDangNhap) throws UsernameNotFoundException {
        NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(tenDangNhap)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng"));

        return new org.springframework.security.core.userdetails.User(
                nguoiDung.getTenDangNhap(),
                nguoiDung.getMatKhau(),
                nguoiDung.getTrangThaiHoatDong(),
                true, true, true,
                List.of(new SimpleGrantedAuthority("ROLE_" + nguoiDung.getVaiTro().getTenVaiTro()))
        );
    }
}
