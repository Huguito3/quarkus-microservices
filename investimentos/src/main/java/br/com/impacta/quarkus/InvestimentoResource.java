package br.com.impacta.quarkus;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jose4j.json.internal.json_simple.JSONObject;

import br.com.impacta.quarkus.Models.ContaCorrente;
import br.com.impacta.quarkus.Models.Investimento;
import br.com.impacta.quarkus.Models.Monto;
import br.com.impacta.quarkus.Models.Solicitacao;
import br.com.impacta.quarkus.Services.ContaCorrenteRestClient;
import io.smallrye.mutiny.Multi;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@Path("/investimentos")
public class InvestimentoResource {
    @Channel("retorno") Multi<Solicitacao> retornoSolicitacoes; 

    @Channel("solicitacao-requests")
    Emitter<String> debitoRequestEmitter;

    @Inject
    @RestClient
    ContaCorrenteRestClient contaCorrenteRestClient;

    @Inject
    InvestimentoService InvestimentoService;

   

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Retry(maxRetries = 4)
    @Timeout(150)
    public Set<Investimento> listInvestimento() {
        return InvestimentoService.listInvestimento();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{idInvestimento}")
    @Retry(maxRetries = 4)
    @Timeout(150)
    public Investimento getInvestimento(@PathParam("idInvestimento") Integer idInvestimento) {
        Investimento InvestimentoEntity = new Investimento();
        InvestimentoEntity.setIdInvestimento(idInvestimento);
        InvestimentoEntity = InvestimentoService.getInvestimento(InvestimentoEntity);
        return InvestimentoEntity;
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("saldo/conta/id/{idConta}")
    @Retry(maxRetries = 4)
    @Timeout(150)
    public Monto getSaldoInvestimentos(@PathParam("idConta") Integer idConta) {

        Set<Investimento> listaInvestimentos = InvestimentoService.listInvestimento();

        Monto montoTotal = new Monto();
        montoTotal.valor = BigDecimal.ZERO;
        for (Investimento investimento : listaInvestimentos) {
            System.out.println(investimento.getSaldo());
            montoTotal.valor = montoTotal.valor.add(investimento.getSaldo());
        }
        return montoTotal;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    @Retry(maxRetries = 4)
    @Timeout(150)
    public Investimento addInvestimento(Investimento conta) {

        try {
            ContaCorrente contaSelecionada = contaCorrenteRestClient.getContaCorrente(conta.getIdConta());

            if (contaSelecionada == null) {
                throw new NotFoundException("Conta não Localizada");
            }
            Investimento InvestimentoEntity = InvestimentoService.addInvestimento(conta);
            return InvestimentoEntity;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Mensaje Erro: " + e);
            throw new BadRequestException();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{idInvestimento}/aplicacao")
    // @Retry(maxRetries = 4)
    @Timeout(500)
    public Investimento Aplicacao(@PathParam("idInvestimento") Integer idInvestimento, Monto valor) {
        try {
            Investimento InvestimentoEntity = new Investimento();
            InvestimentoEntity.setIdInvestimento(idInvestimento);
            InvestimentoEntity = InvestimentoService.getInvestimento(InvestimentoEntity);

            ContaCorrente contaSelecionada = contaCorrenteRestClient.getContaCorrente(InvestimentoEntity.getIdConta());
            if (contaSelecionada == null) {
                throw new NotFoundException("Conta não Localizada");
            }
            Thread.sleep(200);
            // contaCorrenteRestClient.Debito(InvestimentoEntity.getIdConta(), valor);
            // Investimento upInvestimentoEntity = InvestimentoService.Aplicacao(InvestimentoEntity, valor.valor);
            UUID uuid = UUID.randomUUID();
            HashMap<String,String> novo = new HashMap<String,String>();
            novo.put("Uid", uuid.toString());
            novo.put("idConta", idInvestimento.toString());
            novo.put("valor",valor.valor.toString());
            novo.put("Tipo", "D");
            System.out.println("Chamando");
            JSONObject json = new JSONObject(novo);
            debitoRequestEmitter.send(json.toString());
            // contaCorrenteRestClient.Debito(InvestimentoEntity.getIdConta(), valor);
            Investimento upInvestimentoEntity = InvestimentoService.Aplicacao(InvestimentoEntity, valor.valor);
            return upInvestimentoEntity;
        } catch (Exception e) {
            throw new BadRequestException();

        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{idInvestimento}/resgate")
    @Retry(maxRetries = 4)
    @Timeout(150)
    public Investimento Resgate(@PathParam("idInvestimento") Integer idInvestimento, Monto valor) {
        try {
            Investimento InvestimentoEntity = new Investimento();
            InvestimentoEntity.setIdInvestimento(idInvestimento);
            InvestimentoEntity = InvestimentoService.getInvestimento(InvestimentoEntity);

            if (!(InvestimentoEntity.getSaldo().compareTo(valor.valor) >= 0)) {
                throw new BadRequestException();
            }
            ContaCorrente contaSelecionada = contaCorrenteRestClient.getContaCorrente(InvestimentoEntity.getIdConta());
            if (contaSelecionada == null) {
                throw new NotFoundException("Conta não Localizada");
            }
            // contaCorrenteRestClient.Credito(InvestimentoEntity.getIdConta(), valor);
            UUID uuid = UUID.randomUUID();
            HashMap<String,String> novo = new HashMap<String,String>();
            novo.put("Uid", uuid.toString());
            novo.put("idConta", idInvestimento.toString());
            novo.put("valor",valor.valor.toString());
            novo.put("Tipo", "C");
            System.out.println("Chamando");
            JSONObject json = new JSONObject(novo);
            debitoRequestEmitter.send(json.toString());
            Investimento upInvestimentoEntity = InvestimentoService.Resgate(InvestimentoEntity, valor.valor);
            return upInvestimentoEntity;
        } catch (Exception e) {
            throw new BadRequestException();

        }
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{idInvestimento}")
    @RolesAllowed("admin")
    @Retry(maxRetries = 4)
    @Timeout(150)
    public Investimento deleteInvestimento(@PathParam("idInvestimento") Integer idInvestimento) {
        Investimento InvestimentoEntity = new Investimento();
        InvestimentoEntity.setIdInvestimento(idInvestimento);
        InvestimentoEntity = InvestimentoService.deleteInvestimento(InvestimentoEntity);
        return InvestimentoEntity;
    }

    @GET
    @Retry(maxRetries = 4)
    @Path("/listaSolicitacoes")
    @Produces(MediaType.APPLICATION_JSON) 
    public Multi<Solicitacao> stream() {
        System.out.println("RETORNO SOLICITACOES");
        return retornoSolicitacoes; 
    }

//     @GET
//     @Produces(MediaType.APPLICATION_JSON)
//     @Retry(maxRetries = 4)
//     @Timeout(150)
//     public Set<Investimento> listInvestimento() {
//         return InvestimentoService.listInvestimento();
//     }
}