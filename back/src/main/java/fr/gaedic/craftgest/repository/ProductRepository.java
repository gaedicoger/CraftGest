package fr.gaedic.craftgest.repository;

import fr.gaedic.craftgest.model.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public class ProductRepository {
    private final JdbcTemplate jdbcTemplate;

    //Constructeur:
    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //RowMapper :
    private final RowMapper<Product> productRowMapper = (rs, rowNum) -> {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setCodeRef(rs.getString("code_ref"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setCostMaterial(rs.getBigDecimal("cost_material"));
        product.setSize(rs.getString(("size")));
        product.setStock(rs.getInt("stock"));
        product.setVisibleSite(rs.getBoolean("is_visible_site"));
        product.setArchive(rs.getBoolean("archive"));
        product.setUserId((rs.getInt("user_id")));
        product.setTypeId(rs.getInt("type_id"));
        product.setCollectionId((rs.getObject("collection_id", Integer.class)));
        product.setFindingId(rs.getObject("finding_id", Integer.class));
        product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        product.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return product;
    };

    // Read : Afficher une liste de tous les produits // A revoir au moment de la création de l'écran affichage au front.

    public List<Product> findByUserId(Integer userId) {
        return jdbcTemplate.query(
                """
                SELECT p.id, p.name, p.code_ref, p.description, p.price, p.cost_material, p.size, p.stock, p.is_visible_site, p.archive, p.user_id, p.type_id, p.collection_id, p.finding_id, p.created_at, p.updated_at
                FROM products p
                WHERE p.user_id = ?
                ORDER BY p.created_at DESC
                """, productRowMapper,userId
        );
    }

    //Read: afficher un produit en particulier

    public Optional<Product> findById (Integer productId, Integer userId) {
        return jdbcTemplate.query(
                """
               SELECT p.id, p.name, p.code_ref, p.description, p.price, p.cost_material, p.size, p.stock, p.is_visible_site, p.archive, p.user_id, p.type_id, p.collection_id, p.finding_id, p.created_at, p.updated_at
               FROM products p
               WHERE p.user_id = ? AND p.id = ?
               """,
                productRowMapper, userId, productId
       ).stream().findFirst();
   }

   //Create: Créer un nouveau produit

    public Product save(Product product) {
        LocalDateTime now = LocalDateTime.now();
        product.setCreatedAt(now);
        product.setUpdatedAt(now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update( connection -> {
            PreparedStatement ps =connection.prepareStatement(
                    """
               INSERT INTO products (name, code_ref, description, price, cost_material, size, stock, is_visible_site, archive, user_id, type_id, collection_id, finding_id, created_at, updated_at)
               VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
               """,
                    new String[]{"id"}
            );
            ps.setString(1, product.getName());
            ps.setString(2, product.getCodeRef());
            ps.setString(3, product.getDescription());
            ps.setBigDecimal(4, product.getPrice());
            ps.setBigDecimal(5, product.getCostMaterial());
            ps.setString(6, product.getSize());
            ps.setInt(7, product.getStock());
            ps.setBoolean(8, product.getVisibleSite());
            ps.setBoolean(9, product.getArchive());
            ps.setInt(10, product.getUserId());
            ps.setInt(11, product.getTypeId());
            ps.setObject(12, product.getCollectionId());
            ps.setObject(13, product.getFindingId());
            ps.setTimestamp(14, Timestamp.valueOf(product.getCreatedAt()));
            ps.setTimestamp(15, Timestamp.valueOf(product.getUpdatedAt()));
            return ps;
        }, keyHolder);

        product.setId(keyHolder.getKey().intValue());
        return product;
    }

    //Update: Mettre une produit à jour

    public int update(Integer id, Integer userId, Product product) {
        return jdbcTemplate.update(
                """
                        UPDATE products
                        SET name = ?, description = ?, code_ref = ?, price = ?, cost_material = ?, size = ?, stock = ?, is_visible_site = ?, archive = ?, type_id = ?, collection_id = ?, finding_id =?, updated_at = ?
                        WHERE user_id = ? AND id = ?
                        """,
                product.getName(),
                product.getDescription(),
                product.getCodeRef(),
                product.getPrice(),
                product.getCostMaterial(),
                product.getSize(),
                product.getStock(),
                product.getVisibleSite(),
                product.getArchive(),
                product.getTypeId(),
                product.getCollectionId(),
                product.getFindingId(),
                Timestamp.valueOf(LocalDateTime.now()),
                userId,
                id);
    }

    //Delete :
    public int deleteById(Integer id, Integer userId) {
        return jdbcTemplate.update(
                """
                        DELETE FROM products
                        WHERE user_id= ? AND id = ?
                        """,
                userId, id );
    }
}
