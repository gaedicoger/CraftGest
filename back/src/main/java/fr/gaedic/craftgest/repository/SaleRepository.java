package fr.gaedic.craftgest.repository;

import fr.gaedic.craftgest.model.Sale;
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

@Repository
public class SaleRepository{
    private final JdbcTemplate jdbcTemplate;

    //Constructeur:
    public SaleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //RowMapper:
    private final RowMapper<Sale> saleRowMapper = (rs, rowNum) -> {
        Sale sale = new Sale();
        sale.setId(rs.getInt("id"));
        sale.setSaleDate(rs.getObject("sale_date", LocalDate.class));
        sale.setQuantity(rs.getInt("quantity"));
        sale.setPricePaid(rs.getBigDecimal("price_paid"));
        sale.setPriceCatalog(rs.getBigDecimal("price_catalog"));
        sale.setPaidType(rs.getString("paid_type"));
        sale.setEventId(rs.getInt(("event_id")));
        sale.setProductId(rs.getInt("product_id"));
        sale.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        sale.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return sale;
    };
    //Read: Liste de toutes les ventes disponibles pour l'utilisateur

    public List<Sale> findByUserId(Integer userId) {
        return jdbcTemplate.query(
                """
                SELECT s.id, s.sale_date, s.quantity, s.price_paid, s.price_catalog, s.paid_type, s.event_id, s.product_id, s.created_at, s.updated_at
                FROM sales s
                JOIN events e ON s.event_id = e.id
                WHERE e.user_id = ?
                ORDER BY s.sale_date DESC
                """,
                saleRowMapper,
                userId
        );
    }
    // Read : Liste des ventes par produits
    public List<Sale> findByProductId(Integer productId , Integer userId) {
        return jdbcTemplate.query(
                """
                SELECT s.id, s.sale_date, s.quantity, s.price_paid, s.price_catalog, s.paid_type, s.event_id, s.product_id, s.created_at, s.updated_at
                FROM sales s
                JOIN events e ON s.event_id = e.id
                WHERE s.product_id = ? AND e.user_id = ?
                ORDER BY s.sale_date DESC
                """,
                saleRowMapper,
                productId,
                userId
        );
    }

    //Create:
    public Sale save(Sale sale) {
        LocalDateTime now = LocalDateTime.now();
        sale.setCreatedAt(now);
        sale.setUpdatedAt(now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    """
                        INSERT INTO sales (sale_date, quantity, price_paid, price_catalog, paid_type, event_id, product_id, created_at, updated_at)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                        """,
                    new String[]{"id"}
            );
            ps.setObject(1, sale.getSaleDate());
            ps.setInt(2, sale.getQuantity());
            ps.setBigDecimal(3, sale.getPricePaid());
            ps.setBigDecimal(4, sale.getPriceCatalog());
            ps.setString(5, sale.getPaidType());
            ps.setInt(6,sale.getEventId());
            ps.setInt(7,sale.getProductId());
            ps.setTimestamp(8, Timestamp.valueOf(sale.getCreatedAt()));
            ps.setTimestamp(9,Timestamp.valueOf(sale.getUpdatedAt()));
            return ps;
        }, keyHolder);

        sale.setId(keyHolder.getKey().intValue());
        return sale;
    }

    //Update:
    public int update(Integer id, Integer userId, Sale sale) {
        return jdbcTemplate.update(
                """
                UPDATE sales s
                SET sale_date = ?, quantity = ?, price_paid= ?, price_catalog = ?, paid_type = ?, event_id = ?, product_id = ?, updated_at = ?
                FROM events e
                WHERE e.id= event_id AND e.user_id = ? AND s.id = ?
                """,
                sale.getSaleDate(),
                sale.getQuantity(),
                sale.getPricePaid(),
                sale.getPriceCatalog(),
                sale.getPaidType(),
                sale.getEventId(),
                sale.getProductId(),
                Timestamp.valueOf(LocalDateTime.now()),
                userId,
                id
        );
    }

    //Delete:
    public int deleteById(Integer id, Integer userId) {
        return jdbcTemplate.update(
                """
                DELETE FROM sales s
                USING events e
                WHERE e.id = s.event_id AND s.id = ? AND e.user_id = ?
                """,
                id,
                userId
        );
    }
}
