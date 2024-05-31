package br.com.blueproject.transactionbff.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public class RequestTransactionDto extends TransactionDto {

    @JsonIgnore
    private SituacaoEnum situacao;
    @JsonIgnore
    private LocalDateTime data;
}
