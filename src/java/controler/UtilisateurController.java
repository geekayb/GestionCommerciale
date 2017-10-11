package controler;

import bean.Utilisateur;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import controler.util.SessionUtil;
import java.io.IOException;
import service.UtilisateurFacade;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import service.SocieteFacade;

@Named("utilisateurController")
@SessionScoped
public class UtilisateurController implements Serializable {

    @EJB
    private service.UtilisateurFacade ejbFacade;
    private List<Utilisateur> items = null;
    private Utilisateur selected;
    private List<Utilisateur> list = null;
    private String recherche;
    private Utilisateur userNull = null;
    private Boolean isUser;
    private Boolean exist = false;

    private String email;
    private String nouvelEmail;
    private String ancienPassword;
    private String newPassword;
    private String confirmePassword;
    private List<Utilisateur> listusers;
    @EJB
    SocieteFacade societeFacade;

    public UtilisateurController() {
    }

    public void isConnected() throws IOException {

        if (SessionUtil.getConnectedUser() == null) {
            SessionUtil.redirect("/Gestion_commerciale/faces/index");
        }
    }

    public void isConnectedRoot() {

        if (isRoot()) {

            try {
                SessionUtil.redirect("/Gestion_commerciale/faces/index");
            } catch (IOException ex) {
                Logger.getLogger(UtilisateurController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void editeRoot() {
        getFacade().edit(getConnectedUser());
        try {
            SessionUtil.redirect("backoffice.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(UtilisateurController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Boolean isRoot() {

        if (SessionUtil.getConnectedUser() != null) {
            if (SessionUtil.getConnectedUser().getProfil().equals("super utilisateur")) {
                return false;
            } else {
                return true;
            }
        }
        else  return true;
    }

    public void edite() {
        try {
            SessionUtil.redirect("/Gestion_commerciale/faces/utilisateur/Edit.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void redirectListe() {
        try {
            SessionUtil.redirect("/Gestion_commerciale/faces/utilisateur/List.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void profileSelected() {
        if (selected.getProfil() != null) {
            if (selected.getProfil().equals("Administrateur")) {
                setIsUser(false);

            } else if (selected.getProfil().equals("Utilisateur")) {
                setIsUser(true);

            }
        } else {
            setIsUser(false);

        }

    }

    public Boolean getIsUser() {
        System.out.println(isUser);

        return isUser;
    }

    public void setIsUser(Boolean isUser) {
        this.isUser = isUser;
    }

    public String getNouvelEmail() {
        if (nouvelEmail == null) {
            nouvelEmail = "";
        }
        return nouvelEmail;
    }

    public void setNouvelEmail(String nouvelEmail) {
        this.nouvelEmail = nouvelEmail;
    }

    public Boolean getExist() {
        exist = userExist();
        return exist;
    }

    public void setExist(Boolean exist) {
        this.exist = exist;
    }

    public String getAncienPassword() {
        if (ancienPassword == null) {
            ancienPassword = "";
        }
        return ancienPassword;
    }

    public void setAncienPassword(String ancienPassword) {
        this.ancienPassword = ancienPassword;
    }

    public String getNewPassword() {
        if (newPassword == null) {
            newPassword = "";
        }
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmePassword() {
        if (confirmePassword == null) {
            confirmePassword = "";
        }
        return confirmePassword;
    }

    public void setConfirmePassword(String confirmePassword) {
        this.confirmePassword = confirmePassword;
    }

    public List<Utilisateur> getListusers() {
        if (listusers == null) {
            listusers = new ArrayList<Utilisateur>();
        }
        return listusers;
    }

    public void setListusers(List<Utilisateur> listusers) {
        this.listusers = listusers;
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

    public String getEmail() {
        if (email == null) {
            email = "";
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Utilisateur getUserNull() {
        return userNull;
    }

    public void setUserNull(Utilisateur userNull) {
        this.userNull = userNull;
    }

    public void rechercheByCritere() {
        Utilisateur utilisateur = SessionUtil.getConnectedUser();
        setList(getList());
    }

    public List<Utilisateur> getList() {
      Utilisateur utilisateur = SessionUtil.getConnectedUser();
        if (utilisateur != null) {
            
            
                list = getFacade().listUsersSociete(utilisateur, recherche);
            

        } else {

                list = new ArrayList<>();
        }

        return list;
    }

    public void setList(List<Utilisateur> list) {
        this.list = list;
    }

    public Boolean userExist() {
        listusers = getFacade().userExiste(getEmail());
        if (listusers.isEmpty()) {
            setExist(Boolean.FALSE);
            return exist;
        } else {
            setExist(Boolean.TRUE);
            return exist;
        }
    }

    public Boolean getProfilUser() {
        Utilisateur user = SessionUtil.getConnectedUser();
        if (user == null) {
            return false;
        } else if (user.getProfil() != null) {
            if (user.getProfil().equals("Administrateur")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public String getDateSysteme() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        String currentTime = simpleDateFormat.format(new Date());

        return currentTime;
    }

    public Utilisateur getConnectedUser() {
        Utilisateur user = SessionUtil.getConnectedUser();
        if (user == null) {
            return new Utilisateur();
        }
        return user;
    }

    public Boolean infoComplete() {
        //System.out.println("haaa l adresse" + SessionUtil.getConnectedUser().getSociete().getAdresse());
        if(SessionUtil.getConnectedUser() != null){
        if (SessionUtil.getConnectedUser().getSociete() != null && SessionUtil.getConnectedUser().getSociete().getAdresse().equals("")) {
            return true;
        } else {
            
            return false;
        }
        }
        return false;

    }

    public void authentification() {
        System.out.println("LOG : " + selected.getEmail());
        System.out.println("PASSWORD : " + selected.getPassword());

        Utilisateur user = getFacade().authentification(selected.getEmail(), selected.getPassword());
        if (user == null) {
            JsfUtil.addErrorMessage("Login ou Password erroné");
            System.out.println("User Null");
        } else if (user.getSociete() == null) {
            SessionUtil.registerUser(user);
            try {
                SessionUtil.redirect("/Gestion_commerciale/faces/backoffice.xhtml");

            } catch (IOException ex) {

            }
        } else if (user.getSociete().getDroitExploitation() == false) {
            JsfUtil.addErrorMessage("Vous n'avez pas le droit de se connecter");
        } else {
            SessionUtil.registerUser(user);
            user.setDateDernierCnx(new Date());
            getFacade().edit(user);
            try {

//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//                String currentTime = simpleDateFormat.format(new Date());
//
//                System.out.println("haaaaaa date " + currentTime);
//
//                UserAction userAction = new UserAction();
//                try {
//                    userAction.setDate(simpleDateFormat.parse(currentTime));
//                } catch (ParseException ex) {
//                    Logger.getLogger(Utilisateur.class.getName()).log(Level.SEVERE, null, ex);
//                    userAction.setDate(new Date());
//                }
//                userAction.setNom("connexion");
//                userAction.setUser(user);
//
//                System.out.println("HA DATE LI GHAA " +userAction.getDate());
//                getUserActionFacade().edit(userAction);
                SessionUtil.redirect("/Gestion_commerciale/faces/dashboard.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(Utilisateur.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void deconnexion() {

        try {
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//            String currentTime = simpleDateFormat.format(new Date());
//
//            System.out.println("haaaaaa date " + currentTime);

//            UserAction userAction = new UserAction();
//            try {
//                userAction.setDate(simpleDateFormat.parse(currentTime));
//            } catch (ParseException ex) {
//                Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
//                userAction.setDate(new Date());
//
//            }
//            userAction.setNom("deconnexion");
//            userAction.setUser(SessionUtil.getConnectedUser());
//
//            getUserActionFacade().edit(userAction);
            //getConnectedUser().setDateDernierCnx(new Date());
            //getFacade().edit(selected);
            SessionUtil.deconnexion();

            SessionUtil.redirect("/Gestion_commerciale/faces/index.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(UtilisateurController.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("User not nulll");
        if (SessionUtil.getConnectedUser() != null) {
        }

    }

    public Utilisateur getSelected() {
        if (selected == null) {
            selected = new Utilisateur();
        }
        return selected;
    }

    public void setSelected(Utilisateur selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private UtilisateurFacade getFacade() {
        return ejbFacade;
    }

    public Utilisateur prepareCreate() {
        selected = new Utilisateur();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("UtilisateurCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("UtilisateurUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("UtilisateurDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Utilisateur> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public String getPasswordBymail() {
        return getFacade().getPasswordByMail(getSelected().getEmail());
    }

    public long getNumberAbonnee() {
        if (getConnectedUser() != null) {
            return getFacade().getNumberAbonnee();
        } else {
            return 0;
        }

    }

    public long getNumberAbonneeActive() {
        if (getConnectedUser() != null) {
            return getFacade().getNumberAbonneeActive();
        } else {
            return 0;
        }

    }

    public long getNumberAbonneeHorsService() {
        if (getConnectedUser() != null) {
            return getFacade().getNumberAbonneeHorsService();
        } else {
            return 0;
        }

    }

    public long getNumberFacture() {
        if (getConnectedUser() != null) {
            return getFacade().getNumberFacture();
        } else {
            return 0;
        }

    }

    public long getNumberDevis() {
        if (getConnectedUser() != null) {
            return getFacade().getNumberDevis();
        } else {
            return 0;
        }

    }

    public long getNumberLivraison() {
        if (getConnectedUser() != null) {
            return getFacade().getNumberLivraison();
        } else {
            return 0;
        }

    }

    public long getNumberCommande() {
        if (getConnectedUser() != null) {
            return getFacade().getNumberCommande();
        } else {
            return 0;
        }

    }

    public void updateSociete() {
        System.out.println("dkheeeel l update");
        if (getConnectedUser() != null) {
            if ((getConnectedUser().getSociete().getRaisonSociale().equals(""))) {
                JsfUtil.addErrorMessage("Raison sociale ne peut pas etre vide");
            } else if ((getConnectedUser().getSociete().getAdresse().equals(""))) {
                JsfUtil.addErrorMessage("adresse  ne peut pas etre vide");
            } else if ((getConnectedUser().getSociete().getVille().equals(""))) {
                JsfUtil.addErrorMessage("la ville ne peut pas etre vide");

            } else if ((getConnectedUser().getSociete().getCodePostale().equals(""))) {
                JsfUtil.addErrorMessage("Code postale ne peut pas etre vide");

            } else if ((getConnectedUser().getSociete().getPays().equals(""))) {
                JsfUtil.addErrorMessage("Le pays ne peut pas etre vide");

            } else if ((getConnectedUser().getSociete().getRC().equals(""))) {
                JsfUtil.addErrorMessage("Le RC ne peut pas etre vide");

            } else if ((getConnectedUser().getSociete().getICE().equals(""))) {
                JsfUtil.addErrorMessage("ICE ne peut pas etre vide");

            } else if ((getConnectedUser().getSociete().getLogo().equals(""))) {
                JsfUtil.addErrorMessage("Le logo est obligatoire");

            } else if ((getConnectedUser().getSociete().getNumeroIF().equals(""))) {
                JsfUtil.addErrorMessage("IF ne peut pas etre vide");

            } else if ((getConnectedUser().getSociete().getTP().equals(""))) {
                JsfUtil.addErrorMessage("TP ne peut pas etre vide");

            }  else if ((getConnectedUser().getSociete().getTelephone().equals(""))) {
                JsfUtil.addErrorMessage("Le télephone ne peut pas etre vide");

            } else if ((getConnectedUser().getSociete().getFax().equals(""))) {
                JsfUtil.addErrorMessage("Le Fax est obligatoire");

            } else if ((getConnectedUser().getSociete().getEmail().equals(""))) {
                JsfUtil.addErrorMessage("L'émail ne peut pas etre vide");

            } else {
                societeFacade.edit(getConnectedUser().getSociete());
                try {
                    SessionUtil.redirect("/Gestion_commerciale/faces/dashboard.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(UtilisateurController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            //------------------------------------------------------
//            if ((getConnectedUser().getSociete().getRaisonSociale().equals("")) && (getConnectedUser().getSociete().getAdresse().equals("")) && (getConnectedUser().getSociete().getCodePostale().equals("")) && (getConnectedUser().getSociete().getPays().equals(""))
//                    && (getConnectedUser().getSociete().getVille().equals("")) && (getConnectedUser().getSociete().getRC().equals("")) && (getConnectedUser().getSociete().getICE().equals(""))
//                    && (getConnectedUser().getSociete().getLogo().equals("")) && (getConnectedUser().getSociete().getNumeroIF().equals("")) && (getConnectedUser().getSociete().getTP().equals("")) && (getConnectedUser().getSociete().getPatente().equals("")) && (getConnectedUser().getSociete().getTelephone().equals(""))
//                    && (getConnectedUser().getSociete().getFax().equals("")) && (getConnectedUser().getSociete().getEmail().equals(""))) {
//                JsfUtil.addErrorMessage("Raison sociale, adresse, code postale, pays, ville, logo, IF, ICE, TP, Patente, Telephone, Fax, Email ne peuvent pas étre vide, merci de les enter");
//            } else if ((getConnectedUser().getSociete().getRaisonSociale().equals("")) && (getConnectedUser().getSociete().getAdresse().equals("")) && (getConnectedUser().getSociete().getCodePostale().equals("")) && (getConnectedUser().getSociete().getPays().equals(""))
//                    && (getConnectedUser().getSociete().getVille().equals("")) && (getConnectedUser().getSociete().getRC().equals("")) && (getConnectedUser().getSociete().getICE().equals(""))
//                    && (getConnectedUser().getSociete().getLogo().equals("")) && (getConnectedUser().getSociete().getNumeroIF().equals("")) && (getConnectedUser().getSociete().getTP().equals("")) && (getConnectedUser().getSociete().getPatente().equals("")) && (getConnectedUser().getSociete().getTelephone().equals(""))
//                    && (getConnectedUser().getSociete().getFax().equals(""))) {
//                JsfUtil.addErrorMessage("Raison sociale, adresse, code postale, pays, ville, logo, IF, RC, ICE, TP, Patente, Telephone, Fax ne peuvent pas étre vide, merci de les enter");
//
//            } else if ((getConnectedUser().getSociete().getRaisonSociale().equals("")) && (getConnectedUser().getSociete().getAdresse().equals("")) && (getConnectedUser().getSociete().getCodePostale().equals("")) && (getConnectedUser().getSociete().getPays().equals(""))
//                    && (getConnectedUser().getSociete().getVille().equals("")) && (getConnectedUser().getSociete().getRC().equals("")) && (getConnectedUser().getSociete().getICE().equals(""))
//                    && (getConnectedUser().getSociete().getLogo().equals("")) && (getConnectedUser().getSociete().getNumeroIF().equals("")) && (getConnectedUser().getSociete().getTP().equals("")) && (getConnectedUser().getSociete().getPatente().equals("")) && (getConnectedUser().getSociete().getTelephone().equals(""))) {
//                JsfUtil.addErrorMessage("Raison sociale, adresse, code postale, pays, ville, logo, IF, RC, ICE, TP, Patente, Telephone ne peuvent pas étre vide, merci de les enter");
//
//            } else if ((getConnectedUser().getSociete().getRaisonSociale().equals("")) && (getConnectedUser().getSociete().getAdresse().equals("")) && (getConnectedUser().getSociete().getCodePostale().equals("")) && (getConnectedUser().getSociete().getPays().equals(""))
//                    && (getConnectedUser().getSociete().getVille().equals("")) && (getConnectedUser().getSociete().getRC().equals("")) && (getConnectedUser().getSociete().getICE().equals(""))
//                    && (getConnectedUser().getSociete().getLogo().equals("")) && (getConnectedUser().getSociete().getNumeroIF().equals("")) && (getConnectedUser().getSociete().getTP().equals("")) && (getConnectedUser().getSociete().getPatente().equals(""))) {
//                JsfUtil.addErrorMessage("Raison sociale, adresse, code postale, pays, ville, logo, IF, ICE, TP, Patente ne peuvent pas étre vide, merci de les enter");
//
//            } else if ((getConnectedUser().getSociete().getRaisonSociale().equals("")) && (getConnectedUser().getSociete().getAdresse().equals("")) && (getConnectedUser().getSociete().getCodePostale().equals("")) && (getConnectedUser().getSociete().getPays().equals(""))
//                    && (getConnectedUser().getSociete().getVille().equals("")) && (getConnectedUser().getSociete().getRC().equals("")) && (getConnectedUser().getSociete().getICE().equals(""))
//                    && (getConnectedUser().getSociete().getLogo().equals("")) && (getConnectedUser().getSociete().getNumeroIF().equals("")) && (getConnectedUser().getSociete().getTP().equals(""))) {
//                JsfUtil.addErrorMessage("Raison sociale, adresse, code postale, pays, ville, logo, IF, RC, ICE, TP ne peuvent pas étre vide, merci de les enter");
//
//            } else if ((getConnectedUser().getSociete().getRaisonSociale().equals("")) && (getConnectedUser().getSociete().getAdresse().equals("")) && (getConnectedUser().getSociete().getCodePostale().equals("")) && (getConnectedUser().getSociete().getPays().equals(""))
//                    && (getConnectedUser().getSociete().getVille().equals("")) && (getConnectedUser().getSociete().getRC().equals("")) && (getConnectedUser().getSociete().getICE().equals(""))
//                    && (getConnectedUser().getSociete().getLogo().equals("")) && (getConnectedUser().getSociete().getNumeroIF().equals(""))) {
//                JsfUtil.addErrorMessage("Raison sociale, adresse, code postale, pays, ville, logo, IF, RC, ICE ne peuvent pas étre vide, merci de les enter");
//            } else if ((getConnectedUser().getSociete().getRaisonSociale().equals("")) && (getConnectedUser().getSociete().getAdresse().equals("")) && (getConnectedUser().getSociete().getCodePostale().equals("")) && (getConnectedUser().getSociete().getPays().equals(""))
//                    && (getConnectedUser().getSociete().getVille().equals("")) && (getConnectedUser().getSociete().getRC().equals("")) && (getConnectedUser().getSociete().getICE().equals(""))
//                    && (getConnectedUser().getSociete().getLogo().equals(""))) {
//                JsfUtil.addErrorMessage("Raison sociale, adresse, code postale, pays, ville, logo, ICE , RC, ne peuvent pas étre vide, merci de les enter");
//            } else if ((getConnectedUser().getSociete().getRaisonSociale().equals("")) && (getConnectedUser().getSociete().getAdresse().equals("")) && (getConnectedUser().getSociete().getCodePostale().equals("")) && (getConnectedUser().getSociete().getPays().equals(""))
//                    && (getConnectedUser().getSociete().getVille().equals("")) && (getConnectedUser().getSociete().getRC().equals("")) && (getConnectedUser().getSociete().getICE().equals(""))) {
//                JsfUtil.addErrorMessage("Raison sociale, adresse, code postale, pays, ville, ICE, RC ne peuvent pas étre vide, merci de les enter");
//            } else if ((getConnectedUser().getSociete().getRaisonSociale().equals("")) && (getConnectedUser().getSociete().getAdresse().equals("")) && (getConnectedUser().getSociete().getCodePostale().equals("")) && (getConnectedUser().getSociete().getPays().equals(""))
//                    && (getConnectedUser().getSociete().getVille().equals("")) && (getConnectedUser().getSociete().getRC().equals(""))) {
//                JsfUtil.addErrorMessage("Raison sociale, adresse, code postale, pays, ville , RC ne peuvent pas étre vide, merci de les enter");
//            } else if ((getConnectedUser().getSociete().getRaisonSociale().equals("")) && (getConnectedUser().getSociete().getAdresse().equals("")) && (getConnectedUser().getSociete().getCodePostale().equals("")) && (getConnectedUser().getSociete().getPays().equals(""))
//                    && (getConnectedUser().getSociete().getVille().equals(""))) {
//                JsfUtil.addErrorMessage("Raison sociale, adresse, code postale, pays, ville ne peuvent pas étre vide, merci de les enter");
//            } else if ((getConnectedUser().getSociete().getRaisonSociale().equals("")) && (getConnectedUser().getSociete().getAdresse().equals("")) && (getConnectedUser().getSociete().getCodePostale().equals("")) && (getConnectedUser().getSociete().getPays().equals(""))) {
//                JsfUtil.addErrorMessage("Raison sociale, adresse, code postale, pays ne peuvent pas étre vide, merci de les enter");
//            } else if ((getConnectedUser().getSociete().getRaisonSociale().equals("")) && (getConnectedUser().getSociete().getAdresse().equals("")) && (getConnectedUser().getSociete().getCodePostale().equals(""))) {
//                JsfUtil.addErrorMessage("Raison sociale, adresse, code postale ne peuvent pas étre vide, merci de les enter");
//            } else if ((getConnectedUser().getSociete().getRaisonSociale().equals("")) && (getConnectedUser().getSociete().getAdresse().equals(""))) {
//                JsfUtil.addErrorMessage("Raison sociale, adresse ne peuvent pas étre vide, merci de les enter");
//
//            } else if ((getConnectedUser().getSociete().getRaisonSociale().equals("")) || (getConnectedUser().getSociete().getAdresse().equals("")) || (getConnectedUser().getSociete().getCodePostale().equals("")) || (getConnectedUser().getSociete().getPays().equals(""))
//                    || (getConnectedUser().getSociete().getVille().equals("")) || (getConnectedUser().getSociete().getRC().equals("")) || (getConnectedUser().getSociete().getICE().equals(""))
//                    || (getConnectedUser().getSociete().getLogo().equals("")) || (getConnectedUser().getSociete().getNumeroIF().equals("")) || (getConnectedUser().getSociete().getTP().equals("")) || (getConnectedUser().getSociete().getPatente().equals("")) || (getConnectedUser().getSociete().getTelephone().equals(""))
//                    || (getConnectedUser().getSociete().getFax().equals("")) || (getConnectedUser().getSociete().getEmail().equals(""))) {
//                JsfUtil.addErrorMessage("Veuillez remplir tout les champs obligatoire");
//            } else {
//
//            }
        }
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction == PersistAction.CREATE) {
//                   
                    if (SessionUtil.getConnectedUser() != null) {
                        if (getSelected().getProfil() == null) {
                            JsfUtil.addErrorMessage("Veuillez choisir un profile pour cet utilisateur");
                        } else if (selected.getProfil().equals("Administrateur")) {
                            selected.setGererCatalogue(Boolean.TRUE);
                            selected.setGererClients(Boolean.TRUE);
                            selected.setGererDonnees(Boolean.TRUE);
                            selected.setGererFournisseur(Boolean.TRUE);
                            selected.setGererUsers(Boolean.TRUE);
                            selected.setImpression(Boolean.TRUE);
                            selected.setGererParametre(Boolean.TRUE);
                            selected.setPayerFacture(Boolean.TRUE);

                        }
                        if (selected.getNom().equals("")) {
                            JsfUtil.addErrorMessage("Veuillez entrer le nom de l'utilisateur");
                        }
                        if (selected.getPrenom().equals("")) {
                            JsfUtil.addErrorMessage("Veuillez entrer le prénom de l'utilisateur");
                        }
                        if (getEmail().equals("")) {
                            JsfUtil.addErrorMessage("Veuillez entrer l'email de l'utilisateur");

                        } else if (getExist() == false) {
                            selected.setEmail(getEmail());
                            if (selected.getPassword().equals("")) {
                                JsfUtil.addErrorMessage("Mot de passe obligatoire");
                            } else {
                                getSelected().setSociete(SessionUtil.getConnectedUser().getSociete());
                                getSelected().setDateCreation(new Date());
                                getSelected().setDateDernierCnx(new Date());
                                getFacade().create(selected);
                                selected = null;
                                email = null;
                                SessionUtil.redirect("List.xhtml");
                            }
                        } else if (getExist() == true) {
                            JsfUtil.addErrorMessage("Cette adresse email est déjà utilisée par un autre utilisateur, veuillez en saisir une autre");
                        }

                    }

                } else if (persistAction == PersistAction.UPDATE) {

//                    System.out.println("haaa l email jdid" + selected.getEmail());
//                    System.out.println("haa l confirm" + getConfirmePassword());
//                    System.out.println("haaa l ancien" + getAncienPassword());
//                    System.out.println("haaal passworf" + getNewPassword());
                    System.out.println(selected.getEmail());
                    if (getAncienPassword().equals("") && getNewPassword().equals("") && getConfirmePassword().equals("") && (!getNouvelEmail().equals(""))) {
                        selected.setEmail(getNouvelEmail());
                        getFacade().edit(selected);
                        nouvelEmail = null;
                        SessionUtil.redirect("List.xhtml");
                    } else if (getAncienPassword().equals("") && getNewPassword().equals("") && getConfirmePassword().equals("")) {
                        getFacade().edit(selected);
                        SessionUtil.redirect("List.xhtml");
//                    } else if (getAncienPassword().equals("") && getNewPassword().equals("") && getConfirmePassword().equals("") && (!getNouvelEmail().equals(""))) {
//                        selected.setEmail(getNouvelEmail());
//                        getFacade().edit(selected);
//                        SessionUtil.redirect("List.xhtml");
                    } else if (!getAncienPassword().equals("") && getNewPassword().equals("") && getConfirmePassword().equals("")) {
                        JsfUtil.addErrorMessage("Veuillez entrer le nouveau  mot de passe");
                        JsfUtil.addErrorMessage("Veuillez confirmer votre mot de passe ");
                    } else if (!getAncienPassword().equals("") && !getNewPassword().equals("") && getConfirmePassword().equals("")) {
                        JsfUtil.addErrorMessage("Veuillez confirmer votre nouveau mot de passe");

                    } else if (getAncienPassword().equals("") && getNewPassword().equals("") && !getConfirmePassword().equals("")) {
                        JsfUtil.addErrorMessage("Veuillez entrer l'ancien et le nouveau  mot de passe");

                    } else if (getAncienPassword().equals("") && !getNewPassword().equals("") && getConfirmePassword().equals("")) {
                        JsfUtil.addErrorMessage("Veuillez entrer l'ancien  mot de passe, et confirmez le nouveau");

                    } else if (getAncienPassword().equals("") && !getNewPassword().equals("") && !getConfirmePassword().equals("")) {
                        JsfUtil.addErrorMessage("Veuillez entrer l'ancien mot de passe ");
                    } else if (!getAncienPassword().equals("") && !getNewPassword().equals("") && !getConfirmePassword().equals("")) {
                        if (!getAncienPassword().equals(getPasswordBymail())) {
                            JsfUtil.addErrorMessage("Mot de passe incorrect !");
                        } else if (!getConfirmePassword().equals(getNewPassword())) {
                            JsfUtil.addErrorMessage("Confirmation echouée");

                        } else {
                            selected.setPassword(getNewPassword());
                            if (!getNouvelEmail().equals("")) {
                                selected.setEmail(getNouvelEmail());
                            }
                            getFacade().edit(selected);
                            setNouvelEmail("");
                            setNewPassword("");
                            setAncienPassword("");
                            setConfirmePassword("");
                            SessionUtil.redirect("List.xhtml");
                        }
                    }

                } else {
                    getFacade().remove(selected);

                }

                //JsfUtil.addSuccessMessage(successMessage);
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

    public Utilisateur getUtilisateur(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Utilisateur> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Utilisateur> getItemsAvailableSelectOne() {
        return getFacade().findAll();

    }

    @FacesConverter(forClass = Utilisateur.class)
    public static class UtilisateurControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UtilisateurController controller = (UtilisateurController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "utilisateurController");
            return controller.getUtilisateur(getKey(value));
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
            if (object instanceof Utilisateur) {
                Utilisateur o = (Utilisateur) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Utilisateur.class.getName()});
                return null;
            }
        }

    }

}
