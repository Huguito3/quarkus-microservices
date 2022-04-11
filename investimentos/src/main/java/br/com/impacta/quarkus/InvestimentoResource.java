package br.com.impacta.quarkus;

import java.math.BigDecimal;
import java.util.Set;

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

import br.com.impacta.quarkus.Models.Investimento;
import br.com.impacta.quarkus.Models.Monto;

@Path("/investimentos")
public class InvestimentoResource {
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
    public Investimento addInvestimento(Investimento conta) {
        Investimento InvestimentoEntity = InvestimentoService.addInvestimento(conta);
        return InvestimentoEntity;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{idInvestimento}/credito")
    public Investimento Credito(@PathParam("idInvestimento") Integer idInvestimento, Monto valor) {
        try {
            Investimento InvestimentoEntity = new Investimento();
            InvestimentoEntity.setIdInvestimento(idInvestimento);
            InvestimentoEntity = InvestimentoService.getInvestimento(InvestimentoEntity);
            Investimento upInvestimentoEntity = InvestimentoService.Credito(InvestimentoEntity, valor.valor);
            return upInvestimentoEntity;
        } catch (Exception e) {
            throw new BadRequestException();

        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{idInvestimento}/debito")
    public Investimento Debito(@PathParam("idInvestimento") Integer idInvestimento, Monto valor) {
        try {
            Investimento InvestimentoEntity = new Investimento();
            InvestimentoEntity.setIdInvestimento(idInvestimento);
            InvestimentoEntity = InvestimentoService.getInvestimento(InvestimentoEntity);

            if (!(InvestimentoEntity.getSaldo().compareTo(valor.valor) >= 0)) {
                throw new BadRequestException();
            }
            Investimento upInvestimentoEntity = InvestimentoService.Debito(InvestimentoEntity, valor.valor);

            return upInvestimentoEntity;
        } catch (Exception e) {
            throw new BadRequestException();

        }
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{idInvestimento}")
    public Investimento deleteInvestimento(@PathParam("idInvestimento") Integer idInvestimento) {
        Investimento InvestimentoEntity = new Investimento();
        InvestimentoEntity.setIdInvestimento(idInvestimento);
        InvestimentoEntity = InvestimentoService.deleteInvestimento(InvestimentoEntity);
        return InvestimentoEntity;
    }

}