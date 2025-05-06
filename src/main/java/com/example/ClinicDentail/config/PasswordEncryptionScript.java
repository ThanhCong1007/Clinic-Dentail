//package com.example.ClinicDentail.config;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//
//public class PasswordEncryptionScript {
//    public static void main(String[] args) {
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        try {
//            // Kết nối với cơ sở dữ liệu
//            String url = "jdbc:mysql://mysql-doanweb2025-phamminhanh11623-9727.k.aivencloud.com:18559/defaultdb?useSSL=true&requireSSL=true";
//            String username = "root";
//            String password = "";
//
//
//            Connection connection = DriverManager.getConnection(url, username, password);
//
//            // Lấy tất cả user và mật khẩu plain text
//            String selectQuery = "SELECT MaTK, Password FROM taikhoan";
//            PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
//
//            ResultSet rs = selectStmt.executeQuery();
//
//            while (rs.next()) {
//                int userId = rs.getInt("MaTK");
//                String plainTextPassword = rs.getString("Password");
//
//                // Mã hóa mật khẩu
//                String encodedPassword = passwordEncoder.encode(plainTextPassword);
//
//                // Cập nhật mật khẩu mã hóa vào cơ sở dữ liệu
//                String updateQuery = "UPDATE taikhoan SET Password = ? WHERE MaTK = ?";
//                PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
//                updateStmt.setString(1, encodedPassword);
//                updateStmt.setInt(2, userId);
//
//                updateStmt.executeUpdate();
//                System.out.println("Updated password for user ID: " + userId);
//            }
//
//            rs.close();
//            selectStmt.close();
//            connection.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
