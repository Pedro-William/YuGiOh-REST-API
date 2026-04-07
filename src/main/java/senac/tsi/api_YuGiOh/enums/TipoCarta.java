package senac.tsi.api_YuGiOh.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TipoCarta {
    MONSTRO,
    MAGIA,
    ARMADILHA;

    @JsonCreator
    public static TipoCarta fromString(String value) {
        try {
            return TipoCarta.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Tipo inválido: '" + value + "'. Use: MONSTRO, MAGIA ou ARMADILHA"
            );
        }
    }
}
