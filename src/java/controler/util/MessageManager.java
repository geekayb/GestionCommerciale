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
public class MessageManager {

   Message message;

    public static Message createInfoMessage(int resultat, String text) {
        return new Message(resultat, text, FacesMessage.SEVERITY_INFO);
    }

    public static Message createErrorMessage(int resultat, String text) {
        return new Message(resultat, text, FacesMessage.SEVERITY_ERROR);
    }

    public static Message createWarnMessage(int resultat, String text) {
        return new Message(resultat, text, FacesMessage.SEVERITY_WARN);
    }

    public MessageManager() {
    }

   
    public static void showMessage(Message message) {
        if (message != null) {
            if (message.getSeverity() == FacesMessage.SEVERITY_ERROR) {
                JsfUtil.addErrorMessage(message.getText());
            } else if (message.getSeverity() == FacesMessage.SEVERITY_WARN) {
                JsfUtil.addWrningMessage(message.getText());
            } else if (message.getSeverity() == FacesMessage.SEVERITY_INFO) {
                if (message.getText() != null && !message.getText().equals("")) {
                    JsfUtil.addSuccessMessage(message.getText());
                }
            }
        }
    }

   

   

}
