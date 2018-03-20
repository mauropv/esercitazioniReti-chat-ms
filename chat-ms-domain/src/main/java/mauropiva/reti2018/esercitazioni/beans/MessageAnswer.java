package mauropiva.reti2018.esercitazioni.beans;

public class MessageAnswer {

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
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

    //[{"src":"","text":"testo","time":timestamp}]
    private String src, text;
    private Long timestamp;


    public MessageAnswer(String src, String text, Long timestamp) {
        this.src = src;
        this.text = text;
        this.timestamp = timestamp;
    }

}