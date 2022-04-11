package br.com.impacta.quarkus;

import javax.enterprise.context.ApplicationScoped;

import br.com.impacta.quarkus.Models.Investimento;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class InvestimentoService {
    private Set<Investimento> InvestimentoSet = new HashSet<Investimento>(0);

    public Investimento addInvestimento(Investimento Investimento){
        if (InvestimentoSet.contains(Investimento)){
            for (Investimento InvestimentoEntity : InvestimentoSet) {
                if (InvestimentoEntity.equals(Investimento)){
                    return InvestimentoEntity;
                }
            }
        }
        InvestimentoSet.add(Investimento);
        return Investimento;
    }

    public Investimento getInvestimento(Investimento Investimento){
        if (InvestimentoSet.contains(Investimento)){
            for (Investimento InvestimentoEntity : InvestimentoSet) {
                if (InvestimentoEntity.equals(Investimento)){
                    return InvestimentoEntity;
                }
            }
        }
        return null;
    }

    public Set<Investimento> listInvestimento(){
        return InvestimentoSet;
    }

    public Investimento deleteInvestimento(Investimento Investimento){
        if (InvestimentoSet.contains(Investimento)){
            InvestimentoSet.remove(Investimento);
            return Investimento;
        }
        return null;
    }

    

    public Investimento Aplicacao(Investimento Investimento, BigDecimal valor){
        if (InvestimentoSet.contains(Investimento)){
            for (Investimento InvestimentoEntity : InvestimentoSet) {
                if (InvestimentoEntity.equals(Investimento)){
                    InvestimentoEntity.Credito(valor);
                    return InvestimentoEntity;
                }
            }
        }
        return null;
    }

    public Investimento Resgate(Investimento Investimento, BigDecimal valor){
        if (InvestimentoSet.contains(Investimento)){
            for (Investimento InvestimentoEntity : InvestimentoSet) {
                if (InvestimentoEntity.equals(Investimento)){
                    InvestimentoEntity.Debito(valor);
                    return InvestimentoEntity;
                }
            }
        }
        return null;
    }

}