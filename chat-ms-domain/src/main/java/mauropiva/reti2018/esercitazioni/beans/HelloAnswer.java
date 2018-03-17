package mauropiva.reti2018.esercitazioni.beans;

public class HelloAnswer {

    //{"statoDelSistema":"OK"o"KO","nonce":val}
    private String statoDelSistema;

    public String getStatoDelSistema() {
        return statoDelSistema;
    }

    public void setStatoDelSistema(String statoDelSistema) {
        this.statoDelSistema = statoDelSistema;
    }

    public Long getNonce() {
        return nonce;
    }

    public void setNonce(Long nonce) {
        this.nonce = nonce;
    }

    private Long nonce;
    public HelloAnswer(String status, Long nonce) {
        this.statoDelSistema = status; this.nonce = nonce;
    }

}