package br.com.impacta.quarkus.Models;

import java.math.BigDecimal;

public class Investimento {

    private Integer idInvestimento;
    private String nomeInvestimento;
    private Integer idConta;
    public BigDecimal saldo;

    // public Investimento(Integer idInvestimento, String nomeInvestimento, Integer idConta, BigDecimal saldo) {
    //     this.idInvestimento = idInvestimento;
    //     this.idConta = idConta;
    //     this.nomeInvestimento = nomeInvestimento;
    //     this.saldo = saldo;
    // }

    public Integer getIdConta() {
        return idConta;
    }

    public void setIdConta(Integer idConta) {
        this.idConta = idConta;
    }

    public String getNomeInvestimento() {
        return nomeInvestimento;
    }

    public void setNomeInvestimento(String nomeInvestimento) {
        this.nomeInvestimento = nomeInvestimento;
    }

    public Integer getIdInvestimento() {
        return idInvestimento;
    }

    public void setIdInvestimento(Integer inves) {
        this.idInvestimento = inves;
    }
   
    public BigDecimal getSaldo() {
        return saldo;
    }

    public void Credito(BigDecimal monto) {
        saldo = saldo.add(monto);
    }

    public void Debito(BigDecimal monto) {
        saldo = saldo.subtract(monto);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idInvestimento == null) ? 0 : idInvestimento.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Investimento other = (Investimento) obj;
        if (idInvestimento == null) {
            if (other.idInvestimento != null)
                return false;
        } else if (!idInvestimento.equals(other.idInvestimento))
            return false;
        return true;
    }
}