package com.example.ClinicDentail.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PasswordEncryptionScript {
    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        try {
            // Kết nối với cơ sở dữ liệu
            String url = "jdbc:mysql://localhost:3306/nhakhoachuan"; // Cập nhật đúng tên DB của bạn
            String username = "root";
            String password = ""; // Nếu có mật khẩu thì điền vào đây

            Connection connection = DriverManager.getConnection(url, username, password);

            // Lấy tất cả người dùng và mật khẩu plain text
            String selectQuery = "SELECT ma_nguoi_dung, mat_khau FROM nguoi_dung";
            PreparedStatement selectStmt = connection.prepareStatement(selectQuery);

            ResultSet rs = selectStmt.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("ma_nguoi_dung");
                String plainTextPassword = rs.getString("mat_khau");

                // Nếu mật khẩu đã được mã hóa rồi (ví dụ đã có dạng $2a$...) thì bỏ qua
                if (plainTextPassword != null && plainTextPassword.startsWith("$2a$")) {
                    continue;
                }

                // Mã hóa mật khẩu
                String encodedPassword = passwordEncoder.encode(plainTextPassword);

                // Cập nhật mật khẩu đã mã hóa
                String updateQuery = "UPDATE nguoi_dung SET mat_khau = ? WHERE ma_nguoi_dung = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                updateStmt.setString(1, encodedPassword);
                updateStmt.setInt(2, userId);

                updateStmt.executeUpdate();
                updateStmt.close();

                System.out.println("Updated password for user ID: " + userId);
            }

            rs.close();
            selectStmt.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
