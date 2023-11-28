package br.com.blueproject.transactionbff.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public enum TipoTransacao {
    PAGAMENTOS_TRIBUTOS,
    PAGAMENTO_TITULOS,
    TED,
    DOC;
}
