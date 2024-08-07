package br.com.blueproject.transactionbff.api;

import br.com.blueproject.transactionbff.domain.TransactionService;
import br.com.blueproject.transactionbff.dto.RequestTransactionDto;
import br.com.blueproject.transactionbff.dto.TransactionDto;
import br.com.blueproject.transactionbff.tratamentodesvio.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transaction")
@Tag(name = "/transaction", description = "Grupo de API's para manipulação de transações financeiras.")
public class TransactionController {

    //These keys available references from spring cloud config server.
    @Value("${transacoes.duration}")
    private Long duration;

    @Value("${transacoes.events}")
    private String events;

    private TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @Operation(description = "API para criar um transacao financeira.")
    @ResponseBody
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Retorno OK com a transação efetuada."),
            @ApiResponse(responseCode = "401", description = "Erro de autenticação dessa API. "),
            @ApiResponse(responseCode = "403", description = "Erro de autorização dessa API."),
            @ApiResponse(responseCode = "404", description = "Recursos não foi localizado.")
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<RequestTransactionDto> enviarTransacao(@RequestBody final RequestTransactionDto requestTransactionDto) {

        return service.save(requestTransactionDto);
    }


    @GetMapping( value = "/{agencia}/{conta}" )
    public Flux<List<TransactionDto>> buscarTransferencias(@PathVariable("agencia") final Long agencia, @PathVariable("conta") final Long conta) {

        var data = service.findContaStream(agencia, conta);
        return data;

    }


    @GetMapping( value = "/sse/{agencia}/{conta}" )
    public Flux<ServerSentEvent<List<TransactionDto>>> buscarTransferenciasSSE(@PathVariable("agencia") final Long agencia, @PathVariable("conta") final Long conta) {

        return Flux.interval(Duration.ofSeconds(duration)).map( // O valor do intervalo determina um novo mapeamento de eventos após esse tempo específicado.
                sequence -> ServerSentEvent.<List<TransactionDto>>builder()
                        .id(String.valueOf(sequence))
                        .event(events)
                        .data(queryResult(agencia, conta))
                        .retry(Duration.ofSeconds(1)) // Em caso de falha, repetir a nova tentativa após 1 segundo.
                        .build());
    }

    private List<TransactionDto> queryResult(Long agencia, Long conta) {
        return service.findByAgenciaAndConta(agencia, conta);
    }


    @Operation(description = "API para buscar as trasações persistidas por id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Retorno OK com a Lista de transaçãoes."),
            @ApiResponse(responseCode = "401", description = "Erro de autenticação dessa API."),
            @ApiResponse(responseCode = "403", description = "Erro de autorização dessa API."),
            @ApiResponse(responseCode = "404", description = "Recursos não foi localizado.")
    })
    @Parameters(value = { @Parameter(name = "id", in = ParameterIn.PATH) })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Mono<TransactionDto> buscarTransacao(@PathVariable("id") final String uuid) {

        final Optional<TransactionDto> transactionDto = service.findById(uuid);
        if (transactionDto.isPresent()) {
            return Mono.just(transactionDto.get());
        }

        throw new NotFoundException("Não foi possível localizar o recurso.");
    }



    @Operation(description = "API para remover as trasações persistidas por id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Transação removida com sucesso."),
            @ApiResponse(responseCode = "401", description = "Erro de autenticação dessa API."),
            @ApiResponse(responseCode = "403", description = "Erro de autorização dessa API."),
            @ApiResponse(responseCode = "404", description = "Recursos não foi localizado.")
    })
    @Parameters(value = { @Parameter(name = "id", in = ParameterIn.PATH) })
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TransactionDto> removerTransacao(@PathVariable("id") final String uuid) {

        return Mono.empty();
    }



    @Operation(description = "API para autorizar transação financeira.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Transação removida com sucesso."),
            @ApiResponse(responseCode = "401", description = "Erro de autenticação dessa API."),
            @ApiResponse(responseCode = "403", description = "Erro de autorização dessa API."),
            @ApiResponse(responseCode = "404", description = "Recursos não foi localizado.")
    })
    @Parameters(value = { @Parameter(name = "id", in = ParameterIn.PATH) })
    @PatchMapping(value = "/{id}/confirmar")
    public Mono<TransactionDto> confirmarTransacao(@PathVariable("id") final String uuid) {

        return Mono.empty();
    }

}
