package br.com.impacta.quarkus.Models;

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

}