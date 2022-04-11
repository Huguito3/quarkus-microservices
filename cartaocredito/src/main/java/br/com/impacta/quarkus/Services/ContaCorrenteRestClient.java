package br.com.impacta.quarkus.Services;

import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import br.com.impacta.quarkus.Models.*;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
@RegisterRestClient
@RegisterClientHeaders
public interface ContaCorrenteRestClient {

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{idConta}/debito")
    public ContaCorrente Debito(@PathParam("idConta") Integer idConta, Monto valor);

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{idConta}/credito")
    public ContaCorrente Credito(@PathParam("idConta") Integer idConta, Monto valor);

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{idConta}")
    public ContaCorrente getContaCorrente(@PathParam("idConta") Integer idConta);

}
