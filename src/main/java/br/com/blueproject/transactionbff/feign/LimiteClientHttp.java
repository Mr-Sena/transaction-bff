package br.com.blueproject.transactionbff.feign;

import br.com.blueproject.transactionbff.dto.LimiteDiarioDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient( value = "limites", url = "${limites.url}")
public interface LimiteClientHttp {

    @RequestMapping( path = "/limite-diario/{agencia}/{conta}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public LimiteDiarioDto buscarLimiteDiario(@PathVariable("agencia") final Long agencia, @PathVariable("conta") final Long conta);
}
