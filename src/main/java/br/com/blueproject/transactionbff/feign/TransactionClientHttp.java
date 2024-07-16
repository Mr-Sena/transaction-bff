package br.com.blueproject.transactionbff.feign;

import br.com.blueproject.transactionbff.dto.LimiteDiarioDto;
import br.com.blueproject.transactionbff.dto.TransactionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient( value = "transaction", url = "${transaction.url}")
public interface TransactionClientHttp {

    @RequestMapping( path = "/transaction/{agencia}/{conta}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    List<TransactionDto> buscarTransferencias(@PathVariable("agencia") final Long agencia, @PathVariable("conta") final Long conta);
}
