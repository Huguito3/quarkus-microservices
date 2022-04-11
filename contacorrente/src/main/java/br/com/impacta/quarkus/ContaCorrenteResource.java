package br.com.impacta.quarkus;

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

@Path("/contas")
public class ContaCorrenteResource {
    @Inject
    ContaCorrenteService contaCorrenteService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Set<ContaCorrente> listContaCorrente() {
        return contaCorrenteService.listContaCorrente();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{idConta}")
    public ContaCorrente getContaCorrente(@PathParam("idConta") Integer idConta) {
        ContaCorrente contaCorrenteEntity = new ContaCorrente();
        contaCorrenteEntity.setIdConta(idConta);
        contaCorrenteEntity = contaCorrenteService.getContaCorrente(contaCorrenteEntity);
        return contaCorrenteEntity;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ContaCorrente addContaCorrente(ContaCorrente conta) {
        ContaCorrente contaCorrenteEntity = contaCorrenteService.addContaCorrente(conta);
        return contaCorrenteEntity;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{idConta}/credito")
    public ContaCorrente Credito(@PathParam("idConta") Integer idConta, Monto valor) {
        try {
            ContaCorrente contaCorrenteEntity = new ContaCorrente();
            contaCorrenteEntity.setIdConta(idConta);
            contaCorrenteEntity = contaCorrenteService.getContaCorrente(contaCorrenteEntity);
            ContaCorrente upcontaCorrenteEntity = contaCorrenteService.Credito(contaCorrenteEntity, valor.valor);
            return upcontaCorrenteEntity;
        } catch (Exception e) {
            throw new BadRequestException();

        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
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
            throw new BadRequestException();

        }
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{idConta}")
    public ContaCorrente deleteContaCorrente(@PathParam("idConta") Integer idConta) {
        ContaCorrente contaCorrenteEntity = new ContaCorrente();
        contaCorrenteEntity.setIdConta(idConta);
        contaCorrenteEntity = contaCorrenteService.deleteContaCorrente(contaCorrenteEntity);
        return contaCorrenteEntity;
    }

}