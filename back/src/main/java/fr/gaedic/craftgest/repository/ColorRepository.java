package fr.gaedic.craftgest.repository;

import fr.gaedic.craftgest.model.Color;
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
public class ColorRepository {
    private final JdbcTemplate jdbcTemplate; //Outil SQL

    //Rowmapper commun à toutes les méthodes:
    private final RowMapper<Color> colorRowMapper = (rs, rowNum) -> {
        Color color = new Color(); // 1. moule vide
        color.setId(rs.getInt("id"));                                           // 2. on lit "id" dans la ligne, puis on le range dans l'objet
        color.setName(rs.getString("name"));                                    //    texte → getString
        color.setUserId(rs.getInt("user_id"));                                  //    nom SQL en snake_case entre guillemets
        color.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());    //    date : Timestamp → LocalDateTime
        color.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return color;                                                                      // 3. on rend l'objet rempli
    };

    //Constructeur qui reçoit le Jdbc template et le range dans l'attribut ci dessus :
    // Spring appelle ce constructeur au démarrage et fournit le JdbcTemplate (injection de dépendances)
    public ColorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Color> findByUserId(Integer userId) {   // renvoie une liste de Color
        return jdbcTemplate.query(                       // query = SELECT qui renvoie plusieurs lignes
                """
                SELECT id, name, user_id, created_at, updated_at
                FROM colors
                WHERE user_id = ?
                ORDER BY name
                """,
                colorRowMapper,                          // notre RowMapper, réutilisé ici
                userId                                   // remplace le ? dans le SQL
        );
    }

    public Optional<Color> findById(Integer id , Integer userId) {
        return jdbcTemplate.query(
                """
                SELECT id, name, user_id, created_at, updated_at
                       FROM colors
                       WHERE id = ? AND user_id = ?
                """,
                colorRowMapper,
                id,
                userId
        ).stream().findFirst();
    }

    public Color save(Color color) {
        LocalDateTime now = LocalDateTime.now();          // date et heure actuelles
        color.setCreatedAt(now);
        color.setUpdatedAt(now);

        KeyHolder keyHolder = new GeneratedKeyHolder();   // boîte qui va recevoir l'id généré par PostgreSQL

        jdbcTemplate.update(connection -> {               // update = INSERT, UPDATE ou DELETE
            PreparedStatement ps = connection.prepareStatement(
                    """
                    INSERT INTO colors (name, user_id, created_at, updated_at)
                    VALUES (?, ?, ?, ?)
                    """,
                    new String[]{"id"}                    // "renvoie-moi la colonne id"
            );
            ps.setString(1, color.getName());             // 1er ?  (on lit l'objet avec ses getters)
            ps.setInt(2, color.getUserId());              // 2e ?
            ps.setTimestamp(3, Timestamp.valueOf(color.getCreatedAt()));   // LocalDateTime → Timestamp
            ps.setTimestamp(4, Timestamp.valueOf(color.getUpdatedAt()));
            return ps;
        }, keyHolder);

        color.setId(keyHolder.getKey().intValue());       // on récupère l'id et on le range dans l'objet
        return color;                                     // l'objet complet, avec son id
    }

    public int update(Integer id, Color color) {          // renvoie le nombre de lignes modifiées
        return jdbcTemplate.update(
                """
                UPDATE colors
                SET name = ?, updated_at = ?
                WHERE id = ?
                """,
                color.getName(),                           // 1er ?
                Timestamp.valueOf(LocalDateTime.now()),    // 2e ?  : date de modification = maintenant
                id                                         // 3e ?
        );
    }
    public int deleteById(Integer id) {                   // renvoie le nombre de lignes supprimées
        return jdbcTemplate.update(
                """
                DELETE FROM colors
                WHERE id = ?
                """,
                id
        );
    }
}
