package fr.gaedic.craftgest.repository;

import fr.gaedic.craftgest.model.User;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * Accès aux comptes utilisateurs en base.
 *
 * <p>Contrairement aux autres repositories, les méthodes ne filtrent pas par utilisateur. Le
 * contrôle d'accès (admin, ou utilisateur connecté qui agit sur son propre compte) se fait
 * dans le service. {@code update} et {@code deleteById} renvoient le nombre de lignes
 * modifiées (0 si l'utilisateur n'existe pas).
 */
@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    // Constructeur:
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper :
    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setPseudo(rs.getString("pseudo"));
        user.setMail(rs.getString("mail"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setBusiness(rs.getString("business"));
        user.setSlug(rs.getString("slug"));
        user.setRole(rs.getString(("role")));
        user.setStatus(rs.getString("status"));
        user.setMailVerified(rs.getBoolean("mail_verified"));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return user;
    };

    // Read : ADMIN Afficher une liste de tous les utilisateurs
    public List<User> findAllUser() {
        return jdbcTemplate.query("""
                SELECT id, pseudo, mail, password_hash, business, slug, role, status, mail_verified, created_at, updated_at
                FROM users
                ORDER BY created_at DESC
                """, userRowMapper);
    }
    // Read: Récupérer les infos d'un user:
    public Optional<User> findById(Integer id) {
        return jdbcTemplate.query("""
                        SELECT id, pseudo, mail, password_hash, business, slug, role, status, mail_verified, created_at, updated_at
                        FROM users
                        WHERE id = ?
                        """, userRowMapper, id).stream().findFirst();
    }

    // Read : Récupérer les infos d'un user depuis son mail:
    public Optional<User> findByMail(String mail) {
        return jdbcTemplate.query("""
                SELECT id, pseudo, mail, password_hash, business, slug, role, status, mail_verified, created_at, updated_at
                FROM users
                WHERE mail = ?
                        """, userRowMapper, mail).stream().findFirst();
    }

    // Create: Créer un nouvel utilisateur (inscription)
    public User save(User user) {
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement("""

                            INSERT INTO users (pseudo, mail, password_hash, business, slug, role, status, mail_verified, created_at, updated_at)
               VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
               """, new String[] {"id"});
                    ps.setString(1, user.getPseudo());
                    ps.setString(2, user.getMail());
                    ps.setString(3, user.getPasswordHash());
                    ps.setString(4, user.getBusiness());
                    ps.setString(5, user.getSlug());
                    ps.setString(6, user.getRole());
                    ps.setString(7, user.getStatus());
                    ps.setBoolean(8, user.getMailVerified());
                    ps.setTimestamp(9, Timestamp.valueOf(user.getCreatedAt()));
                    ps.setTimestamp(10, Timestamp.valueOf(user.getUpdatedAt()));
                    return ps;
                },
                keyHolder);

        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    // Update: USER Mettre à jour sa propre fiche utilisateur

    public int update(Integer id, User user) {
        return jdbcTemplate.update(
                """
                        UPDATE users
                        SET pseudo = ?, mail = ?, business = ?, slug = ?,  updated_at = ?
                        WHERE id = ?
                        """,
                user.getPseudo(),
                user.getMail(),
                user.getBusiness(),
                user.getSlug(),
                Timestamp.valueOf(LocalDateTime.now()),
                id);
    }

    // Delete :
    public int deleteById(Integer id) {
        return jdbcTemplate.update("""
                        DELETE FROM users
                        WHERE id = ?
                        """, id);
    }
}
