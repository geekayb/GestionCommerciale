package controler;

import bean.Client;
import bean.Utilisateur;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import controler.util.SessionUtil;
import java.io.IOException;
import service.ClientFacade;

import java.io.Serializable;
import java.util.ArrayList;
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

@Named("clientController")
@SessionScoped
public class ClientController implements Serializable {

    @EJB
    private service.ClientFacade ejbFacade;
    private List<Client> items = null;
    private Client selected;
    private String recherche = "";
    private List<Client> list;

    public void rechercheByCritere() {
        Utilisateur utilisateur = SessionUtil.getConnectedUser();
        setList(getList());
    }

    public void edite() {
        try {
            SessionUtil.redirect("/Gestion_commerciale/faces/client/Edit.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void redirectListe() {
        try {
            SessionUtil.redirect("/Gestion_commerciale/faces/client/List.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void reinitialiser() {
        selected = null;
    }

    public ClientController() {
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

    public List<Client> getList() {
      Utilisateur utilisateur = SessionUtil.getConnectedUser();
      if(recherche==null) recherche="";
        if (utilisateur != null) {
            
            
                list = getFacade().listClientSociete(utilisateur, recherche);
            

        } else {

                list = new ArrayList<>();
        }

        return list;
    }

    public void setList(List<Client> list) {
        this.list = list;
    }

    public Client getSelected() {
        if (selected == null) {
            selected = new Client();
        }
        return selected;
    }

    public void setSelected(Client selected) {

        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ClientFacade getFacade() {
        return ejbFacade;
    }

    public Client prepareCreate() {
        selected = new Client();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ClientCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ClientUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ClientDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Client> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public void saveClientPopup() {
        if (selected.getRaisonSociale().equals("")) {
            JsfUtil.addErrorMessage("La raison sociale ne peut pas etre vide");
        } else if (selected.getAdresse().equals("")) {
            JsfUtil.addErrorMessage("L'adresse du client est obligatoire");

        } else if (!selected.getRaisonSociale().equals("") && !selected.getAdresse().equals("")) {
            if (SessionUtil.getConnectedUser() != null) {
                selected.setUtilisateur(SessionUtil.getConnectedUser());
                System.out.println("haaa l user " + SessionUtil.getConnectedUser());
                getFacade().create(selected);
                setList(getFacade().listClientSociete(SessionUtil.getConnectedUser(), recherche));
                //setList(getFacade().findAll());
                selected = null;
                recherche = null;
                try {
                    SessionUtil.redirect("Create.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
                }
                JsfUtil.addSuccessMessage("Client créée avec succès");
            }

        }
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction == PersistAction.CREATE) {
                    if (selected.getRaisonSociale().equals("")) {
                        JsfUtil.addErrorMessage("La raison sociale ne peut pas etre vide");
                    } else if (selected.getAdresse().equals("")) {
                        JsfUtil.addErrorMessage("L'adresse du client est obligatoire");

                    } else if (!selected.getRaisonSociale().equals("") && !selected.getAdresse().equals("")) {
                        if (SessionUtil.getConnectedUser() != null) {
                            selected.setUtilisateur(SessionUtil.getConnectedUser());
                            getFacade().create(selected);
                            setList(getFacade().listClientSociete(SessionUtil.getConnectedUser(), recherche));
                            //setList(getFacade().findAll());
                            selected = null;
                            recherche = null;
                            SessionUtil.redirect("List.xhtml");
                        }

                    }
                } else if (persistAction == PersistAction.UPDATE) {
                    getFacade().edit(selected);
                    //setList(getFacade().findAll());
                    selected = null;
                    recherche = null;
                    SessionUtil.redirect("List.xhtml");

                } else {
                    getFacade().remove(selected);
                    Utilisateur utilisateur = SessionUtil.getConnectedUser();
                    setList(getFacade().listClientSociete(utilisateur, recherche));
                    //setList(getFacade().findAll());
                    recherche = null;
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

    public Client getClient(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Client> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Client> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Client.class)
    public static class ClientControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ClientController controller = (ClientController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "clientController");
            return controller.getClient(getKey(value));
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
            if (object instanceof Client) {
                Client o = (Client) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Client.class.getName()});
                return null;
            }
        }

    }

}
