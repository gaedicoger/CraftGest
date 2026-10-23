package fr.gaedic.craftgest.repository;

import fr.gaedic.craftgest.model.Color;
import fr.gaedic.craftgest.model.Finding;
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
public class FindingRepository {
    private final JdbcTemplate jdbcTemplate;

    //Rowmapper:
    private final RowMapper<Finding> findingRowMapper = (rs, rowNum) -> {
        Finding finding = new Finding();
        finding.setId(rs.getInt("id"));
        finding.setName(rs.getString("name"));
        finding.setUserId(rs.getInt("user_id"));
        finding.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        finding.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return finding;
    };

    //Constructeur:
    public FindingRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate= jdbcTemplate; }

    //Read:
    public List<Finding> findByUserId(Integer userId) {
        return jdbcTemplate.query(
                """
                SELECT id, name, user_id, created_at, updated_at
                FROM findings
                WHERE user_id = ?
                ORDER BY name
                """,
                findingRowMapper,
                userId
        );
    }

    public Optional<Finding> findById(Integer id , Integer userId) {
        return jdbcTemplate.query(
                """
                SELECT id, name, user_id, created_at, updated_at
                       FROM findings
                       WHERE id = ? AND user_id = ?
                """,
                findingRowMapper,
                id,
                userId
        ).stream().findFirst();
    }

    //Create:
    public Finding save(Finding finding) {
        LocalDateTime now = LocalDateTime.now();
        finding.setCreatedAt(now);
        finding.setUpdatedAt(now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    """
                        INSERT INTO findings (name, user_id, created_at, updated_at)
                        VALUES (?, ?, ?, ?)
                        """,
                    new String[]{"id"}
            );
            ps.setString(1, finding.getName());
            ps.setInt(2, finding.getUserId());
            ps.setTimestamp(3, Timestamp.valueOf(finding.getCreatedAt()));
            ps.setTimestamp(4,Timestamp.valueOf(finding.getUpdatedAt()));
            return ps;
        }, keyHolder);

        finding.setId(keyHolder.getKey().intValue());
        return finding;
    }

    //Update:
    public int update(Integer id, Integer userId, Finding finding) {
        return jdbcTemplate.update(
                """
                UPDATE findings
                SET name = ?, updated_at =?
                WHERE id = ? AND user_id = ?
                """,
                finding.getName(),
                Timestamp.valueOf(LocalDateTime.now()),
                id,
                userId
        );
    }

    //Delete:
    public int deleteById(Integer id, Integer userId) {
        return jdbcTemplate.update(
                """
                DELETE FROM findings
                WHERE id = ? AND user_id = ?
                """,
                id,
                userId
        );
    }
}
