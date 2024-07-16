package br.com.blueproject.transactionbff.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RequestTransactionDto extends TransactionDto {

    @JsonIgnore
    private SituacaoEnum situacao;
    @JsonIgnore
    private LocalDateTime data;
}
