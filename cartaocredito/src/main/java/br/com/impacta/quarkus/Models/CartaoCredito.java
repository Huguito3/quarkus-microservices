package br.com.impacta.quarkus.Models;

import java.math.BigDecimal;

public class CartaoCredito {

    private Integer idCartaoCredito;
    private String PAM;
    private Integer idConta;
    public BigDecimal saldo;
    public BigDecimal limite;

    

    public Integer getIdConta() {
        return idConta;
    }

    public void setIdConta(Integer idConta) {
        this.idConta = idConta;
    }

    public String getPAM() {
        return PAM;
    }

    public void setPAM(String PAM) {
        this.PAM = PAM;
    }

    public Integer getIdCartaoCredito() {
        return idCartaoCredito;
    }

    public void setIdCartaoCredito(Integer inves) {
        this.idCartaoCredito = inves;
    }
   
    public BigDecimal getSaldo() {
        return saldo;
    }
    public BigDecimal getLimite() {
        return limite;
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
        result = prime * result + ((idCartaoCredito == null) ? 0 : idCartaoCredito.hashCode());
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
        CartaoCredito other = (CartaoCredito) obj;
        if (idCartaoCredito == null) {
            if (other.idCartaoCredito != null)
                return false;
        } else if (!idCartaoCredito.equals(other.idCartaoCredito))
            return false;
        return true;
    }
}