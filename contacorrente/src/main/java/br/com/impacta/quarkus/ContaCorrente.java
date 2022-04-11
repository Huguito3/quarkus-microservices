package br.com.impacta.quarkus;

import java.math.BigDecimal;

public class ContaCorrente {

    private Integer idConta;
    private Long agencia;
    private Long conta;
    private String nome;
    private BigDecimal saldo;

    public Integer getIdConta() {
        return idConta;
    }

    public void setIdConta(Integer idConta) {
        this.idConta = idConta;
    }
    public Long getAgencia() {
        return agencia;
    }

    public void setAgencia(Long agencia) {
        this.agencia = agencia;
    }
    public Long getConta() {
        return conta;
    }

    public void setConta(Long conta) {
        this.conta = conta;
    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
        result = prime * result + ((idConta == null) ? 0 : idConta.hashCode());
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
        ContaCorrente other = (ContaCorrente) obj;
        if (idConta == null) {
            if (other.idConta != null)
                return false;
        } else if (!idConta.equals(other.idConta))
            return false;
        return true;
    }
}