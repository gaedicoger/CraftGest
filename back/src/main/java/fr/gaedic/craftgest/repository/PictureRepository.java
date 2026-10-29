package fr.gaedic.craftgest.repository;

import fr.gaedic.craftgest.model.Color;
import fr.gaedic.craftgest.model.Material;
import fr.gaedic.craftgest.model.Picture;
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
public class PictureRepository {
    private final JdbcTemplate jdbcTemplate;

//Constructeur:
    public PictureRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
}

//RowMapper:
    private final RowMapper<Picture> pictureRowMapper = (rs, rowNum) -> {
        Picture picture = new Picture();
        picture.setId(rs.getInt("id"));                                           // 2. on lit "id" dans la ligne, puis on le range dans l'objet
        picture.setUrl(rs.getString("url"));                                    //    texte → getString
        picture.setPosition(rs.getInt("position"));                                  //    nom SQL en snake_case entre guillemets
        picture.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());    //    date : Timestamp → LocalDateTime
        picture.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return picture;
};
    //Read: Sur une seule photo liée au produit :
    public Optional<Picture> findByProductId(Integer productId , Integer userId) {
        return jdbcTemplate.query(
                """
                SELECT p.id, p.url, p.position, p.product_id, p.created_at, p.updated_at
                       FROM pictures p
                       JOIN products pr ON pr.id = p.product_id
                       WHERE p.productId = ? AND pr.user_id = ?
                       ORDER BY position
                """,
                pictureRowMapper,
                productId,
                userId
        ).stream().findFirst();
    }

    //Create:
    public Picture save(Picture picture) {
        LocalDateTime now = LocalDateTime.now();
        picture.setCreatedAt(now);
        picture.setUpdatedAt(now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    """
                        INSERT INTO pictures (url, position, product_id, created_at, updated_at)
                        VALUES (?, ?, ?, ?,?)
                        """,
                    new String[]{"id"}
            );
            ps.setString(1, picture.getUrl());
            ps.setInt(2, picture.getPosition());
            ps.setInt(3, picture.getProductId());
            ps.setTimestamp(4, Timestamp.valueOf(picture.getCreatedAt()));
            ps.setTimestamp(5,Timestamp.valueOf(picture.getUpdatedAt()));
            return ps;
        }, keyHolder);

        picture.setId(keyHolder.getKey().intValue());
        return picture;
    }

    //Update:
    public int update(Integer id, Integer userId, Picture picture) {
        return jdbcTemplate.update(
                """
                UPDATE pictures p
                SET url = ?, position = ?, updated_at = ?
                FROM products pr
                WHERE pr.id= p.product_id AND p.id = ? pr.user_id = ?
                """,
                picture.getUrl(),
                picture.getPosition(),
                Timestamp.valueOf(LocalDateTime.now()),
                id,
                userId
        );
    }

    //Delete:
    public int deleteById(Integer id, Integer userId) {
        return jdbcTemplate.update(
                """
                DELETE FROM pictures p
                USING products pr
                WHERE pr.id = p.product_id AND p.id = ? AND pr.user_id = ?
                """,
                id,
                userId
        );
    }
}
