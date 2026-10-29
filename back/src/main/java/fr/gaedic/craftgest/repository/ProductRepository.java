package fr.gaedic.craftgest.repository;

import fr.gaedic.craftgest.model.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

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
}
