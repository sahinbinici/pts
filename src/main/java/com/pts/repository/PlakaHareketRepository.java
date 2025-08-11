package com.pts.repository;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;

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
        hareket.setHareketTipi(rs.getString("HareketTipi"));
        hareket.setKameraNo(rs.getInt("KameraNo"));
        hareket.setPlaka(rs.getString("SonGecisPlaka"));
        
        java.sql.Timestamp sonGecisTarih = rs.getTimestamp("SonGecisTarih");
        if (sonGecisTarih != null) {
            hareket.setSonGecisTarih(sonGecisTarih.toLocalDateTime());
        }
        return hareket;
    };


    public List<PlakaHareket> findMovementsInLastThirtyMinutes() {
        String sql = "SELECT * FROM Kamera.v_SonPlakaGecisleri_SB WHERE SonGecisTarih >= DATEADD(HOUR, -1, GETDATE())ORDER BY SonGecisTarih DESC";
        return jdbcTemplate.query(sql, plakaHareketRowMapper);
    }

    public PlakaHareket findLastMovement() {
        String sql = "SELECT * FROM Kamera.sb_SonKameraGecislerV2 ORDER BY SonGecisTarih DESC";
        List<PlakaHareket> results = jdbcTemplate.query(sql, plakaHareketRowMapper);
        return results.isEmpty() ? null : results.get(0);
    }

    public List<PlakaHareket> searchPlakaHareketByDateRange(LocalDateTime startDate, LocalDateTime endDate, String plaka) {
        String sql = "SELECT * FROM Kamera.v_SonPlakaGecisleri_SB WHERE SonGecisTarih BETWEEN ? AND ?";
        if (plaka != null && !plaka.trim().isEmpty()) {
            sql += " AND SonGecisPlaka = ?";
            return jdbcTemplate.query(sql, plakaHareketRowMapper, startDate, endDate, plaka);
        }
        return jdbcTemplate.query(sql, plakaHareketRowMapper, startDate, endDate);
    }

} 