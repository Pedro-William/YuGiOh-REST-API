package senac.tsi.api_YuGiOh.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import senac.tsi.api_YuGiOh.entities.Efeito;


@Repository
public interface EfeitoRepository extends JpaRepository<Efeito, Long> {

    Page<Efeito> findByDescricaoContainingIgnoreCase(String descricao, Pageable pageable);

}
