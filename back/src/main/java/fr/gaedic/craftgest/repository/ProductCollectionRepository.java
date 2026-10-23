package fr.gaedic.craftgest.repository;

import fr.gaedic.craftgest.model.Color;
import fr.gaedic.craftgest.model.ProductCollection;
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
public class ProductCollectionRepository {
    private final JdbcTemplate jdbcTemplate;

    //Rowmapper:
    private final RowMapper<ProductCollection> productCollectionRowMapper = (rs, rowNum) -> {
        ProductCollection productCollection = new ProductCollection();
        productCollection.setId(rs.getInt("id"));
        productCollection.setName(rs.getString("name"));
        productCollection.setUserId(rs.getInt("user_id"));
        productCollection.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        productCollection.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return productCollection;
    };

    //Constructeur:
    public ProductCollectionRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate= jdbcTemplate; }

    //Read:
    public List<ProductCollection> findByUserId(Integer userId) {
        return jdbcTemplate.query(
                """
                SELECT id, name, user_id, created_at, updated_at
                FROM collections
                WHERE user_id = ?
                ORDER BY name
                """,
                productCollectionRowMapper,
                userId
        );
    }

    //Create:
    public ProductCollection save(ProductCollection productCollection) {
        LocalDateTime now = LocalDateTime.now();
        productCollection.setCreatedAt(now);
        productCollection.setUpdatedAt(now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    """
                        INSERT INTO collections (name, user_id, created_at, updated_at)
                        VALUES (?, ?, ?, ?)
                        """,
                    new String[]{"id"}
            );
            ps.setString(1, productCollection.getName());
            ps.setInt(2, productCollection.getUserId());
            ps.setTimestamp(3, Timestamp.valueOf(productCollection.getCreatedAt()));
            ps.setTimestamp(4,Timestamp.valueOf(productCollection.getUpdatedAt()));
            return ps;
        }, keyHolder);

        productCollection.setId(keyHolder.getKey().intValue());
        return productCollection;
    }

    public Optional<ProductCollection> findById(Integer id , Integer userId) {
        return jdbcTemplate.query(
                """
                SELECT id, name, user_id, created_at, updated_at
                       FROM collections
                       WHERE id = ? AND user_id = ?
                """,
                productCollectionRowMapper,
                id,
                userId
        ).stream().findFirst();
    }

    //Update:
    public int update(Integer id, Integer userId, ProductCollection productCollection) {
        return jdbcTemplate.update(
                """
                UPDATE collections
                SET name = ?, updated_at =?
                WHERE id = ? AND user_id = ?
                """,
                productCollection.getName(),
                Timestamp.valueOf(LocalDateTime.now()),
                id,
                userId
        );
    }

    //Delete:
    public int deleteById(Integer id, Integer userId) {
        return jdbcTemplate.update(
                """
                DELETE FROM collections
                WHERE id = ? AND user_id = ?
                """,
                id,
                userId
        );
    }
}
