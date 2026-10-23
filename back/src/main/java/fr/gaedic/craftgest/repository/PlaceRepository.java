package fr.gaedic.craftgest.repository;

import fr.gaedic.craftgest.model.Place;
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
public class PlaceRepository {
    private final JdbcTemplate jdbcTemplate; //Outil SQL

    //Rowmapper :
    private final RowMapper<Place> placeRowMapper = (rs, rowNum) -> {
        Place place = new Place();
        place.setId(rs.getInt("id"));
        place.setName(rs.getString("name"));
        place.setType(rs.getString("type"));
        place.setDiscountRate(rs.getObject("discount_rate", Integer.class)); //Ici on ne met pas de getInt car la colonne peut être null et du coup ç peut planter
        place.setCommissionRate(rs.getObject("commission_rate", Integer.class));
        place.setUserId(rs.getInt("user_id"));
        place.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        place.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return place;
    };

    //Constructeur:
    public PlaceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Place> findByUserId(Integer userId) {
        return jdbcTemplate.query(
                """
                SELECT id, name, type, discount_rate, commission_rate, user_id, created_at, updated_at
                FROM places
                WHERE user_id = ?
                ORDER BY name
                """,
                placeRowMapper,
                userId
        );
    }

    public Optional<Place> findById(Integer id , Integer userId) {
        return jdbcTemplate.query(
                """
                SELECT id, name, type, discount_rate, commission_rate, user_id, created_at, updated_at
                       FROM places
                       WHERE id = ? AND user_id = ?
                """,
                placeRowMapper,
                id,
                userId
        ).stream().findFirst();
    }

    public Place save(Place place) {
        LocalDateTime now = LocalDateTime.now();
        place.setCreatedAt(now);
        place.setUpdatedAt(now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    """
                    INSERT INTO places (name, type, discount_rate, commission_rate, user_id, created_at, updated_at)
                    VALUES (?, ?, ?, ?, ?, ?, ?)
                    """,
                    new String[]{"id"}                    // "renvoie-moi la colonne id"
            );
            ps.setString(1, place.getName());
            ps.setString(2, place.getType());
            ps.setObject(3, place.getDiscountRate());
            ps.setObject(4, place.getCommissionRate());
            ps.setInt(5, place.getUserId());
            ps.setTimestamp(6, Timestamp.valueOf(place.getCreatedAt()));   // LocalDateTime → Timestamp
            ps.setTimestamp(7, Timestamp.valueOf(place.getUpdatedAt()));
            return ps;
        }, keyHolder);

        place.setId(keyHolder.getKey().intValue());
        return place;
    }

    public int update(Integer id, Integer userId, Place place) {
        return jdbcTemplate.update(
                """
                UPDATE places
                SET name = ?, type = ?, discount_rate = ?, commission_rate = ?, updated_at = ?
                WHERE id = ? AND user_id = ?
                """,
                place.getName(),
                Timestamp.valueOf(LocalDateTime.now()),
                id,
                userId
        );
    }
    public int deleteById(Integer id, Integer userId) {
        return jdbcTemplate.update(
                """
                DELETE FROM places
                WHERE id = ? AND user_id = ?
                """,
                id,
                userId
        );
    }
}
