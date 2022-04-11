package br.com.impacta.quarkus;

import java.math.BigDecimal;
import java.util.Set;

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

import org.eclipse.microprofile.rest.client.inject.RestClient;

import br.com.impacta.quarkus.Models.ContaCorrente;
import br.com.impacta.quarkus.Models.Investimento;
import br.com.impacta.quarkus.Models.Monto;
import br.com.impacta.quarkus.Services.ContaCorrenteRestClient;

@Path("/investimentos")
public class InvestimentoResource {
    @Inject
    @RestClient
    ContaCorrenteRestClient contaCorrenteRestClient;

    @Inject
    InvestimentoService InvestimentoService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Investimento> listInvestimento() {
        return InvestimentoService.listInvestimento();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{idInvestimento}")
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
    public Investimento Aplicacao(@PathParam("idInvestimento") Integer idInvestimento, Monto valor) {
        try {
            Investimento InvestimentoEntity = new Investimento();
            InvestimentoEntity.setIdInvestimento(idInvestimento);
            InvestimentoEntity = InvestimentoService.getInvestimento(InvestimentoEntity);

            ContaCorrente contaSelecionada = contaCorrenteRestClient.getContaCorrente(InvestimentoEntity.getIdConta());
            if (contaSelecionada == null) {
                throw new NotFoundException("Conta não Localizada");
            }
            contaCorrenteRestClient.Debito(InvestimentoEntity.getIdConta(), valor);
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
            contaCorrenteRestClient.Credito(InvestimentoEntity.getIdConta(), valor);
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
    public Investimento deleteInvestimento(@PathParam("idInvestimento") Integer idInvestimento) {
        Investimento InvestimentoEntity = new Investimento();
        InvestimentoEntity.setIdInvestimento(idInvestimento);
        InvestimentoEntity = InvestimentoService.deleteInvestimento(InvestimentoEntity);
        return InvestimentoEntity;
    }

}