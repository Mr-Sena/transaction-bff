package br.com.blueproject.transactionbff.api;

import br.com.blueproject.transactionbff.domain.LimiteService;
import br.com.blueproject.transactionbff.dto.LimiteDiarioDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/limites")
public class LimiteController {

    private LimiteService service;

    public LimiteController(LimiteService service) {
        this.service = service;
    }

    @GetMapping( value = "/{agencia}/{conta}" )
    public LimiteDiarioDto buscarLimiteDiario(@PathVariable("agencia") final Long agencia, @PathVariable("conta") final Long conta) {

        return service.buscarLimiteDiario(agencia, conta);

    }
}
