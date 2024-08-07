package br.com.blueproject.transactionbff.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

//Replace by the oldest Spring boot release [DOWNGRADE]:
//import jakarta.validation.Valid;
import javax.validation.Valid;

//import jakarta.validation.constraints.NotNull;
import javax.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(of = "uuid")
@RedisHash(value = "TransactionDto", timeToLive = 300) // Persistir os dados somente durante 5 minutos (300 segundos).
@ToString(of = {"uuid", "situacao"})
public class TransactionDto {

    @Id
    @Schema(description = "Propriedade para identificar o código da transação.")
    private UUID uuid;

    @Schema(description = "Valor da transação")
    @NotNull(message = "Favor, informar o valor da transação")
    private BigDecimal value;

    @Schema(description = "Data/hora/minuto e segundo da transação")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime data;

    @NotNull(message = "Favor, informar a origem da transação")
    @Schema(description = "Conta de origem da transação")
    @Valid
    private Conta conta;

    @Schema(description = "Beneficiário da transação")
    @Valid
    private BeneficiarioDto beneficiario;

    @NotNull(message = "Favor, informar o tipo da transação")
    @Schema(description = "Tipo de transação")
    private TipoTransacao tipoTransacao;

    @Schema(description = "Situação da transação")
    private SituacaoEnum situacao;

    public void naoAnalisada() {
        this.situacao = SituacaoEnum.NAO_ANALISADA;
    }
}
