    package com.example.StorePDV.model;


    import jakarta.persistence.*;
    import java.time.LocalDateTime;

    @Entity
    @Table(name = "sessoes")

    public class Sessao {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private LocalDateTime dataUltimaatividade;
        private LocalDateTime dataLogin;
        private boolean status;
        private String ip;

        @ManyToOne
        @JoinColumn(name = "usuario_id")
        private Usuario usuario;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public LocalDateTime getDataUltimaatividade() {
            return dataUltimaatividade;
        }

        public void setDataUltimaatividade(LocalDateTime dataUltimaatividade) {
            this.dataUltimaatividade = dataUltimaatividade;
        }

        public LocalDateTime getDataLogin() {
            return dataLogin;
        }

        public void setDataLogin(LocalDateTime dataLogin) {
            this.dataLogin = dataLogin;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public Usuario getUsuario() {
            return usuario;
        }

        public void setUsuario(Usuario usuario) {
            this.usuario = usuario;
        }
    }
