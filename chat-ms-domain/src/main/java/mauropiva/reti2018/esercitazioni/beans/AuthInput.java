package mauropiva.reti2018.esercitazioni.beans;

public class AuthInput {

    //{matricola:xxxx, nickname:xxxx, password:xxxx,nonce:xxxx}}
    private String matricola,nickname,password,nonce;

    public AuthInput(String matricola, String nickname, String password, String nonce) {
        this.matricola = matricola;
        this.nickname = nickname;
        this.password = password;
        this.nonce = nonce;
    }

    public AuthInput(){

    }

    public String getMatricola() {
        return matricola;
    }

    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }
}