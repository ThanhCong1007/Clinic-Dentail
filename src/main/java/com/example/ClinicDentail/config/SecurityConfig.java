//package com.example.ClinicDentail.config;
//import com.javaweb.student_score_management.service.implement.CustomUserDetailsSerImplement;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.ProviderManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    private final CustomUserDetailsSerImplement customUserDetailsService;
//    private final CustomSuccessHandler customSuccessHandler;
//
//    public SecurityConfig(CustomUserDetailsSerImplement customUserDetailsService, CustomSuccessHandler customSuccessHandler) {
//        this.customUserDetailsService = customUserDetailsService;
//        this.customSuccessHandler = customSuccessHandler;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(authorize -> authorize
//                        // Cho phép truy cập công khai vào các tài nguyên tĩnh và trang login
//                        .requestMatchers("/login", "/logout", "/css/**", "/js/**", "/images/**", "/vendor/**").permitAll()
//                        // Chỉ Admin được truy cập các endpoint /admin/**
//                        .requestMatchers("/**").permitAll()
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        // Chỉ GiangVien được truy cập các endpoint /giangvien/**
//                        .requestMatchers("/giangvien/**").hasRole("GIANGVIEN")
//                        // Chỉ SinhVien được truy cập các endpoint /sinhvien/**
//                        .requestMatchers("/sinhvien/**").hasRole("SINHVIEN")
//                        // Các API công khai (nếu cần, ví dụ: /api/monhoc)
//                        .requestMatchers("/api/monhoc", "/api/monhoc/**").permitAll()
//                        // Tất cả các yêu cầu khác phải được xác thực
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .successHandler(customSuccessHandler)
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutSuccessUrl("/login")
//                        .permitAll()
//                )
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
//                );
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(customUserDetailsService);
//        provider.setPasswordEncoder(passwordEncoder);
//        return new ProviderManager(provider);
//    }
//}