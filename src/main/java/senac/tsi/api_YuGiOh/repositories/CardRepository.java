package senac.tsi.api_YuGiOh.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import senac.tsi.api_YuGiOh.entities.CardEntity;
import org.springframework.data.domain.Pageable;
@Repository
public interface CardRepository extends JpaRepository<CardEntity, Long> {

    Page<CardEntity> findByNomeContaining(String nome, Pageable pageable);
}
