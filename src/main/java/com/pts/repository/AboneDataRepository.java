package com.pts.repository;

import com.pts.entity.AboneData;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class AboneDataRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcCall aboneCreateCall;
    private final SimpleJdbcCall aboneUpdateCall;
    private final SimpleJdbcCall aboneDeleteCall;

    private final RowMapper<AboneData> aboneRowMapper = (rs, rowNum) -> {
        AboneData abone = new AboneData();
        abone.setPlaka(rs.getString("Plaka"));
        abone.setAd(rs.getString("Ad"));
        abone.setSoyad(rs.getString("Soyad"));
        abone.setAdres(rs.getString("Adres"));
        abone.setTcKimlikNo(rs.getString("TcKimlikNo"));
        abone.setTelefon(rs.getString("Telefon"));
        abone.setEmail(rs.getString("Email"));
        return abone;
    };

    public AboneDataRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        
        this.aboneCreateCall = new SimpleJdbcCall(dataSource)
                .withProcedureName("SP_ABONECREATE_SB")
                .withSchemaName("dbo")
                .declareParameters(
                        new SqlParameter("PLAKA", Types.NVARCHAR),
                        new SqlParameter("AD", Types.NVARCHAR),
                        new SqlParameter("SOYAD", Types.NVARCHAR),
                        new SqlParameter("ADRES", Types.NVARCHAR),
                        new SqlParameter("TCKIMLIKNO", Types.NVARCHAR),
                        new SqlParameter("EMAIL", Types.NVARCHAR),
                        new SqlParameter("TELEFON", Types.NVARCHAR)
                );

        this.aboneUpdateCall = new SimpleJdbcCall(dataSource)
                .withProcedureName("SP_ABONEUPDATE_SB")
                .withSchemaName("dbo")
                .declareParameters(
                        new SqlParameter("PLAKA", Types.NVARCHAR),
                        new SqlParameter("AD", Types.NVARCHAR),
                        new SqlParameter("SOYAD", Types.NVARCHAR),
                        new SqlParameter("ADRES", Types.NVARCHAR),
                        new SqlParameter("TCKIMLIKNO", Types.NVARCHAR),
                        new SqlParameter("EMAIL", Types.NVARCHAR),
                        new SqlParameter("TELEFON", Types.NVARCHAR)
                );

        this.aboneDeleteCall = new SimpleJdbcCall(dataSource)
                .withProcedureName("SP_ABONEDELETE_SB")
                .withSchemaName("dbo")
                .declareParameters(
                        new SqlParameter("PLAKA", Types.NVARCHAR)
                );
    }

    public Integer createAbone(AboneData aboneData) {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("PLAKA", aboneData.getPlaka())
                    .addValue("AD", aboneData.getAd())
                    .addValue("SOYAD", aboneData.getSoyad())
                    .addValue("ADRES", aboneData.getAdres())
                    .addValue("TCKIMLIKNO", aboneData.getTcKimlikNo())
                    .addValue("EMAIL", aboneData.getEmail())
                    .addValue("TELEFON", aboneData.getTelefon());

            Map<String, Object> result = aboneCreateCall.execute(params);
            
            // Stored procedure'ün dönüş değerini kontrol et
            if (result.containsKey("#update-count-1")) {
                return (Integer) result.get("#update-count-1");
            } else if (result.containsKey("@RETURN_VALUE")) {
                return (Integer) result.get("@RETURN_VALUE");
            } else {
                // Eğer hiçbir dönüş değeri bulunamazsa
                return result.values().stream()
                        .filter(v -> v instanceof Integer)
                        .map(v -> (Integer) v)
                        .findFirst()
                        .orElse(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Integer updateAbone(AboneData aboneData) {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("PLAKA", aboneData.getPlaka())
                    .addValue("AD", aboneData.getAd())
                    .addValue("SOYAD", aboneData.getSoyad())
                    .addValue("ADRES", aboneData.getAdres())
                    .addValue("TCKIMLIKNO", aboneData.getTcKimlikNo())
                    .addValue("EMAIL", aboneData.getEmail())
                    .addValue("TELEFON", aboneData.getTelefon());

            Map<String, Object> result = aboneUpdateCall.execute(params);
            
            if (result.containsKey("#update-count-1")) {
                return (Integer) result.get("#update-count-1");
            } else if (result.containsKey("@RETURN_VALUE")) {
                return (Integer) result.get("@RETURN_VALUE");
            } else {
                return result.values().stream()
                        .filter(v -> v instanceof Integer)
                        .map(v -> (Integer) v)
                        .findFirst()
                        .orElse(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Integer deleteAbone(String plaka) {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("PLAKA", plaka);

            Map<String, Object> result = aboneDeleteCall.execute(params);
            
            if (result.containsKey("#update-count-1")) {
                return (Integer) result.get("#update-count-1");
            } else if (result.containsKey("@RETURN_VALUE")) {
                return (Integer) result.get("@RETURN_VALUE");
            } else {
                return result.values().stream()
                        .filter(v -> v instanceof Integer)
                        .map(v -> (Integer) v)
                        .findFirst()
                        .orElse(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<AboneData> findAllByPlaka(String plaka) {
        String sql = "SELECT Plaka, Ad, Soyad, Adres, TcKimlikNo, Telefon, Email FROM Plate.AboneData WHERE Plaka = ?";
        try {
            return jdbcTemplate.query(sql, aboneRowMapper, plaka);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<AboneData> findAllByTcKimlikNo(String tcKimlikNo) {
        String sql = "SELECT Plaka, Ad, Soyad, Adres, TcKimlikNo, Telefon, Email FROM Plate.AboneData WHERE TcKimlikNo = ?";
        try {
            return jdbcTemplate.query(sql, aboneRowMapper, tcKimlikNo);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<AboneData> findAllByAdSoyad(String adSoyad) {
        String sql = "SELECT Plaka, Ad, Soyad, Adres, TcKimlikNo, Telefon, Email FROM Plate.AboneData WHERE AdSoyad LIKE CONCAT('%', ?, '%')";
        try {
            return jdbcTemplate.query(sql, aboneRowMapper, adSoyad);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}