package br.com.impacta.quarkus.Services;

import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import br.com.impacta.quarkus.Monto;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
@RegisterRestClient
@RegisterClientHeaders
public interface BuscaSaldoInvestimentosRestClient {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/saldo/conta/id/{idConta}")
    public Monto getSaldoInvestimentos(@PathParam("idConta") Integer idConta);

    // implementar metodos async
    // https://quarkus.io/guides/rest-client

}
