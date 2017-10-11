package controler;

import bean.Fournisseur;
import bean.Produit;
import bean.Utilisateur;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import controler.util.SessionUtil;
import java.io.IOException;
import service.ProduitFacade;

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

@Named("produitController")
@SessionScoped
public class ProduitController implements Serializable {

    @EJB
    private service.ProduitFacade ejbFacade;
    private List<Produit> items = null;
    private Produit selected;
    private Fournisseur fournisseurNull = null;
    private String recherche="";
    private List<Produit> list = null;
    
    public String prixProduit(Double prix){
        if(prix==null)
            return "-";
        else return prix+"";
    }
   

    public void reinitialiser() {
        selected = null;
    }
    
      public void edite(){
        try {
            SessionUtil.redirect("/Gestion_commerciale/faces/produit/Edit.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
      public void redirectListe(){
        try {
            SessionUtil.redirect("/Gestion_commerciale/faces/produit/List.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getColorStock(Double qte) {
        if (qte != null) {
            if (qte == 0.0) {
                return "#ec0622";
            } else {
                return "orange";
            }
        } else {
            return "";
        }
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

    public List<Produit> getList() {
        list = new ArrayList<>();
        if (recherche == null) {
            recherche = "";
        }
        Utilisateur utilisateur = SessionUtil.getConnectedUser();
        if (utilisateur != null) {
            if(getFacade().listProduitSociete(utilisateur, recherche)!=null) list = getFacade().listProduitSociete(utilisateur, recherche);
            else list = new ArrayList<>();
        } else {

            list = getItems();
        }

        return list;
    }

   
    
   
    
    

    public void setList(List<Produit> list) {
        this.list = list;
    }

    public void rechercheByCritere() {
        Utilisateur utilisateur = SessionUtil.getConnectedUser();
        setList(getList());
    }

    public Fournisseur getFournisseurNull() {
        return fournisseurNull;
    }

    public void setFournisseurNull(Fournisseur fournisseurNull) {
        this.fournisseurNull = fournisseurNull;
    }

    public ProduitController() {
    }

    public Produit getSelected() {
        if (selected == null) {
            selected = new Produit();
        }
        return selected;
    }

    public void setSelected(Produit selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ProduitFacade getFacade() {
        return ejbFacade;
    }

    public Produit prepareCreate() {
        selected = new Produit();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ProduitCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ProduitUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ProduitDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Produit> getItems() {

        items = getFacade().findAll();
        return items;
    }

    public void save(Produit selected) {
        if (SessionUtil.getConnectedUser() != null) {

        }
        // System.out.println("ha ref"+selected.getReference());
        //if(selected.getLibelle()!=null) System.out.println("ha lib"+selected.getLibelle());
        //System.out.println("ha prix"+selected.getPrixUnitaire());
        if (selected.getReference() == null) {
            System.out.println("haaa ref " + getSelected().getReference());
            JsfUtil.addErrorMessage("Veuillez entrer la réference du produit");
        } else if (selected.getFournisseur() == null) {
            JsfUtil.addErrorMessage("Veuillez choisir un fournisseur");
        } else if (selected.getLibelle().equals("")) {
            JsfUtil.addErrorMessage("Veuillez entrer le nom du produit");
        } else if (selected.getPrixDirham() == null && selected.getPrixDollar()== null && selected.getPrixEuro()== null) {
            JsfUtil.addErrorMessage("Veuillez entrer le prix unitaire du produit");
//        } else if (selected.getPrixDirham() !=null) {
//            if(selected.getPrixDirham()<=0)
//            JsfUtil.addErrorMessage("Le prix unitaire en dirham doit etre supérieur à zéro");
//        } else if (selected.getPrixDollar()!=null) {
//            if(selected.getPrixDollar()<=0)
//            JsfUtil.addErrorMessage("Le prix unitaire en dollar doit etre supérieur à zéro");
//        } else if (selected.getPrixEuro()!=null) {
//            if(selected.getPrixEuro()<=0)
//            JsfUtil.addErrorMessage("Le prix unitaire en euro doit etre supérieur à zéro");
        } else if (selected.getQuantiteStock() == null) {
            JsfUtil.addErrorMessage("Veuillez saisir la quantité du stock");
        } else if (selected.getQuantiteStock() <= 0) {
            JsfUtil.addErrorMessage("La quantité doit etre supérieur à zéro");
        } else {
            selected.setUtilisateur(SessionUtil.getConnectedUser());
            getFacade().create(selected);
            setList(getFacade().listProduitSociete(SessionUtil.getConnectedUser(), recherche));
            selected = null;
            recherche = null;

            try {

                SessionUtil.redirect("List.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(ProduitController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();

            try {
                if (persistAction == PersistAction.CREATE) {
                    save(selected);

                } else if (persistAction == PersistAction.UPDATE) {
                    getFacade().edit(selected);
                    selected = null;
                    recherche = null;
                    SessionUtil.redirect("List.xhtml");
                } else {
                    getFacade().remove(selected);
                    setList(getFacade().listProduitSociete(SessionUtil.getConnectedUser(), recherche));

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

    public Produit getProduit(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Produit> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Produit> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Produit.class)
    public static class ProduitControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProduitController controller = (ProduitController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "produitController");
            return controller.getProduit(getKey(value));
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
            if (object instanceof Produit) {
                Produit o = (Produit) object;
                return getStringKey(o.getReference());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Produit.class.getName()});
                return null;
            }
        }

    }

}
