/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controler.util;

import javax.faces.application.FacesMessage;

/**
 *
 * @author moulaYounes
 */
public class Message {

    private int resultat;
    private String text;
    private FacesMessage.Severity severity;

    public Message() {
    }

    public Message(int resultat, String text, FacesMessage.Severity severity) {
        this.resultat = resultat;
        this.text = text;
        this.severity = severity;
    }

    public Message(int resultat) {
        this.resultat = resultat;
    }

    public int getResultat() {
        return resultat;
    }

    public void setResultat(int resultat) {
        this.resultat = resultat;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public FacesMessage.Severity getSeverity() {
        return severity;
    }

    public void setSeverity(FacesMessage.Severity severity) {
        this.severity = severity;
    }

    @Override
    public String toString() {
        return text;
    }

    
}
