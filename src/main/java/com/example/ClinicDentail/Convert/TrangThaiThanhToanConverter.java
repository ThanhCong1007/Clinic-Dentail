package com.example.ClinicDentail.Convert;


import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.example.ClinicDentail.Enity.ThanhToan.TrangThaiThanhToan;
@Converter(autoApply = false)
public class TrangThaiThanhToanConverter implements AttributeConverter<TrangThaiThanhToan, String> {

    @Override
    public String convertToDatabaseColumn(TrangThaiThanhToan attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public TrangThaiThanhToan convertToEntityAttribute(String dbData) {
        for (TrangThaiThanhToan t : TrangThaiThanhToan.values()) {
            if (t.getValue().equalsIgnoreCase(dbData)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Trạng thái không hợp lệ: " + dbData);
    }
}
