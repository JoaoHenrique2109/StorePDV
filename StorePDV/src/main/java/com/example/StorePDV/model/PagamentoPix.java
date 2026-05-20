package com.example.StorePDV.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
@Entity
@Table(name = "pagamentos_pix")
public class PagamentoPix extends Pagamento{
    private String qrCode;
    private String chavePix;
    private LocalDateTime expiracao;

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getChavePix() {
        return chavePix;
    }

    public void setChavePix(String chavePix) {
        this.chavePix = chavePix;
    }

    public LocalDateTime getExpiracao() {
        return expiracao;
    }

    public void setExpiracao(LocalDateTime expiracao) {
        this.expiracao = expiracao;
    }
}
