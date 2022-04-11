package br.com.impacta.quarkus;

import java.math.BigDecimal;
import java.util.Iterator;
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

import org.eclipse.microprofile.rest.client.inject.RestClient;

import br.com.impacta.quarkus.Models.CartaoCredito;
import br.com.impacta.quarkus.Models.Monto;
import br.com.impacta.quarkus.Services.ContaCorrenteRestClient;

@Path("/cartaoCredito")
public class CartaoCreditoResource {


    @Inject
    @RestClient
    ContaCorrenteRestClient contaCorrenteRestClient;

    @Inject
    CartaoCreditoService cartaoCreditoervice;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Set<CartaoCredito> listCartaoCredito() {
        return cartaoCreditoervice.listCartaoCredito();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{idCartaoCredito}")
    public CartaoCredito getCartaoCredito(@PathParam("idCartaoCredito") Integer idCartaoCredito) {
        CartaoCredito CartaoCreditoEntity = new CartaoCredito();
        CartaoCreditoEntity.setIdCartaoCredito(idCartaoCredito);
        CartaoCreditoEntity = cartaoCreditoervice.getCartaoCredito(CartaoCreditoEntity);
        return CartaoCreditoEntity;
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("conta/id/{idConta}")
    public Monto getSaldoCartao(@PathParam("idConta") Integer idConta) {

        Set<CartaoCredito> listaTarjetas = cartaoCreditoervice.listCartaoCredito();

        Monto montoTotal = new Monto();
        montoTotal.valor = BigDecimal.ZERO;
        for (CartaoCredito tarjeta : listaTarjetas) {
            montoTotal.valor = montoTotal.valor.add(tarjeta.getSaldo());
        }
        return montoTotal;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CartaoCredito addCartaoCredito(CartaoCredito conta) {
        CartaoCredito CartaoCreditoEntity = cartaoCreditoervice.addCartaoCredito(conta);
        return CartaoCreditoEntity;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{idCartaoCredito}/credito")
    public CartaoCredito Credito(@PathParam("idCartaoCredito") Integer idCartaoCredito, Monto valor) {
        try {
            contaCorrenteRestClient.Debito(1, valor);
            CartaoCredito CartaoCreditoEntity = new CartaoCredito();
            CartaoCreditoEntity.setIdCartaoCredito(idCartaoCredito);
            CartaoCreditoEntity = cartaoCreditoervice.getCartaoCredito(CartaoCreditoEntity);
            CartaoCredito upCartaoCreditoEntity = cartaoCreditoervice.Credito(CartaoCreditoEntity, valor.valor);
            return upCartaoCreditoEntity;
        } catch (Exception e) {
            throw new BadRequestException();

        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{idCartaoCredito}/debito")
    public CartaoCredito Debito(@PathParam("idCartaoCredito") Integer idCartaoCredito, Monto valor) {
        try {
            CartaoCredito CartaoCreditoEntity = new CartaoCredito();
            CartaoCreditoEntity.setIdCartaoCredito(idCartaoCredito);
            CartaoCreditoEntity = cartaoCreditoervice.getCartaoCredito(CartaoCreditoEntity);

            if (!(CartaoCreditoEntity.getSaldo().compareTo(valor.valor) >= 0)) {
                throw new BadRequestException();
            }
            contaCorrenteRestClient.Credito(1, valor);
            CartaoCredito upCartaoCreditoEntity = cartaoCreditoervice.Debito(CartaoCreditoEntity, valor.valor);

            return upCartaoCreditoEntity;
        } catch (Exception e) {
            throw new BadRequestException();

        }
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{idCartaoCredito}")
    public CartaoCredito deleteCartaoCredito(@PathParam("idCartaoCredito") Integer idCartaoCredito) {
        CartaoCredito CartaoCreditoEntity = new CartaoCredito();
        CartaoCreditoEntity.setIdCartaoCredito(idCartaoCredito);
        CartaoCreditoEntity = cartaoCreditoervice.deleteCartaoCredito(CartaoCreditoEntity);
        return CartaoCreditoEntity;
    }

}