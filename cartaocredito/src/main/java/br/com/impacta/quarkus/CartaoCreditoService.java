package br.com.impacta.quarkus;

import javax.enterprise.context.ApplicationScoped;

import br.com.impacta.quarkus.Models.CartaoCredito;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class CartaoCreditoService {
    private Set<CartaoCredito> CartaoCreditoSet = new HashSet<CartaoCredito>(0);

    public CartaoCredito addCartaoCredito(CartaoCredito CartaoCredito){
        if (CartaoCreditoSet.contains(CartaoCredito)){
            for (CartaoCredito CartaoCreditoEntity : CartaoCreditoSet) {
                if (CartaoCreditoEntity.equals(CartaoCredito)){
                    return CartaoCreditoEntity;
                }
            }
        }
        CartaoCreditoSet.add(CartaoCredito);
        return CartaoCredito;
    }

    public CartaoCredito getCartaoCredito(CartaoCredito CartaoCredito){
        if (CartaoCreditoSet.contains(CartaoCredito)){
            for (CartaoCredito CartaoCreditoEntity : CartaoCreditoSet) {
                if (CartaoCreditoEntity.equals(CartaoCredito)){
                    return CartaoCreditoEntity;
                }
            }
        }
        return null;
    }

    public Set<CartaoCredito> listCartaoCredito(){
        return CartaoCreditoSet;
    }

    public CartaoCredito deleteCartaoCredito(CartaoCredito CartaoCredito){
        if (CartaoCreditoSet.contains(CartaoCredito)){
            CartaoCreditoSet.remove(CartaoCredito);
            return CartaoCredito;
        }
        return null;
    }

    

    public CartaoCredito Credito(CartaoCredito CartaoCredito, BigDecimal valor){
        if (CartaoCreditoSet.contains(CartaoCredito)){
            for (CartaoCredito CartaoCreditoEntity : CartaoCreditoSet) {
                if (CartaoCreditoEntity.equals(CartaoCredito)){
                    CartaoCreditoEntity.Credito(valor);
                    return CartaoCreditoEntity;
                }
            }
        }
        return null;
    }

    public CartaoCredito Debito(CartaoCredito CartaoCredito, BigDecimal valor){
        if (CartaoCreditoSet.contains(CartaoCredito)){
            for (CartaoCredito CartaoCreditoEntity : CartaoCreditoSet) {
                if (CartaoCreditoEntity.equals(CartaoCredito)){
                    CartaoCreditoEntity.Debito(valor);
                    return CartaoCreditoEntity;
                }
            }
        }
        return null;
    }

}