package fr.gaedic.craftgest.repository;

import fr.gaedic.craftgest.model.Product;
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

@Repository
public class ProductRepository {
    private final JdbcTemplate jdbcTemplate;

    // Constructeur:
    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper :
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

    // Read : Afficher une liste de tous les produits // A revoir au moment de la création de l'écran affichage au
    // front.

    public List<Product> findByUserId(Integer userId) {
        return jdbcTemplate.query("""
                SELECT p.id, p.name, p.code_ref, p.description, p.price, p.cost_material, p.size, p.stock, p.is_visible_site, p.archive, p.user_id, p.type_id, p.collection_id, p.finding_id, p.created_at, p.updated_at
                FROM products p
                WHERE p.user_id = ?
                ORDER BY p.created_at DESC
                """, productRowMapper, userId);
    }

    // Read: afficher un produit en particulier

    public Optional<Product> findById(Integer productId, Integer userId) {
        return jdbcTemplate.query("""
               SELECT p.id, p.name, p.code_ref, p.description, p.price, p.cost_material, p.size, p.stock, p.is_visible_site, p.archive, p.user_id, p.type_id, p.collection_id, p.finding_id, p.created_at, p.updated_at
               FROM products p
               WHERE p.user_id = ? AND p.id = ?
               """, productRowMapper, userId, productId).stream()
                .findFirst();
    }

    // Create: Créer un nouveau produit

    public Product save(Product product) {
        LocalDateTime now = LocalDateTime.now();
        product.setCreatedAt(now);
        product.setUpdatedAt(now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement("""
               INSERT INTO products (name, code_ref, description, price, cost_material, size, stock, is_visible_site, archive, user_id, type_id, collection_id, finding_id, created_at, updated_at)
               VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
               """, new String[] {"id"});
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
                },
                keyHolder);

        product.setId(keyHolder.getKey().intValue());
        return product;
    }

    // Update: Mettre une produit à jour

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

    // Delete :
    public int deleteById(Integer id, Integer userId) {
        return jdbcTemplate.update("""
                        DELETE FROM products
                        WHERE user_id= ? AND id = ?
                        """, userId, id);
    }

    // Tables de liaison (product_colors, product_materials, product_universes) :
    // une ligne = un produit relié à une couleur / une matière / un univers.
    // Lecture et suppression vérifient le propriétaire par jointure avec products.
    // Ajout : le service doit vérifier avant que le produit ET la couleur (matière, univers) appartiennent à
    // l'utilisateur.
    // Les liaisons sont supprimées toutes seules avec le produit (ON DELETE CASCADE dans le script V1).

    // Couleurs : les ids des couleurs d'un produit
    public List<Integer> findColorIdsByProductId(Integer productId, Integer userId) {
        return jdbcTemplate.queryForList("""
                SELECT pc.color_id
                FROM product_colors pc
                JOIN products p ON p.id = pc.product_id
                WHERE pc.product_id = ? AND p.user_id = ?
                """, Integer.class, productId, userId);
    }

    public int addColor(Integer productId, Integer colorId) {
        return jdbcTemplate.update("""
                INSERT INTO product_colors (product_id, color_id)
                VALUES (?, ?)
                """, productId, colorId);
    }

    public int removeColor(Integer productId, Integer colorId, Integer userId) {
        return jdbcTemplate.update("""
                DELETE FROM product_colors pc
                USING products p
                WHERE p.id = pc.product_id
                  AND pc.product_id = ? AND pc.color_id = ? AND p.user_id = ?
                """, productId, colorId, userId);
    }

    // Matières
    public List<Integer> findMaterialIdsByProductId(Integer productId, Integer userId) {
        return jdbcTemplate.queryForList("""
                SELECT pm.material_id
                FROM product_materials pm
                JOIN products p ON p.id = pm.product_id
                WHERE pm.product_id = ? AND p.user_id = ?
                """, Integer.class, productId, userId);
    }

    public int addMaterial(Integer productId, Integer materialId) {
        return jdbcTemplate.update("""
                INSERT INTO product_materials (product_id, material_id)
                VALUES (?, ?)
                """, productId, materialId);
    }

    public int removeMaterial(Integer productId, Integer materialId, Integer userId) {
        return jdbcTemplate.update("""
                DELETE FROM product_materials pm
                USING products p
                WHERE p.id = pm.product_id
                  AND pm.product_id = ? AND pm.material_id = ? AND p.user_id = ?
                """, productId, materialId, userId);
    }

    // Univers
    public List<Integer> findUniverseIdsByProductId(Integer productId, Integer userId) {
        return jdbcTemplate.queryForList("""
                SELECT pu.universe_id
                FROM product_universes pu
                JOIN products p ON p.id = pu.product_id
                WHERE pu.product_id = ? AND p.user_id = ?
                """, Integer.class, productId, userId);
    }

    public int addUniverse(Integer productId, Integer universeId) {
        return jdbcTemplate.update("""
                INSERT INTO product_universes (product_id, universe_id)
                VALUES (?, ?)
                """, productId, universeId);
    }

    public int removeUniverse(Integer productId, Integer universeId, Integer userId) {
        return jdbcTemplate.update("""
                DELETE FROM product_universes pu
                USING products p
                WHERE p.id = pu.product_id
                  AND pu.product_id = ? AND pu.universe_id = ? AND p.user_id = ?
                """, productId, universeId, userId);
    }
}
