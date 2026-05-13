package senac.tsi.api_YuGiOh.configs;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class IdempotencyStorage {

    private final Map<String, Object> storage = new ConcurrentHashMap<>();

    public boolean existe(String key) {
        return storage.containsKey(key);
    }

    public <T> T buscar(String key) {
        return (T) storage.get(key);
    }

    public void salvar(String key, Object response) {
        storage.put(key, response);
    }
}