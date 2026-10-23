package fr.gaedic.craftgest.repository;

import fr.gaedic.craftgest.model.Universe;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class UniverseRepository {
    private final JdbcTemplate jdbcTemplate;

    //Rowmapper:
    private final RowMapper<Universe> universeRowMapper = (rs, rowNum) -> {
        Universe universe = new Universe();
        universe.setId(rs.getInt("id"));
        universe.setName(rs.getString("name"));
        universe.setUserId(rs.getInt("user_id"));
        universe.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        universe.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return universe;
    };

    //Constructeur:
    public UniverseRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate= jdbcTemplate; }

    //Read:
    public List<Universe> findByUserId(Integer userId) {
        return jdbcTemplate.query(
                """
                SELECT id, name, user_id, created_at, updated_at
                FROM universes
                WHERE user_id = ?
                ORDERBY name
                """,
                universeRowMapper,
                userId
        );
    }

    //Create:
    public Universe save(Universe universe) {
        LocalDateTime now = LocalDateTime.now();
        universe.setCreatedAt(now);
        universe.setUpdatedAt(now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    """
                        INSERT INTO universes (name, user_id, created_at, updated_at)
                        VALUES (?, ?, ?, ?)
                        """,
                        new String[]{"id"}
            );
            ps.setString(1, universe.getName());
            ps.setInt(2, universe.getUserId());
            ps.setTimestamp(3, Timestamp.valueOf(universe.getCreatedAt()));
            ps.setTimestamp(4,Timestamp.valueOf(universe.getUpdatedAt()));
            return ps;
        }, keyHolder);

        universe.setId(keyHolder.getKey().intValue());
        return universe;
    }

    //Update:
    public int update(Integer id, Universe universe) {
        return jdbcTemplate.update(
                """
                UPDATE universes
                SET name = ?, updated_at =?
                WHERE id = ?
                """,
                universe.getName(),
                Timestamp.valueOf(LocalDateTime.now()),
                id
        );
    }

    //Delete:
    public int deleteById(Integer id) {
        return jdbcTemplate.update(
                """
                DELETE FROM universes
                WHERE id = ?
                """,
                id
        );
    }
}
