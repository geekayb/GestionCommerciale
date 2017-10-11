package controler;

import bean.FactureItem;
import bean.Produit;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import service.FactureItemFacade;

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

@Named("factureItemController")
@SessionScoped
public class FactureItemController implements Serializable {

    @EJB
    private service.FactureItemFacade ejbFacade;
    private List<FactureItem> items = null;
    private FactureItem selected;
    
    
    public void getQuantiteProduit(Produit produit){
        //return getFacade().getQuantitePrduit(produit);
    }

    public FactureItemController() {
    }

    public FactureItem getSelected() {
        if(selected==null)selected=new FactureItem();
        return selected;
    }

    public void setSelected(FactureItem selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private FactureItemFacade getFacade() {
        return ejbFacade;
    }

    public FactureItem prepareCreate() {
        selected = new FactureItem();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("FactureItemCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("FactureItemUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("FactureItemDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<FactureItem> getItems() {
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
                   // if(selected.getFacture().getFactureItems()== null) {
                        //selected.getFacture().getFactureItems() = new List<FactureItem>();
                    //}
                    selected.getFacture().getFactureItems().add(selected);
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

    public FactureItem getFactureItem(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<FactureItem> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<FactureItem> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = FactureItem.class)
    public static class FactureItemControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            FactureItemController controller = (FactureItemController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "factureItemController");
            return controller.getFactureItem(getKey(value));
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
            if (object instanceof FactureItem) {
                FactureItem o = (FactureItem) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), FactureItem.class.getName()});
                return null;
            }
        }

    }

}
