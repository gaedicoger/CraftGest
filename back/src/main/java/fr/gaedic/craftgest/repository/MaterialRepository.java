package fr.gaedic.craftgest.repository;

import fr.gaedic.craftgest.model.Color;
import fr.gaedic.craftgest.model.Material;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class MaterialRepository {
    private final JdbcTemplate jdbcTemplate;

    //Rowmapper:
    private final RowMapper<Material> materialRowMapper = (rs, rowNum) -> {
        Material material = new Material();
        material.setId(rs.getInt("id"));
        material.setName(rs.getString("name"));
        material.setUserId(rs.getInt("user_id"));
        material.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        material.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return material;
    };

    //Constructeur:
    public MaterialRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate= jdbcTemplate; }

    //Read:
    public List<Material> findByUserId(Integer userId) {
        return jdbcTemplate.query(
                """
                SELECT id, name, user_id, created_at, updated_at
                FROM materials
                WHERE user_id = ?
                ORDER BY name
                """,
                materialRowMapper,
                userId
        );
    }

    //Create:
    public Material save(Material material) {
        LocalDateTime now = LocalDateTime.now();
        material.setCreatedAt(now);
        material.setUpdatedAt(now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    """
                        INSERT INTO materials (name, user_id, created_at, updated_at)
                        VALUES (?, ?, ?, ?)
                        """,
                    new String[]{"id"}
            );
            ps.setString(1, material.getName());
            ps.setInt(2, material.getUserId());
            ps.setTimestamp(3, Timestamp.valueOf(material.getCreatedAt()));
            ps.setTimestamp(4,Timestamp.valueOf(material.getUpdatedAt()));
            return ps;
        }, keyHolder);

        material.setId(keyHolder.getKey().intValue());
        return material;
    }

    public Optional<Material> findById(Integer id , Integer userId) {
        return jdbcTemplate.query(
                """
                SELECT id, name, user_id, created_at, updated_at
                       FROM materials
                       WHERE id = ? AND user_id = ?
                """,
                materialRowMapper,
                id,
                userId
        ).stream().findFirst();
    }

    //Update:
    public int update(Integer id, Integer userId, Material material) {
        return jdbcTemplate.update(
                """
                UPDATE materials
                SET name = ?, updated_at =?
                WHERE id = ? AND user_id = ?
                """,
                material.getName(),
                Timestamp.valueOf(LocalDateTime.now()),
                id,
                userId
        );
    }

    //Delete:

    public int deleteById(Integer id, Integer userId) {
        return jdbcTemplate.update(
                """
                DELETE FROM materials
                WHERE id = ? AND user_id = ?
                """,
                id,
                userId
        );
    }
}
