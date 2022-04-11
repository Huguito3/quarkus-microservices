package br.com.impacta.quarkus;

import javax.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class ContaCorrenteService {
    private Set<ContaCorrente> ContaCorrenteSet = new HashSet<ContaCorrente>(0);

    public ContaCorrente addContaCorrente(ContaCorrente ContaCorrente){
        if (ContaCorrenteSet.contains(ContaCorrente)){
            for (ContaCorrente ContaCorrenteEntity : ContaCorrenteSet) {
                if (ContaCorrenteEntity.equals(ContaCorrente)){
                    return ContaCorrenteEntity;
                }
            }
        }
        ContaCorrenteSet.add(ContaCorrente);
        return ContaCorrente;
    }

    public ContaCorrente getContaCorrente(ContaCorrente ContaCorrente){
        if (ContaCorrenteSet.contains(ContaCorrente)){
            for (ContaCorrente ContaCorrenteEntity : ContaCorrenteSet) {
                if (ContaCorrenteEntity.equals(ContaCorrente)){
                    return ContaCorrenteEntity;
                }
            }
        }
        return null;
    }

    public Set<ContaCorrente> listContaCorrente(){
        return ContaCorrenteSet;
    }

    public ContaCorrente deleteContaCorrente(ContaCorrente ContaCorrente){
        if (ContaCorrenteSet.contains(ContaCorrente)){
            ContaCorrenteSet.remove(ContaCorrente);
            return ContaCorrente;
        }
        return null;
    }

    

    public ContaCorrente Credito(ContaCorrente ContaCorrente, BigDecimal valor){
        if (ContaCorrenteSet.contains(ContaCorrente)){
            for (ContaCorrente ContaCorrenteEntity : ContaCorrenteSet) {
                if (ContaCorrenteEntity.equals(ContaCorrente)){
                    ContaCorrenteEntity.Credito(valor);
                    return ContaCorrenteEntity;
                }
            }
        }
        return null;
    }

    public ContaCorrente Debito(ContaCorrente ContaCorrente, BigDecimal valor){
        if (ContaCorrenteSet.contains(ContaCorrente)){
            for (ContaCorrente ContaCorrenteEntity : ContaCorrenteSet) {
                if (ContaCorrenteEntity.equals(ContaCorrente)){
                    ContaCorrenteEntity.Debito(valor);
                    return ContaCorrenteEntity;
                }
            }
        }
        return null;
    }

}