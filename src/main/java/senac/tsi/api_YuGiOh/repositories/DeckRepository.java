package senac.tsi.api_YuGiOh.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import senac.tsi.api_YuGiOh.entities.Deck;

@Repository
public interface DeckRepository extends JpaRepository<Deck, Long> {
    Page<Deck> findByNomeContaining(String nome, Pageable pageable);
}
