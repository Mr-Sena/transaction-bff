package br.com.blueproject.transactionbff.dto;

import io.swagger.v3.oas.annotations.media.Schema;

//import jakarta.validation.constraints.NotNull;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class BeneficiarioDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 4370085483662215437L;

    @Schema(description = "CPF do Beneficiário")
    @NotNull(message = "favor, informar o CPF.")
    private Long cpf;

    @NotNull(message = "Favor, informar o código do banco do destino.")
    @Schema(description = "Código do banco destino")
    private Long codigoBanco;

    @NotNull(message = "Favor, informar o código da agência destino.")
    @Schema(description = "Código de agência destino")
    private String agencia;

    @NotNull(message = "Favor, informar o código da conta destino.")
    @Schema(description = "Código da conta destino")
    private String conta;

    @NotNull(message = "Favor, informar o nome do favorecido.")
    @Schema(description = "Nome do Favorecido.")
    private String nomeFavorecido;
}
