package br.com.impacta.quarkus;

import java.math.BigDecimal;
import java.util.Random;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.json.internal.json_simple.parser.JSONParser;

import br.com.impacta.quarkus.Models.Solicitacao;
import io.smallrye.reactive.messaging.annotations.Blocking;
import org.jboss.logging.Logger;

/**
 * A bean consuming data from the "quote-requests" RabbitMQ queue and giving out
 * a random quote.
 * The result is pushed to the "quotes" RabbitMQ exchange.
 */
@ApplicationScoped
public class AlterarValorProcessor {
    private static final Logger LOGGER = Logger.getLogger(AlterarValorProcessor.class);
    @Inject
    ContaCorrenteService contaCorrenteService;

    @Incoming("requests")
    @Outgoing("retorno")
    @Blocking
    public Solicitacao process(String solicRequest) throws InterruptedException {
        Solicitacao sol = new Solicitacao();
        // simulate some hard-working task
        try {
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(solicRequest);
            System.out.println("Debitou ou Aplicacao: " + solicRequest);
            System.out.println(json.get("idConta"));
            System.out.println(json.get("valor"));
            System.out.println(json.get("Tipo"));
            ContaCorrente contaCorrenteEntity = new ContaCorrente();
            contaCorrenteEntity.setIdConta(Integer.parseInt(json.get("idConta").toString()));
            contaCorrenteEntity = contaCorrenteService.getContaCorrente(contaCorrenteEntity);
            BigDecimal money = new BigDecimal(json.get("valor").toString());
            if (!(contaCorrenteEntity.getSaldo().compareTo(money) >= 0)) {
                throw new Exception();
            }
            contaCorrenteService.Debito(contaCorrenteEntity, money);
            sol.tipo = json.get("Tipo").toString();
            sol.uid = json.get("Uid").toString();
            sol.valor = money;
            sol.idConta = Integer.parseInt(json.get("idConta").toString());

        } catch (Exception e) {
            LOGGER.error(e);
            // TODO: handle exception
        }
        return sol;
        // try {
        // ContaCorrente contaCorrenteEntity = new ContaCorrente();
        // contaCorrenteEntity.setIdConta(idConta);
        // contaCorrenteEntity =
        // contaCorrenteService.getContaCorrente(contaCorrenteEntity);

        // if (!(contaCorrenteEntity.getSaldo().compareTo(valor.valor) >= 0)) {
        // throw new BadRequestException();
        // }
        // ContaCorrente upcontaCorrenteEntity =
        // contaCorrenteService.Debito(contaCorrenteEntity, valor.valor);

        // return upcontaCorrenteEntity;
        // } catch (Exception e) {
        // LOGGER.error(e);
        // throw new BadRequestException();

        // }
    }
}