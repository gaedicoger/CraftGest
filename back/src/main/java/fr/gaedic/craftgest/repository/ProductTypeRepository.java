package fr.gaedic.craftgest.repository;

import fr.gaedic.craftgest.model.Color;
import fr.gaedic.craftgest.model.ProductType;
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
public class ProductTypeRepository {
    private final JdbcTemplate jdbcTemplate;

    //Rowmapper:
    private final RowMapper<ProductType> productTypeRowMapper = (rs, rowNum) -> {
        ProductType productType = new ProductType();
        productType.setId(rs.getInt("id"));
        productType.setName(rs.getString("name"));
        productType.setUserId(rs.getInt("user_id"));
        productType.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        productType.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return productType;
    };

    //Constructeur:
    public ProductTypeRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate= jdbcTemplate; }

    //Read:
    public List<ProductType> findByUserId(Integer userId) {
        return jdbcTemplate.query(
                """
                SELECT id, name, user_id, created_at, updated_at
                FROM types
                WHERE user_id = ?
                ORDER BY name
                """,
                productTypeRowMapper,
                userId
        );
    }

    //Create:
    public ProductType save(ProductType productType) {
        LocalDateTime now = LocalDateTime.now();
        productType.setCreatedAt(now);
        productType.setUpdatedAt(now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    """
                        INSERT INTO types (name, user_id, created_at, updated_at)
                        VALUES (?, ?, ?, ?)
                        """,
                    new String[]{"id"}
            );
            ps.setString(1, productType.getName());
            ps.setInt(2, productType.getUserId());
            ps.setTimestamp(3, Timestamp.valueOf(productType.getCreatedAt()));
            ps.setTimestamp(4,Timestamp.valueOf(productType.getUpdatedAt()));
            return ps;
        }, keyHolder);

        productType.setId(keyHolder.getKey().intValue());
        return productType;
    }

    public Optional<ProductType> findById(Integer id , Integer userId) {
        return jdbcTemplate.query(
                """
                SELECT id, name, user_id, created_at, updated_at
                       FROM types
                       WHERE id = ? AND user_id = ?
                """,
                productTypeRowMapper,
                id,
                userId
        ).stream().findFirst();
    }

    //Update:
    public int update(Integer id, Integer userId, ProductType productType) {
        return jdbcTemplate.update(
                """
                UPDATE types
                SET name = ?, updated_at =?
                WHERE id = ? AND user_id = ?
                """,
                productType.getName(),
                Timestamp.valueOf(LocalDateTime.now()),
                id,
                userId
        );
    }

    //Delete:
    public int deleteById(Integer id, Integer userId) {
        return jdbcTemplate.update(
                """
                DELETE FROM types
                WHERE id = ? AND user_id = ?
                """,
                id,
                userId
        );
    }
}
