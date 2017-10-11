package controler;

import bean.Client;
import bean.Facture;
import bean.Paiement;
import bean.Utilisateur;
import controler.util.DateUtil;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import controler.util.SessionUtil;
import service.PaiementFacade;

import java.io.Serializable;
import java.math.BigDecimal;
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
import service.FactureFacade;

@Named("paiementController")
@SessionScoped
public class PaiementController implements Serializable {

    @EJB
    private service.PaiementFacade ejbFacade;
    private List<Paiement> items = null;
    private Paiement selected;
    private Date dateDebute;
    private Date dateFin;
    private List<Paiement> list;
    private Client client;
    private BigDecimal montantTotalPaiementDh;
    private BigDecimal montantTotalPaiementEuro;
    private BigDecimal montantTotalPaiementDollar;
    private Boolean size;
    @EJB
    FactureFacade factureFacade;

    public PaiementController() {
    }

    public Paiement getSelected() {
        if (selected == null) {
            selected = new Paiement();
            selected.setMontant(BigDecimal.ZERO);
        }
        return selected;
    }

    public Boolean getSize() {
        if(size == null)size = false;
        return size;
    }

    public void setSize(Boolean size) {
        this.size = size;
    }
    
    

    public Boolean sizeList(){
        if(getList().isEmpty())size = false;
        else size =true;
        return size;
    }
    public BigDecimal getMontantTotalPaiementDh() {
        if (montantTotalPaiementDh == null) {
            montantTotalPaiementDh = BigDecimal.ZERO;
        }else  montantTotalPaiementDh = getFacade().getMontantTotalPaiementsDh(SessionUtil.getConnectedUser(), dateDebute, dateFin, client);
        return montantTotalPaiementDh;
    }

    public void setMontantTotalPaiementDh(BigDecimal montantTotalPaiementDh) {
        this.montantTotalPaiementDh = montantTotalPaiementDh;
    }

    public BigDecimal getMontantTotalPaiementEuro() {
         if (montantTotalPaiementEuro == null) {
            montantTotalPaiementEuro = BigDecimal.ZERO;
        }else  montantTotalPaiementEuro = getFacade().getMontantTotalPaiementsEuro(SessionUtil.getConnectedUser(), dateDebute, dateFin, client);
        return montantTotalPaiementEuro;
    }

    public void setMontantTotalPaiementEuro(BigDecimal montantTotalPaiementEuro) {
        this.montantTotalPaiementEuro = montantTotalPaiementEuro;
    }

    public BigDecimal getMontantTotalPaiementDollar() {
        if (montantTotalPaiementDollar == null) {
            montantTotalPaiementDollar = BigDecimal.ZERO;
        }else  montantTotalPaiementDollar = getFacade().getMontantTotalPaiementsDollar(SessionUtil.getConnectedUser(), dateDebute, dateFin, client);
        return montantTotalPaiementDollar;
    }

    public void setMontantTotalPaiementDollar(BigDecimal montantTotalPaiementDollar) {
        this.montantTotalPaiementDollar = montantTotalPaiementDollar;
    }
    
    

    public Client getClient() {
        if (client == null) {
            client = new Client();
        }
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Paiement> getList() {
        Utilisateur u = SessionUtil.getConnectedUser();
        if (u != null) {
            list = new ArrayList<>();
            list = getFacade().findPaimentByCritere(u, dateDebute, dateFin, client);
            montantTotalPaiementDh = getFacade().getMontantTotalPaiementsDh(u, dateDebute, dateFin, client);
        } else {
            list = getItems();
        }
        return list;
    }

    public void setList(List<Paiement> list) {
        this.list = list;
    }

    public Date getDateDebute() {
        if (dateDebute == null) {
            dateDebute = new Date();
        }
        return dateDebute;
    }

    public void setDateDebute(Date dateDebute) {
        this.dateDebute = dateDebute;
    }

    public Date getDateFin() {
        if (dateFin == null) {
            dateFin = new Date();
        }

        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getDateSysteme() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        String currentTime = simpleDateFormat.format(new Date());

        return currentTime;
    }

    public void save() {
        // System.out.println(getSelected().getFacture().getMontantDu());
        getSelected().getFacture().setMontantDu(getSelected().getFacture().getMontantDu().subtract(getSelected().getMontant()));
        getSelected().getFacture().setMontantPaye(getSelected().getFacture().getMontantPaye().add(getSelected().getMontant()));
        getSelected().getFacture().setEtat(getEtatFacture(getSelected().getFacture()));
        factureFacade.edit(getSelected().getFacture());
        getFacade().create(selected);
        JsfUtil.addSuccessMessage("Le paiement a été ajouté avec succès !!");
    }

    public String getEtatFacture(Facture facture) {
        if (facture.getMontantDu().compareTo(BigDecimal.ZERO) == 1 || (facture.getMontantDu().compareTo(BigDecimal.ZERO) == 0 && facture.getMontantPaye().compareTo(BigDecimal.ZERO) == 0)) {
            return "Non payée";
//        } else if (facture.getMontantDu().compareTo(BigDecimal.ZERO)  == 0   && facture.getMontantPaye().compareTo(BigDecimal.ZERO) == 1) {
//            return "payée";
        } else {
            return "payée";
        }
    }

    public BigDecimal getMontantTotalPaiements() {

        if (SessionUtil.getConnectedUser() == null) {
            return BigDecimal.ZERO;
        } else {
            return getFacade().getMontantTotalPaiementsDh(SessionUtil.getConnectedUser(), dateDebute, dateFin, client);
        }
    }

    public void setSelected(Paiement selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private PaiementFacade getFacade() {
        return ejbFacade;
    }

    public Paiement prepareCreate() {
        selected = new Paiement();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PaiementCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("PaiementUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("PaiementDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Paiement> getItems() {
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

    public Paiement getPaiement(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Paiement> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Paiement> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Paiement.class)
    public static class PaiementControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PaiementController controller = (PaiementController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "paiementController");
            return controller.getPaiement(getKey(value));
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
            if (object instanceof Paiement) {
                Paiement o = (Paiement) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Paiement.class.getName()});
                return null;
            }
        }

    }

}
