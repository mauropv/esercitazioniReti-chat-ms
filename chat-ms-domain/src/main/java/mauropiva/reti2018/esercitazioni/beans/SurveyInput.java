package mauropiva.reti2018.esercitazioni.beans;

public class SurveyInput {

    //
    //choosen: getChoosen(),
    //userId: userId
    private String choosen, userid;

    public String getChoosen() {
        return choosen;
    }

    public void setChoosen(String choosen) {
        this.choosen = choosen;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public SurveyInput(String choosen, String userid) {

        this.choosen = choosen;
        this.userid = userid;
    }

    public SurveyInput(){

    }


}