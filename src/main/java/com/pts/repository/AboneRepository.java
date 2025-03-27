package com.pts.repository;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.pts.entity.AboneData;

@Repository
public class AboneRepository {
    private final JdbcTemplate jdbcTemplate;

    public AboneRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer createAbone(AboneData aboneData) {
        String sql = "INSERT INTO Abone (Plaka, Ad, Soyad, TcKimlikNo, Telefon, Email, Adres) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, 
            aboneData.getPlaka(),
            aboneData.getAd(),
            aboneData.getSoyad(),
            aboneData.getTcKimlikNo(),
            aboneData.getTelefon(),
            aboneData.getEmail(),
            aboneData.getAdres()
        );
    }

    public Integer updateAbone(AboneData aboneData) {
        String sql = "UPDATE Abone SET Ad = ?, Soyad = ?, TcKimlikNo = ?, Telefon = ?, Email = ?, Adres = ? WHERE Plaka = ?";
        return jdbcTemplate.update(sql,
            aboneData.getAd(),
            aboneData.getSoyad(),
            aboneData.getTcKimlikNo(),
            aboneData.getTelefon(),
            aboneData.getEmail(),
            aboneData.getAdres(),
            aboneData.getPlaka()
        );
    }

    public Integer deleteAbone(String plaka) {
        String sql = "DELETE FROM Abone WHERE Plaka = ?";
        return jdbcTemplate.update(sql, plaka);
    }

    public List<AboneData> findAllByPlaka(String plaka) {
        String sql = "SELECT * FROM Abone WHERE Plaka LIKE ?";
        return jdbcTemplate.query(sql, 
            new Object[]{"%" + plaka + "%"},
            (rs, rowNum) -> {
                AboneData abone = new AboneData();
                abone.setPlaka(rs.getString("Plaka"));
                abone.setAd(rs.getString("Ad"));
                abone.setSoyad(rs.getString("Soyad"));
                abone.setTcKimlikNo(rs.getString("TcKimlikNo"));
                abone.setTelefon(rs.getString("Telefon"));
                abone.setEmail(rs.getString("Email"));
                abone.setAdres(rs.getString("Adres"));
                return abone;
            }
        );
    }

    public List<AboneData> findAllByTcKimlikNo(String tcKimlikNo) {
        String sql = "SELECT * FROM Abone WHERE TcKimlikNo LIKE ?";
        return jdbcTemplate.query(sql, 
            new Object[]{"%" + tcKimlikNo + "%"},
            (rs, rowNum) -> {
                AboneData abone = new AboneData();
                abone.setPlaka(rs.getString("Plaka"));
                abone.setAd(rs.getString("Ad"));
                abone.setSoyad(rs.getString("Soyad"));
                abone.setTcKimlikNo(rs.getString("TcKimlikNo"));
                abone.setTelefon(rs.getString("Telefon"));
                abone.setEmail(rs.getString("Email"));
                abone.setAdres(rs.getString("Adres"));
                return abone;
            }
        );
    }

    public List<AboneData> findAllByAdSoyad(String adSoyad) {
        String sql = "SELECT * FROM Abone WHERE Ad + ' ' + Soyad LIKE ?";
        return jdbcTemplate.query(sql, 
            new Object[]{"%" + adSoyad + "%"},
            (rs, rowNum) -> {
                AboneData abone = new AboneData();
                abone.setPlaka(rs.getString("Plaka"));
                abone.setAd(rs.getString("Ad"));
                abone.setSoyad(rs.getString("Soyad"));
                abone.setTcKimlikNo(rs.getString("TcKimlikNo"));
                abone.setTelefon(rs.getString("Telefon"));
                abone.setEmail(rs.getString("Email"));
                abone.setAdres(rs.getString("Adres"));
                return abone;
            }
        );
    }
} 