package br.com.blueproject.transactionbff.dto;

import io.swagger.v3.oas.annotations.media.Schema;

//import jakarta.validation.constraints.NotNull;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class Conta implements Serializable {

    private static final long serialVersionUID = -2980968685936210499L;

    @Schema(description = "Código da Agência")
    @NotNull(message = "Favor, informar o código da agência.")
    private Long codigoAgencia;

    @Schema(description = "Codigo da Conta")
    @NotNull(message = "Favor, informar o código da conta.")
    private Long codigoConta;
}
