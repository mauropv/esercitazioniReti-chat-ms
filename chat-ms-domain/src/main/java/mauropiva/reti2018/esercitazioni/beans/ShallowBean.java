package mauropiva.reti2018.esercitazioni.beans;

public class ShallowBean {

    private String text;

    public ShallowBean(String text) {
        this.text = text;
    }

    public ShallowBean() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}