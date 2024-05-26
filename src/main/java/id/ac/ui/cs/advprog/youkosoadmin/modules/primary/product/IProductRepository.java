package id.ac.ui.cs.advprog.youkosoadmin.modules.primary.product;

import id.ac.ui.cs.advprog.youkosoadmin.models.primary.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findAllBy(Pageable pageable);

    Page<Product> findByProductNameContainingIgnoreCase(String productName, Pageable pageable);
}
