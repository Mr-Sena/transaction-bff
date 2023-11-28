package br.com.blueproject.transactionbff.dto;

import java.time.LocalDateTime;

public class RequestTransactionDto extends TransactionDto {

    private SituacaoEnum situacao;
    private LocalDateTime data;
}
