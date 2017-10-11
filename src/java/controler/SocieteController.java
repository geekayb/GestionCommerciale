package controler;

import bean.Societe;
import bean.Utilisateur;
import controler.util.GoogleMail;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import controler.util.ServerConfigUtil;
import controler.util.SessionUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import service.SocieteFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.mail.MessagingException;
import org.primefaces.event.FileUploadEvent;
import service.UtilisateurFacade;

@Named("societeController")
@SessionScoped
public class SocieteController implements Serializable {

    @EJB
    private service.SocieteFacade ejbFacade;
    private List<Societe> items = null;
    private List<Societe> allSociete;
    private String recherche = "";
    private String etat = "";
    private Societe selected;
    private String email = "";
    private String password = "";
    private String telephone = "";
    private String raisonsociale = "";
    private String text = "";
    private List<String> listeForme;
    private List<String> listePays;
    Locale locale = Locale.ENGLISH;
    private static final int BUFFER_SIZE = 6124;
    @EJB
    UtilisateurFacade utilisateurFacade;

    public void upload(FileUploadEvent event) {
        SessionUtil.getConnectedUser().getSociete().setLogo("logo" + SessionUtil.getConnectedUser().getSociete().getId() + ".png");
        ServerConfigUtil.upload(event.getFile(), ServerConfigUtil.getEntrePath(SessionUtil.getConnectedUser().getSociete(), true), SessionUtil.getConnectedUser().getSociete().getLogo());
        getFacade().edit(SessionUtil.getConnectedUser().getSociete());
        try {
            SessionUtil.redirect("Edit");
        } catch (IOException ex) {
            Logger.getLogger(SocieteController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public String findPath(){
        if(SessionUtil.getConnectedUser()!=null){
         return "/uploads/" + SessionUtil.getConnectedUser().getSociete().getLogo();
        }
        else return "";
    }
    public String logoItem(Societe item){
        if(item!=null && item.getLogo()!=null){
         return "/uploads/" + item.getLogo();
        }
        else return "";
    }

    public Boolean getBoolean() {
        if (!email.equals("") && !password.equals("") && !raisonsociale.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public void edite() {
        try {
            SessionUtil.redirect("/Gestion_commerciale/faces/societe/Edit.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getEmailSuperUser(){
        return getFacade().getEmailSuperUser();
    }
    public String getPasswordSuperUser(){
        return getFacade().getPasswordSuperUser();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public SocieteController() {
    }

    public String getRaisonsociale() {
        return raisonsociale;
    }

    public void setRaisonsociale(String raisonsociale) {
        this.raisonsociale = raisonsociale;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public List<Societe> getAllSociete() {
        Utilisateur u = SessionUtil.getConnectedUser();
        if (u != null) {
            allSociete = new ArrayList<>();
            allSociete = getFacade().listSociete(u, recherche, etat);
        } else {
            allSociete = getItems();
        }

        return allSociete;
    }

    public String getRecherche() {
        if (recherche == null) {
            recherche = "";
        }
        return recherche;
    }

    public void setRecherche(String recherche) {
        this.recherche = recherche;
    }

    public String getEtat() {
        if (etat == null) {
            etat = "";
        }
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void setAllSociete(List<Societe> allSociete) {
        this.allSociete = allSociete;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public List<Societe> findSocieteByEtat() {
        Utilisateur u = SessionUtil.getConnectedUser();
        setAllSociete(getFacade().findSocietesByEtat(etat, u));
        return allSociete;
    }

    public List<Societe> findSocieteByCritere() {
        Utilisateur u = SessionUtil.getConnectedUser();
        setAllSociete(getFacade().findSocieteByCritere(recherche, u));
        return allSociete;
    }

    public void inscription() {

        if (raisonsociale.equals("")) {
            JsfUtil.addErrorMessage("Veuillez entrer le nom de votre société");
        } else if (email.equals("")) {
            JsfUtil.addErrorMessage("Veuillez entrer votre email");
        } else if (!email.contains("@")) {
            JsfUtil.addErrorMessage("Veuillez entrer une adresse valide");
        } else if (telephone.equals("")) {
            JsfUtil.addErrorMessage("Veuillez entrer votre télephone");
        } else {
            getSelected().setRaisonSociale(raisonsociale);
            getSelected().setDroitExploitation(false);
            getFacade().create(selected);

            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setEmail(email);
            utilisateur.setTelephone(telephone);
            //utilisateur.setPassword(password);
            String password = (int) (Math.floor(Math.random() * (100000 - 1000) + 1000)) + "";
            utilisateur.setPassword(password);
            utilisateur.setProfil("Administrateur");

            utilisateur.setDateCreation(new Date());
            utilisateur.setGererCatalogue(Boolean.TRUE);
            utilisateur.setGererClients(Boolean.TRUE);
            utilisateur.setGererDonnees(Boolean.TRUE);
            utilisateur.setGererFournisseur(Boolean.TRUE);
            utilisateur.setGererUsers(Boolean.TRUE);
            utilisateur.setGererParametre(Boolean.TRUE);
            utilisateur.setImpression(Boolean.TRUE);
            utilisateur.setPayerFacture(Boolean.TRUE);
            System.out.println("haaaa societe " + selected.getId());
            Societe s = selected;
            utilisateur.setSociete(s);
            //getFacade().create(selected);
            utilisateurFacade.create(utilisateur);
            utilisateur.setSociete(selected);
            utilisateurFacade.edit(utilisateur);
            try {
                GoogleMail.Send(getFacade().getEmailSuperUser(), getFacade().getPasswordSuperUser(), "" + email, "" + email, "Ouverture de votre compte sur l'application de gestion commerciale pour votre société " + getSelected().getRaisonSociale(),
                        "Bienvenu\n\n" + "Nous vous remercions pour votre inscription et sommes heureux de vous compter parmi nos nouveaux utilisateurs. Vous trouverez ci-dessous un bref rappel de vos informations.\n\n\n"
                        + "Accès à votre compte: \n  \n" + "votre identifiant: " + email + "\n" + "votre mot de passe: " + password + "\n Vous pouvez changer votre mot de passe dés que vous vous connectez à l'application\n\n Bonne journée");
            } catch (MessagingException ex) {
                Logger.getLogger(GoogleMail.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                SessionUtil.redirect("registre.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(SocieteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public Societe clone(Societe societe) {
        Societe clone = new Societe();
        clone.setId(societe.getId());
        clone.setRaisonSociale(societe.getRaisonSociale());
        clone.setDroitExploitation(societe.getDroitExploitation());
        return clone;
    }

    public List<String> getListeForme() {

        if (listeForme == null) {
            listeForme = new ArrayList<>();
            listeForme.add("Société à responsabilité limitée(SARL)");
            listeForme.add("Société anonyme(SA)");
            listeForme.add("Société à commandite");
            listeForme.add("Société en nom collectf");
            listeForme.add("Personne physique");
            listeForme.add("M.");
            listeForme.add("Mme.");
            listeForme.add("Mlle.");
            listeForme.add("M. et Mme.");
            listeForme.add("M. ou Mme.");
            listeForme.add("Entrepreneur");
            listeForme.add("Autre");

        }
        return listeForme;
    }

    public List<String> getListePays() {
        if (listePays == null) {
            listePays = new ArrayList<>();
            for (Locale locale : locale.getAvailableLocales()) {
                if (locale.getAvailableLocales() != null) {
                    listePays.add((String) locale.getDisplayCountry() + "");
                }
            }
        }
        return listePays;
    }

    public Societe getSelected() {
        if (selected == null) {
            selected = new Societe();
            selected.setLogo("");
        }
        return selected;
    }

    public void setSelected(Societe selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private SocieteFacade getFacade() {
        return ejbFacade;
    }

    public Societe prepareCreate() {
        selected = new Societe();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("SocieteCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("SocieteUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("SocieteDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Societe> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Societe getSociete(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Societe> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Societe> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Societe.class)
    public static class SocieteControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SocieteController controller = (SocieteController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "societeController");
            return controller.getSociete(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Societe) {
                Societe o = (Societe) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Societe.class.getName()});
                return null;
            }
        }

    }

    public void popupa() {
        if (selected.getDroitExploitation() == false) {
            selected.setDroitExploitation(true);
            getFacade().edit(selected);
            JsfUtil.addSuccessMessage("Opération éffectuée avec succès !");
        } else if (selected.getDroitExploitation() == true) {
            selected.setDroitExploitation(false);
            getFacade().edit(selected);
            JsfUtil.addSuccessMessage("Opération éffectuée avec succès !");

        }

    }

    public String droit() {
        if (selected.getDroitExploitation() == Boolean.FALSE) {
            return "hors service";
        } else if (selected.getDroitExploitation() == Boolean.TRUE) {
            return "en service";
        } else {
            return "";
        }

    }

    public long getNumberFacture() {

        return getFacade().getNumberFacture(selected);

    }

    public long getNumberDevis() {

        return getFacade().getNumberDevis(selected);

    }

    public long getNumberLivraison() {

        return getFacade().getNumberLivraison(selected);

    }

    public long getNumberCommande() {

        return getFacade().getNumberCommande(selected);

    }

}
