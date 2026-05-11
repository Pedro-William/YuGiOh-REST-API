package senac.tsi.api_YuGiOh.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import senac.tsi.api_YuGiOh.entities.CardEntity;
import senac.tsi.api_YuGiOh.exceptions.ResourceNotFoundException;
import senac.tsi.api_YuGiOh.repositories.CardRepository;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository repo;

    public Page<CardEntity> listar(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public CardEntity buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carta não encontrada"));
    }

    public CardEntity criar(CardEntity card) {
        return repo.save(card);
    }

    public CardEntity atualizar(Long id, CardEntity card) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Carta não encontrada");
        }
        card.setId(id);
        return repo.save(card);
    }

    public void deletar(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Carta não encontrada");
        }
        repo.deleteById(id);
    }

    public Page<CardEntity> buscarPorNome(String nome, Pageable pageable) {

        Page<CardEntity> resultado =
                repo.findByNomeContainingIgnoreCase(nome, pageable);

        if (resultado.getTotalElements() == 0) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Nenhuma carta encontrada com esse nome"
            );
        }

        return resultado;
    }
}
