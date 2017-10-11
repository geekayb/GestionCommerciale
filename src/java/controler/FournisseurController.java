package controler;

import bean.Fournisseur;
import bean.Utilisateur;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import controler.util.SessionUtil;
import java.io.IOException;
import service.FournisseurFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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

@Named("fournisseurController")
@SessionScoped
public class FournisseurController implements Serializable {

    @EJB
    private service.FournisseurFacade ejbFacade;
    private List<Fournisseur> items = null;
    private Fournisseur selected;
    private String formeNull = null;
    private String paysNull = null;
    private String recherche;
    private List<Fournisseur> list = null;

    List<String> listeForme;
    List<String> listePays;
    Locale locale = Locale.ENGLISH;

    public void reinitialiser() {
        selected = null;
    }

    public void edite() {
        try {
            SessionUtil.redirect("/Gestion_commerciale/faces/fournisseur/Edit.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       public void redirectListe(){
        try {
            SessionUtil.redirect("/Gestion_commerciale/faces/fournisseur/List.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void rechercheByCritere() {
        Utilisateur utilisateur = SessionUtil.getConnectedUser();
        setList(getList());
    }

    public List<Fournisseur> getList() {

        Utilisateur utilisateur = SessionUtil.getConnectedUser();
        if (utilisateur != null) {
            if (recherche == null) {
                recherche = "";
            }
            if (getFacade().listFournisseurSociete(utilisateur, recherche) != null) {
                list = getFacade().listFournisseurSociete(utilisateur, recherche);
            } else {
                list = new ArrayList<>();
            }

        } else {

            list = getItems();
        }

        return list;
    }

    public void setList(List<Fournisseur> list) {
        this.list = list;
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

    public String getFormeNull() {
        return formeNull;
    }

    public void setFormeNull(String formeNull) {
        this.formeNull = formeNull;
    }

    public String getPaysNull() {
        return paysNull;
    }

    public void setPaysNull(String paysNull) {
        this.paysNull = paysNull;
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

    public void setListeForme(List<String> listeForme) {
        this.listeForme = listeForme;
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

    public void setListePays(List<String> listePays) {
        this.listePays = listePays;
    }

    public FournisseurController() {
    }

    public Fournisseur getSelected() {
        if (selected == null) {
            selected = new Fournisseur();
        }
        return selected;
    }

    public void setSelected(Fournisseur selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private FournisseurFacade getFacade() {
        return ejbFacade;
    }

    public Fournisseur prepareCreate() {
        selected = new Fournisseur();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("FournisseurCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("FournisseurUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("FournisseurDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Fournisseur> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    
        
    }
    
    
    public void saveFournisseurPopup() {
       
            if (SessionUtil.getConnectedUser() != null) {
                selected.setUtilisateur(SessionUtil.getConnectedUser());
                getFacade().create(selected);
                setList(getFacade().listFournisseurSociete(SessionUtil.getConnectedUser(), recherche));
                //setList(getFacade().findAll());
                selected = null;
                recherche = null;
                try {
                    SessionUtil.redirect("Create.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
                }
                JsfUtil.addSuccessMessage("fournisseur créée avec succès");
            }

        }


    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                
                if (persistAction == PersistAction.CREATE) {
                    
                    if (SessionUtil.getConnectedUser() != null) {
                        selected.setUtilisateur(SessionUtil.getConnectedUser());
                    }
                    getFacade().create(selected);
                    setList(getFacade().listFournisseurSociete(SessionUtil.getConnectedUser(), recherche));
                    selected = null;
                    recherche = null;
                    SessionUtil.redirect("List.xhtml");
                } else if (persistAction == PersistAction.UPDATE) {
                    getFacade().edit(selected);
                    selected = null;
                    recherche = null;
                    SessionUtil.redirect("List.xhtml");
                } else {
                    getFacade().remove(selected);
                    setList(getFacade().listFournisseurSociete(SessionUtil.getConnectedUser(), recherche));

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

    public Fournisseur getFournisseur(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Fournisseur> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Fournisseur> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Fournisseur.class)
    public static class FournisseurControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            FournisseurController controller = (FournisseurController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "fournisseurController");
            return controller.getFournisseur(getKey(value));
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
            if (object instanceof Fournisseur) {
                Fournisseur o = (Fournisseur) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Fournisseur.class.getName()});
                return null;
            }
        }

    }

}
