package br.com.impacta.quarkus;

import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Gauge;

import br.com.impacta.quarkus.Models.Saldos;
import br.com.impacta.quarkus.Services.BuscaSaldoCartaoRestClient;
import br.com.impacta.quarkus.Services.BuscaSaldoInvestimentosRestClient;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import org.jboss.logging.Logger;

@Path("/contas")
public class ContaCorrenteResource {

    private static final Logger LOGGER = Logger.getLogger(ContaCorrenteResource.class);

    @Inject
    MeterRegistry registry;

    @Inject
    ContaCorrenteService contaCorrenteService;

    @Inject
    @RestClient
    BuscaSaldoCartaoRestClient buscaSaldoCartaoRestClient;
    @Inject
    @RestClient
    BuscaSaldoInvestimentosRestClient buscaSaldoInvestimentosRestClient;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Retry(maxRetries = 4)
    @Timeout(250)
    public Set<ContaCorrente> listContaCorrente() {
        return contaCorrenteService.listContaCorrente();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{idConta}")
    @Retry(maxRetries = 4)
    @Timeout(250)
    public ContaCorrente getContaCorrente(@PathParam("idConta") Integer idConta) {
        ContaCorrente contaCorrenteEntity = new ContaCorrente();
        contaCorrenteEntity.setIdConta(idConta);
        contaCorrenteEntity = contaCorrenteService.getContaCorrente(contaCorrenteEntity);
        return contaCorrenteEntity;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Retry(maxRetries = 4)
    @Timeout(250)
    @RolesAllowed("admin")
    public ContaCorrente addContaCorrente(ContaCorrente conta) {
        ContaCorrente contaCorrenteEntity = contaCorrenteService.addContaCorrente(conta);
        registry.counter("contas_ativas", Tags.of("Conta", contaCorrenteEntity.getIdConta().toString())).increment();
        return contaCorrenteEntity;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Retry(maxRetries = 4)
    @Timeout(250)
    @Path("/id/{idConta}/credito")
    public ContaCorrente Credito(@PathParam("idConta") Integer idConta, Monto valor) {
        try {
            ContaCorrente contaCorrenteEntity = new ContaCorrente();
            contaCorrenteEntity.setIdConta(idConta);
            contaCorrenteEntity = contaCorrenteService.getContaCorrente(contaCorrenteEntity);
            ContaCorrente upcontaCorrenteEntity = contaCorrenteService.Credito(contaCorrenteEntity, valor.valor);
            return upcontaCorrenteEntity;
        } catch (Exception e) {
            LOGGER.error(e);
            throw new BadRequestException();

        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Retry(maxRetries = 4)
    @Timeout(250)
    @Path("/id/{idConta}/debito")
    public ContaCorrente Debito(@PathParam("idConta") Integer idConta, Monto valor) {
        try {
            ContaCorrente contaCorrenteEntity = new ContaCorrente();
            contaCorrenteEntity.setIdConta(idConta);
            contaCorrenteEntity = contaCorrenteService.getContaCorrente(contaCorrenteEntity);

            if (!(contaCorrenteEntity.getSaldo().compareTo(valor.valor) >= 0)) {
                throw new BadRequestException();
            }
            ContaCorrente upcontaCorrenteEntity = contaCorrenteService.Debito(contaCorrenteEntity, valor.valor);

            return upcontaCorrenteEntity;
        } catch (Exception e) {
            LOGGER.error(e);
            throw new BadRequestException();

        }
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{idConta}")
    @RolesAllowed("admin")
    @Retry(maxRetries = 4)
    @Timeout(250)
    public ContaCorrente deleteContaCorrente(@PathParam("idConta") Integer idConta) {
        ContaCorrente contaCorrenteEntity = new ContaCorrente();
        contaCorrenteEntity.setIdConta(idConta);
        contaCorrenteEntity = contaCorrenteService.deleteContaCorrente(contaCorrenteEntity);
        return contaCorrenteEntity;
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("saldo/id/{idConta}")
    @Retry(maxRetries = 4)
    @Timeout(150)
    @Fallback(fallbackMethod = "fallbackgetSaldosContas")
    public Saldos getSaldosContas(@PathParam("idConta") Integer idConta) {
        try {

            Monto fatura = buscaSaldoCartaoRestClient.getSaldoCartao(idConta);
            Monto investimentos = buscaSaldoInvestimentosRestClient.getSaldoInvestimentos(idConta);
            ContaCorrente contaCorrenteEntity = new ContaCorrente();
            contaCorrenteEntity.setIdConta(idConta);
            contaCorrenteEntity = contaCorrenteService.getContaCorrente(contaCorrenteEntity);
            Saldos saldosFinal = new Saldos();
            saldosFinal.FaturaCartao = fatura.getValor();
            saldosFinal.ContaCorrente = contaCorrenteEntity.getSaldo();
            saldosFinal.Investimentos = investimentos.getValor();
            return saldosFinal;
        } catch (Exception e) {
            LOGGER.error(e);
            throw new BadRequestException();
        }
    }

    public Saldos fallbackgetSaldosContas(@PathParam("idConta") Integer idConta) {
        try {
            LOGGER.info("FALLBACK SALDOS CONTAS");
            ContaCorrente contaCorrenteEntity = new ContaCorrente();
            contaCorrenteEntity.setIdConta(idConta);
            contaCorrenteEntity = contaCorrenteService.getContaCorrente(contaCorrenteEntity);
            Saldos saldosFinal = new Saldos();
            saldosFinal.ContaCorrente = contaCorrenteEntity.getSaldo();
            return saldosFinal;
        } catch (Exception e) {
            LOGGER.error(e);
            throw new BadRequestException();
        }
    }

    @Gauge(name = "QUARKUS_QUANTIDADE_CONTAS", unit = MetricUnits.NONE, description = "QUANTIDADE DE CONTAS")
    public long checkCustomerAmmout(){
        return contaCorrenteService.listContaCorrente().size();
    }

}