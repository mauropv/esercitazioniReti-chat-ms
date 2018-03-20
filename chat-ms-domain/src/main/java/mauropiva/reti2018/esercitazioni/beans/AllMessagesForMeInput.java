package mauropiva.reti2018.esercitazioni.beans;

public class AllMessagesForMeInput {

    //{nickname:xxxx}
    private String nickname;

    public AllMessagesForMeInput(String nickname) {

        this.nickname = nickname;
    }

    public AllMessagesForMeInput(){

    }

  public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}