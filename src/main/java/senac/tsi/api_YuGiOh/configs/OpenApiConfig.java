package senac.tsi.api_YuGiOh.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Yu-Gi-Oh")
                        .description(
                                "A API YuGiOh tem como objetivo gerenciar cartas e suas relações dentro de um sistema inspirado no jogo de cartas. A aplicação permite cadastrar, consultar, atualizar e remover informações relacionadas às cartas, seus atributos, efeitos, coleções e decks.\n\n" +

                                        "O sistema foi desenvolvido utilizando Spring Boot, seguindo o padrão REST, com persistência em banco de dados H2 e uso de Spring Data JPA para mapeamento objeto-relacional.\n\n" +

                                        "A aplicação contempla as seguintes entidades:\n\n" +

                                        "• Cartas: representa uma carta do jogo, contendo nome, tipo, atributo, coleção e efeitos associados.\n" +
                                        "  Funcionalidades: criação, listagem paginada, busca por ID, busca por nome, atualização e remoção.\n\n" +

                                        "• Atributos: representa o tipo elemental da carta (ex: LUZ, TREVAS).\n" +
                                        "  Funcionalidades: criação, listagem, busca por ID, busca por nome, atualização e remoção.\n\n" +

                                        "• Coleçoes: representa o conjunto ao qual a carta pertence.\n" +
                                        "  Funcionalidades: criação, listagem, busca por ID, busca por nome, atualização e remoção.\n\n" +

                                        "• Efeitos: representa habilidades ou ações de uma carta.\n" +
                                        "  Funcionalidades: criação, listagem, busca por ID, busca por descrição, atualização e remoção.\n\n" +

                                        "• Decks: representa um conjunto de cartas utilizadas por um jogador.\n" +
                                        "  Funcionalidades: criação com múltiplas cartas, listagem, busca por ID, busca por nome, atualização e remoção.\n\n" +

                                        "Relacionamentos implementados:\n" +
                                        "- OneToMany: Atributo → Carta\n" +
                                        "- OneToMany: Coleção → Carta\n" +
                                        "- ManyToMany: Carta ↔ Efeito\n" +
                                        "- ManyToMany: Carta ↔ Deck\n\n" +

                                        "A API segue o padrão REST utilizando os métodos GET, POST, PUT e DELETE, com suporte a paginação em todas as listagens através de Pageable.\n\n" +

                                        "Códigos de status HTTP utilizados:\n" +
                                        "- 200 OK: operações realizadas com sucesso (consultas e atualizações)\n" +
                                        "- 201 Created: recurso criado com sucesso\n" +
                                        "- 204 No Content: remoção realizada com sucesso\n" +
                                        "- 400 Bad Request: dados inválidos ou requisição mal formatada\n" +
                                        "- 404 Not Found: recurso não encontrado\n\n" +

                                        "A API também implementa HATEOAS para navegação entre recursos e possui documentação interativa com Swagger (OpenAPI), facilitando o uso e testes dos endpoints.\n\n" +

                                        "© 2026 Pedro Almeida. Todos os direitos reservados."
                        )
                        .version("1.1"));
    }
}

