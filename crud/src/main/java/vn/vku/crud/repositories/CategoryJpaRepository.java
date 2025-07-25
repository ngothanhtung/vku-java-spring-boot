package vn.vku.crud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.vku.crud.entities.Category;

@Repository
public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

}
