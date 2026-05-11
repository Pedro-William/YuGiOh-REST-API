package senac.tsi.api_YuGiOh.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import senac.tsi.api_YuGiOh.entities.Colecao;


@Repository
public interface ColecaoRepository extends JpaRepository<Colecao, Long> {

    Page<Colecao> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

}
