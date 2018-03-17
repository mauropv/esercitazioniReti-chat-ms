package mauropiva.reti2018.esercitazioni.beans;

public class AuthAnswer {

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    //{"statoDelSistema":"OK"o"KO","nonce":val}
    private String status, token;

    public AuthAnswer(String status, String token) {
        this.status = status; this.token = token;
    }

    public AuthAnswer(){

    }

}