package br.com.blueproject.transactionbff.api;

import br.com.blueproject.transactionbff.dto.LimiteDiarioDto;
import br.com.blueproject.transactionbff.feign.LimiteClientHttp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/limites")
public class LimiteController {

    private LimiteClientHttp limiteClient;

    public LimiteController(LimiteClientHttp limiteClient) {
        this.limiteClient = limiteClient;
    }

    @GetMapping( value = "/{agencia}/{conta}" )
    public LimiteDiarioDto buscarLimiteDiario(@PathVariable("agencia") final Long agencia, @PathVariable("conta") final Long conta) {

        return limiteClient.buscarLimiteDiario(agencia, conta);

    }
}
