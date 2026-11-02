package fr.gaedic.craftgest.repository;

import fr.gaedic.craftgest.model.Deposit;
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

// deposits n'a pas de user_id : le propriétaire se vérifie par jointure avec events (events.user_id)
@Repository
public class DepositRepository {
    private final JdbcTemplate jdbcTemplate;

    //Constructeur:
    public DepositRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //RowMapper:
    private final RowMapper<Deposit> depositRowMapper = (rs, rowNum) -> {
        Deposit deposit = new Deposit();
        deposit.setId(rs.getInt("id"));
        deposit.setEventId(rs.getInt("event_id"));
        deposit.setProductId(rs.getInt("product_id"));
        deposit.setQuantityDeposited(rs.getInt("quantity_deposited"));
        deposit.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        deposit.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return deposit;
    };

    //Read: tout ce qui est déposé dans un dépôt (un événement)
    public List<Deposit> findByEventId(Integer eventId, Integer userId) {
        return jdbcTemplate.query(
                """
                SELECT d.id, d.event_id, d.product_id, d.quantity_deposited, d.created_at, d.updated_at
                FROM deposits d
                JOIN events e ON e.id = d.event_id
                WHERE d.event_id = ? AND e.user_id = ?
                ORDER BY d.created_at DESC
                """,
                depositRowMapper,
                eventId,
                userId
        );
    }

    //Read: où est déposé un produit
    public List<Deposit> findByProductId(Integer productId, Integer userId) {
        return jdbcTemplate.query(
                """
                SELECT d.id, d.event_id, d.product_id, d.quantity_deposited, d.created_at, d.updated_at
                FROM deposits d
                JOIN events e ON e.id = d.event_id
                WHERE d.product_id = ? AND e.user_id = ?
                ORDER BY d.created_at DESC
                """,
                depositRowMapper,
                productId,
                userId
        );
    }

    //Read: un seul dépôt
    public Optional<Deposit> findById(Integer id, Integer userId) {
        return jdbcTemplate.query(
                """
                SELECT d.id, d.event_id, d.product_id, d.quantity_deposited, d.created_at, d.updated_at
                FROM deposits d
                JOIN events e ON e.id = d.event_id
                WHERE d.id = ? AND e.user_id = ?
                """,
                depositRowMapper,
                id,
                userId
        ).stream().findFirst();
    }

    //Create: le service doit vérifier avant que l'événement ET le produit appartiennent à l'utilisateur
    // (un seul dépôt par couple événement + produit : contrainte d'unicité en base)
    public Deposit save(Deposit deposit) {
        LocalDateTime now = LocalDateTime.now();
        deposit.setCreatedAt(now);
        deposit.setUpdatedAt(now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    """
                    INSERT INTO deposits (event_id, product_id, quantity_deposited, created_at, updated_at)
                    VALUES (?, ?, ?, ?, ?)
                    """,
                    new String[]{"id"}
            );
            ps.setInt(1, deposit.getEventId());
            ps.setInt(2, deposit.getProductId());
            ps.setInt(3, deposit.getQuantityDeposited());
            ps.setTimestamp(4, Timestamp.valueOf(deposit.getCreatedAt()));
            ps.setTimestamp(5, Timestamp.valueOf(deposit.getUpdatedAt()));
            return ps;
        }, keyHolder);

        deposit.setId(keyHolder.getKey().intValue());
        return deposit;
    }

    //Update: seule la quantité déposée change (l'événement et le produit restent les mêmes)
    public int update(Integer id, Integer userId, Deposit deposit) {
        return jdbcTemplate.update(
                """
                UPDATE deposits d
                SET quantity_deposited = ?, updated_at = ?
                FROM events e
                WHERE e.id = d.event_id
                  AND d.id = ? AND e.user_id = ?
                """,
                deposit.getQuantityDeposited(),
                Timestamp.valueOf(LocalDateTime.now()),
                id,
                userId
        );
    }

    //Delete:
    public int deleteById(Integer id, Integer userId) {
        return jdbcTemplate.update(
                """
                DELETE FROM deposits d
                USING events e
                WHERE e.id = d.event_id
                  AND d.id = ? AND e.user_id = ?
                """,
                id,
                userId
        );
    }
}
