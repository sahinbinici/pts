package com.pts.repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.pts.entity.PlakaHareket;

@Repository
public class PlakaHareketRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PlakaHareketRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<PlakaHareket> plakaHareketRowMapper = (ResultSet rs, int rowNum) -> {
        PlakaHareket hareket = new PlakaHareket();
        hareket.setAboneId(rs.getInt("PlateHareketId"));
        hareket.setAboneId(rs.getInt("AboneId"));
        hareket.setPlaka(rs.getString("okunanPlaka"));
        
        java.sql.Timestamp girisTarihi = rs.getTimestamp("GirisTarih");
        if (girisTarihi != null) {
            hareket.setGirisTarihi(girisTarihi.toLocalDateTime());
        }
        
        java.sql.Timestamp cikisTarihi = rs.getTimestamp("CikisTarih");
        if (cikisTarihi != null) {
            hareket.setCikisTarihi(cikisTarihi.toLocalDateTime());
        }
        
        hareket.setGirisKameraId(rs.getInt("GirisKameraId"));
        Object cikisKameraId = rs.getObject("CikisKameraId");
        if (cikisKameraId != null) {
            hareket.setCikisKameraId(rs.getInt("CikisKameraId"));
        }
        return hareket;
    };

    public List<PlakaHareket> findAllByOrderByGirisTarihiDesc() {
        String sql = "SELECT * FROM Plate.PlateHareket ORDER BY GirisTarih DESC";
        return jdbcTemplate.query(sql, plakaHareketRowMapper);
    }

    public List<PlakaHareket> findLastTenPlates() {
        String sql = "SELECT TOP 10 * FROM Plate.PlateHareket ORDER BY PlateHareketId DESC";
        return jdbcTemplate.query(sql, plakaHareketRowMapper);
    }

    public List<PlakaHareket> findMovementsInLastThirtyMinutes() {
        String sql = "SELECT * FROM Plate.PlateHareket WHERE " +
                    "GirisTarih >= DATEADD(MINUTE, -30, GETDATE()) OR " +
                    "CikisTarih >= DATEADD(MINUTE, -30, GETDATE()) " +
                    "ORDER BY PlateHareketId DESC";
        return jdbcTemplate.query(sql, plakaHareketRowMapper);
    }

    public Optional<PlakaHareket> findByPlakaAndCikisTarihiIsNull(String plaka) {
        String sql = "SELECT * FROM Plate.PlateHareket WHERE okunanPlaka = ? AND CikisTarihi IS NULL";
        List<PlakaHareket> results = jdbcTemplate.query(sql, plakaHareketRowMapper, plaka);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

} 