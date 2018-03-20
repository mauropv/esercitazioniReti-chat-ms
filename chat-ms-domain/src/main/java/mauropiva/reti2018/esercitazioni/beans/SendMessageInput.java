package mauropiva.reti2018.esercitazioni.beans;

public class SendMessageInput {

    //{"nickname":xxxx,"dstNickname":xxxxx,"text":xxxx,"timestamp":long}


    private String nickname,dstnickname,text;
    private Long timestamp;

    public SendMessageInput(String nickname, String dstnickname, String text, Long timestamp) {

        this.nickname = nickname;
        this.dstnickname = dstnickname;
        this.text = text;
        this.timestamp = timestamp;
    }

    public SendMessageInput(){

    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDstnickname() {
        return dstnickname;
    }

    public void setDstnickname(String dstNickname) {
        this.dstnickname = dstNickname;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }


}