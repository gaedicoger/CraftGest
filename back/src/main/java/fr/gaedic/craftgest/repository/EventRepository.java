package fr.gaedic.craftgest.repository;

import fr.gaedic.craftgest.model.Event;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class EventRepository {
    private final JdbcTemplate jdbcTemplate;

    //Rowmapper:
    private final RowMapper<Event> eventRowMapper =(rs, rowNum) -> {
        Event event = new Event();
        event.setId(rs.getInt("id"));
        event.setName(rs.getString("name"));
        event.setType(rs.getString("type"));
        event.setDateBeginning(rs.getObject("date_beginning", LocalDate.class));
        event.setDateEnd(rs.getObject("date_end", LocalDate.class));
        event.setStatus(rs.getString("status"));
        event.setFees(rs.getBigDecimal("fees"));
        event.setCommissionRate(rs.getObject("commission_rate", Integer.class));
        event.setUserId(rs.getInt("user_id"));
        event.setPlaceId(rs.getInt("place_id"));
        event.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        event.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return event;
    };

    //Constructeur:
    public EventRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    //Read:
    public List<Event> findByUserId(Integer userId) {
        return jdbcTemplate.query(
                """
                
                        SELECT id, name, type, date_beginning, date_end, status, fees, commission_rate, user_id, place_id, created_at, updated_at
                                        FROM events
                                        WHERE user_id = ?
                ORDER
                        BY name
                """,
                eventRowMapper,
                userId
        );
    }

    public Optional<Event> findById(Integer id , Integer userId){
        return jdbcTemplate.query(
                """
                    
                        SELECT id, name, type, date_beginning, date_end, status, fees, commission_rate, user_id, place_id, created_at, updated_at
                    FROM events
                    WHERE id= ?
                    """,
                eventRowMapper,
                id,
                userId
        ).stream().findFirst();
    }

    //Create:
    public Event save(Event event) {
        LocalDateTime now = LocalDateTime.now();
        event.setCreatedAt(now);
        event.setUpdatedAt(now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    """
                    INSERT INTO events (name, type, date_beginning, date_end, status, fees, commission_rate, user_id, place_id, created_at, updated_at)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """,
                    new String[]{"id"}
            );
            ps.setString(1, event.getName());
            ps.setString(2, event.getType());
            ps.setObject(3, event.getDateBeginning());
            ps.setObject(4, event.getDateEnd());
            ps.setString(5, event.getStatus());
            ps.setBigDecimal(6, event.getFees());
            ps.setObject(7, event.getCommissionRate());
            ps.setInt(8, event.getUserId());
            ps.setInt(9, event.getPlaceId());
            ps.setTimestamp(10, Timestamp.valueOf(event.getCreatedAt()));
            ps.setTimestamp(11,Timestamp.valueOf(event.getUpdatedAt()));
            return ps;
        }, keyHolder);

        event.setId(keyHolder.getKey().intValue());
        return event;
    }

    //Update:
    public int update(Integer id, Integer userId, Event event) {
        return jdbcTemplate.update(
                """
                    UPDATE events
                    SET name = ?, type = ?, date_beginning = ?, date_end = ?, status = ?, fees = ?, commission_rate = ?, user_id = ?, place_id = ?, created_at = ?, updated_at = ?
                    WHERE id = ? AND user_id = ?
                    """,
                event.getName(),
                event.getType(),
                event.getDateBeginning(),
                event.getDateEnd(),
                event.getStatus(),
                event.getFees(),
                event.getCommissionRate(),
                event.getUserId(),
                event.getPlaceId(),
                Timestamp.valueOf(LocalDateTime.now()),
                id,
                userId
        );
    }

    //Delete:
    public int deleteById(Integer id, Integer userId) {
        return jdbcTemplate.update(
                """
                    DELETE FROM events
                    WHERE id = ? AND user_id = ?
                    """,
                id,
                userId
        );
    }
}

