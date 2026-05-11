package senac.tsi.api_YuGiOh.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import senac.tsi.api_YuGiOh.entities.Atributo;
@Repository
public interface AtributoRepository extends JpaRepository<Atributo, Long> {

    Page<Atributo> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

}
