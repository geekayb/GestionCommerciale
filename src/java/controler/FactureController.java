package controler;

import bean.Client;
import static bean.Devis_.client;
import bean.Facture;
import bean.FactureItem;
import bean.Paiement;
import bean.Produit;
import bean.Utilisateur;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import controler.util.PdfUtil;
import controler.util.SessionUtil;
import java.io.IOException;
import service.FactureFacade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import net.sf.jasperreports.engine.JRException;
import service.FactureItemFacade;
import service.PaiementFacade;
import service.ProduitFacade;

@Named("factureController")
@SessionScoped
public class FactureController implements Serializable {

    @EJB
    private service.FactureFacade ejbFacade;
    private List<Facture> items = null;
    private Facture selected;
    private List<FactureItem> factureItems = null;
    private FactureItem factureItem;
    private FactureItem factureItem2;
    private Double quantitStock;
    private double prix;
    private Produit produit;
    private String etat="";
    private List<Facture> allFacures;
    private List<Facture> facturesRapport;
    private List<Facture> facturesEnRetard;
    private String recherche="";
    Utilisateur utilisateur;
    private Produit p = new Produit();
    private Client client;
    private String contact;
    private Boolean isClient;
    private Boolean isFournisseur;
    private List<Facture>facturesRecentes;
    private Boolean rapportClient;
    private Boolean rapportFacture;
    private Boolean contactClientFour;
     private List<Produit> listProduitDevise = null;
     private List<Produit> listProduitDeviseEdit = null;
    @EJB
    FactureItemFacade factureItemFacade;
    @EJB
    ProduitFacade produitFacade;
    @EJB
    PaiementFacade paiementFacade;

    public Boolean getRapportClient() {
        if(rapportClient==null)rapportClient=false;
        return rapportClient;
    }

    public void setRapportClient(Boolean rapportClient) {
        this.rapportClient = rapportClient;
    }

    public Boolean getRapportFacture() {
        if(rapportFacture==null)rapportFacture=true;
        return rapportFacture;
    }

    public void setRapportFacture(Boolean rapportFacture) {
        this.rapportFacture = rapportFacture;
    }

    public Boolean getContactClientFour() {
         if(contactClientFour==null)contactClientFour=false;
        return contactClientFour;
    }

    public void setContactClientFour(Boolean contactClientFour) {
        this.contactClientFour = contactClientFour;
    }
    
    
     public List<Produit> getListProduitDevise() {
          Utilisateur utilisateur = SessionUtil.getConnectedUser();
        if(listProduitDevise==null){
            listProduitDevise = new ArrayList<>();
        }
           if(!selected.getDevise().equals(""))listProduitDevise = getFacade().getProduitByDevise(selected.getDevise(), utilisateur);

        return listProduitDevise;
    }
     public List<Produit> getListProduitDeviseEdit() {
          Utilisateur utilisateur = SessionUtil.getConnectedUser();
        if(listProduitDeviseEdit==null){
            listProduitDeviseEdit = new ArrayList<>();
            
        }
        listProduitDeviseEdit=getFacade().getProduitByDevise(selected.getDevise(), utilisateur);
        return listProduitDeviseEdit;
    }
      public void setListProduitDeviseEdit(List<Produit> listProduitDevise) {
        this.listProduitDeviseEdit = listProduitDevise;
    }

    public void setListProduitDevise(List<Produit> listProduitDevise) {
        this.listProduitDevise = listProduitDevise;
    }
    
     public void getProduitByDevise(){
        System.out.println("dkheeeeel l getProduitByDevise");
        Utilisateur utilisateur = SessionUtil.getConnectedUser();
       setListProduitDevise(getFacade().getProduitByDevise(selected.getDevise(), utilisateur));
       //factureItem.setProduit(null);
         setQuantitStock(0.0);
         setPrix(0);
         
       
       
    }
    
    public void selectContact(){
        setRapportClient(false);
        setRapportFacture(false);
        setContactClientFour(true);
           System.out.println("haaa rapClient "+ rapportClient);
           System.out.println("haaa rapFactue "+ rapportFacture);
           System.out.println("haaa conatct "+ contactClientFour);
     
    }
    public void selectRapportFacture(){
        setRapportClient(false);
        setRapportFacture(true);
        setContactClientFour(false);
        System.out.println("haaa rapClient "+ rapportClient);
           System.out.println("haaa rapFactue "+ rapportFacture);
           System.out.println("haaa conatct "+ contactClientFour);
    }
    public void selectRapportClient(){
        setRapportClient(true);
        setRapportFacture(false);
        setContactClientFour(false);
        System.out.println("haaa rapClient "+ rapportClient);
           System.out.println("haaa rapFactue "+ rapportFacture);
           System.out.println("haaa conatct "+ contactClientFour);
    }
    
   
    
    
    
    
    public String getContact(){
        if(contact==null)contact="";
        return contact;
    }
    
    

    public Boolean getIsClient() {
        if(isClient==null){isClient=true;}
        return isClient;
    }

    public void setIsClient(Boolean isClient) {
        this.isClient = isClient;
    }

    public Boolean getIsFournisseur() {
           if(isFournisseur==null){isFournisseur=false;}
        return isFournisseur;
    }

    public void setIsFournisseur(Boolean isFournisseur) {
        this.isFournisseur = isFournisseur;
    }
     public void contactSelected() {
         System.out.println("haaa l contact li 3zel "+ getContact());
        if (getContact().equals("Client")) {
            setIsClient(true);
            setIsFournisseur(false);
            } else if (getContact().equals("Fournisseur")) {
                setIsClient(false);
            setIsFournisseur(true);
            }
         System.out.println("haaaa isclient " + isClient);
         System.out.println("haaaa isfour " + isFournisseur);

    }
    

//    public String getMessage(){
//        return "Etes vous sure de payer tout le montant restant "+ getSelected().getMontantDu() + "Dhs";
//    }
//
    public void setContact(String contact) {
        this.contact = contact;
    }

    public Boolean isPaid() {
        if(getEtatFacture(selected).equals("Non payée"))return false;
        else if(getEtatFacture(selected).equals("payée")) return true;
        else return  false;
    }
    
    public void edite(){
        try {
            SessionUtil.redirect("/Gestion_commerciale/faces/facture/Edit.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
       public void redirectListe(){
        try {
            SessionUtil.redirect("/Gestion_commerciale/faces/facture/List.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       public void redirectListeRetard(){
        try {
            SessionUtil.redirect("/Gestion_commerciale/faces/facture/Retard.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       
    public Produit getP() {
        if(p==null)p= new Produit();
        p.setQuantiteStock(100000000.0);
        p.setPrixDirham(0.0);
        p.setPrixDollar(0.0);
        p.setPrixEuro(0.0);
        return p;
    }

    public void setP(Produit p) {
        this.p = p;
    }

    public Client getClient() {
        if(client==null)client = new Client();
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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

    public List<Facture> getFacturesEnRetard() {
    
           

            //allFacures = getFacade().findAll();
            Utilisateur u = SessionUtil.getConnectedUser();
            if (u != null) {
                 facturesEnRetard = new ArrayList<>();
                facturesEnRetard = getFacade().findFactureEnRetard(u, recherche);
            } else {
                facturesEnRetard = getItems();
            }
      

        return facturesEnRetard;
    }

    public void setFacturesEnRetard(List<Facture> facturesEnRetard) {
        this.facturesEnRetard = facturesEnRetard;
    }

    public List<Facture> getAllFacures() {
        
            Utilisateur u = SessionUtil.getConnectedUser();
            if (u != null) {
                allFacures = new ArrayList<>();
                allFacures = getFacade().listFactureSociete(u, recherche, etat);
            } else {        
                allFacures = getItems();
            }
       
        return allFacures;
    }

    
    public void setAllFacures(List<Facture> allFacures) {
        this.allFacures = allFacures;
    }

    public List<Facture> getFacturesRapport() {
        Utilisateur u = SessionUtil.getConnectedUser();
        
            if (u != null) {
                facturesRapport = new ArrayList<>();
                facturesRapport = getFacade().listFactureRapport(u, client);
            } else {        
                allFacures = getItems();
            }
            
            
        return facturesRapport;
    }

    public void setFacturesRapport(List<Facture> facturesRapport) {
        this.facturesRapport = facturesRapport;
    }
    
    

    public List<Facture> getFacturesRecentes() {
          
            Utilisateur u = SessionUtil.getConnectedUser();
            if (u != null) {
                facturesRecentes = new ArrayList<>();
                facturesRecentes = getFacade().listFactureRecentes(u);
            } else {        
                facturesRecentes = getItems();
            }
       
        return facturesRecentes;
    }

    public void setFacturesRecentes(List<Facture> facturesRecentes) {
        this.facturesRecentes = facturesRecentes;
    }
    
  

    public String getEtat() {
        if(etat == null) etat = "";
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

//    public void calculerSousTotal() {
//        if (getFactureItem().getQuantite() != null) {
//            BigDecimal total = BigDecimal.valueOf(getFactureItem().getQuantite() * getFactureItem().getProduit().getPrixUnitaire());
//            getFactureItem().setSousTotalHT(total);
//        } else {
//            getFactureItem().setSousTotalHT(BigDecimal.ZERO);
//        }
//    }
    public List<Facture> findFacturesByEtat() {
        Utilisateur u = SessionUtil.getConnectedUser();
        setAllFacures(getFacade().findFacturesByEtat(etat, u));
        return allFacures;
    }

    public List<Facture> findFactureByCritere() {
        Utilisateur u = SessionUtil.getConnectedUser();
        setAllFacures(getFacade().findFactureByCritere(recherche, u));
        return allFacures;
    }

    public List<Facture> findFactureRetardByCritere() {
        setFacturesEnRetard(getFacturesEnRetard());
        return facturesEnRetard;
    }

    public List<Facture> findFactureEnRetard() {
        setFacturesEnRetard(getFacade().findFactureEnRetard(SessionUtil.getConnectedUser(),recherche));
        return facturesEnRetard;
    }

    public String getColor(String etat) {
        String res = "";
       // System.out.println("haaa l etat" + etat);
        if (etat != null) {

            if (etat.equals("payée")) {
                res = "#8bdc43";

            } else  if (etat.equals("Non payée")){
                res = "orange";
            }else if (etat.equals("annulée")){
                res = "red";
        }
        }
       // System.out.println("haaa l couleur: " + res);
        return res;
    }

    public String getSizeDiv(String etat) {
        String res = "";
       // System.out.println("haaa l etat" + etat);
        if (etat != null) {

            if (etat.equals("payée")) {
                res = "30%";

            } else {
                res = "50%";
            }

        }
       // System.out.println("haaa l couleur: " + res);
        return res;
    }

    public void modifierItem() {
     //   System.out.println("haaahwa dkhel l modifier");
        double qteAvant = getQteByFactureItem();
     //   System.out.println("qte avant" + qteAvant);
        double qteApres = factureItem.getQuantite();
        //double res = (qteAvant - factureItem.getQuantite());
     //   System.out.println("qte aprés" + (qteApres - qteAvant));
        //modfier stock
          if((factureItem.getProduit().getQuantiteStock() + qteAvant) - qteApres >= 0){
        factureItem.getProduit().setQuantiteStock((factureItem.getProduit().getQuantiteStock() + qteAvant) - qteApres);

        factureItemFacade.edit(factureItem);
        produitFacade.edit(factureItem.getProduit());
        selected.setMontantDu(BigDecimal.ZERO);
        selected.setTotalAvecRemise(BigDecimal.ZERO);
        selected.setTotalTax(BigDecimal.ZERO);
        selected.setMontantPaye(BigDecimal.ZERO);
        selected.setNouveauSousTotal(BigDecimal.ZERO);
        selected.setSousTotal(BigDecimal.ZERO);
        selected.setEtat(getEtatFacture(selected));

        List<FactureItem> list = factureItemFacade.findFactureItemsByFacture(selected);
       // System.out.println("ha liste : " + list.size());
        for (int i = 0; i < list.size(); i++) {
            getSelected().setSousTotal(getSelected().getSousTotal().add(list.get(i).getSousTotalHT()));
            getSelected().setTotalTax(getSelected().getTotalTax().add(list.get(i).getSousTotalHT().multiply(BigDecimal.valueOf(list.get(i).getTax()/100))));
            getSelected().setNouveauSousTotal(getSelected().getSousTotal().add(getSelected().getTotalTax()));
            getSelected().setTotalAvecRemise(getSelected().getNouveauSousTotal().subtract(getSelected().getRemise()));
            getSelected().setMontantPaye(getSelected().getMontantPaye());
            getSelected().setMontantDu(getSelected().getTotalAvecRemise().subtract(getSelected().getMontantPaye()));
        }
        getFacade().edit(getSelected());

    }else{
               JsfUtil.addErrorMessage("Opération echouée ! stock insuffisant");
          }
    }

    public Double getQteByFactureItem() {
        return getFacade().getQteByFactureItem(factureItem);
    }

    public void deleteFactureItem(FactureItem factureItem) {
        int res = factureItemFacade.removeFactureItem(factureItem, selected);
        if (res > 0) {
           // System.out.println("haa list f lwl=" + selected.getFactureItems().size());
            selected.getFactureItems().remove(factureItem);
           // System.out.println("haa list f lekher=" + selected.getFactureItems().size());

           // System.out.println("delete FactureItem new Montant=>" + selected.getMontantDu());
            JsfUtil.addSuccessMessage("Ligne de Facture Supprimé avec succes!!");
        } else {
            JsfUtil.addErrorMessage("Erreur lors de la suppression !!");
        }
    }

    public void calculerSousTotal() {
       // System.out.println("dkheeel");
        BigDecimal total = BigDecimal.ZERO;
//        if(getFactureItem().getProduit().getPrixUnitaire()==null)System.out.println("rah null asat");
//        System.out.println("haaa l qte "+ getFactureItem().getQuantite());
        if (getFactureItem().getQuantite() != null) {
            total = total.add(BigDecimal.valueOf((getFactureItem().getQuantite() * getFactureItem().getPrixDevise())));
        }
        if (getFactureItem().getRemise() != null) {
            total = total.subtract(getFactureItem().getRemise());
        }
        getFactureItem().setSousTotalHT(total);

    }
    public void calculerSousTotalService() {
       // System.out.println("dkheeel");
        BigDecimal total = BigDecimal.ZERO;
//        if(getFactureItem().getProduit().getPrixUnitaire()==null)System.out.println("rah null asat");
//        System.out.println("haaa l qte "+ getFactureItem().getQuantite());
        if (getFactureItem2().getQuantite() != null) {
            total = total.add(BigDecimal.valueOf((getFactureItem2().getQuantite() * getFactureItem2().getPrixDevise())));
        }
        if (getFactureItem2().getRemise() != null) {
            total = total.subtract(getFactureItem2().getRemise());
        }
        getFactureItem2().setSousTotalHT(total);

    }

    public void getQtePixByProduit() {
        setQuantitStock(produitFacade.getQteByProduit(factureItem.getProduit()));
        setPrix(produitFacade.getPrixByProduit(factureItem.getProduit(),selected.getDevise()));
        getFactureItem().setSousTotalHT(BigDecimal.ZERO);
        getFactureItem().setQuantite(0.0);
        getFactureItem().setRemise(BigDecimal.ZERO);
        getFactureItem().setTax(null);
    }

    public void addToFacture() {
        //factureItemFacade.addFactureItemToItem(getFactureItem(), selected);
        if(selected.getDevise().equals("")){
            JsfUtil.addErrorMessage("Veuillez choisir un devise");
        }
        else if (getFactureItem().getProduit() == null) {
            JsfUtil.addErrorMessage("Veuillez choisir un produit");
        } else if (getFactureItem().getQuantite() > getFactureItem().getProduit().getQuantiteStock()) {
            JsfUtil.addErrorMessage("Stock insuffisant !!");
        } else if (getFactureItem().getQuantite() <= 0) {
            JsfUtil.addErrorMessage("La quantitéte saisie est invalide");
        } else if (getFactureItem().getRemise().signum() == -1) {
            JsfUtil.addErrorMessage("Remise doit étre supérieur à 0Dh");
        } else if (getFactureItem().getRemise().compareTo(factureItem.getSousTotalHT()) == 1) {
            JsfUtil.addErrorMessage("Le remise saisi est supérieur au total");
        } else if (produitExist(getFactureItem(), getFactureItems())) {
            JsfUtil.addErrorMessage("Article déja choisi");
        } else {
                       getFactureItem().setPrixDevise(produitFacade.getPrixByProduit(getFactureItem().getProduit(),selected.getDevise()));

            getFactureItems().add(clone(getFactureItem()));
             for (int i = 0; i < factureItems.size(); i++) {
           System.out.println("haaa les ref kamlin" + factureItems.get(i).getProduit().getReference());
             }
            //modifier les stock
            // getFactureItem().getProduit().setQuantiteStock(getFactureItem().getProduit().getQuantiteStock() - getFactureItem().getQuantite());
            //produitFacade.edit(getFactureItem().getProduit());
//            for (int i = 0; i < getFactureItems().size(); i++) {
//                getSelected().setSousTotal(getSelected().getSousTotal().add(getFactureItems().get(i).getSousTotalHT()));
//                getSelected().setTotalTax(getSelected().getTotalTax().add(getFactureItems().get(i).getSousTotalHT().multiply(BigDecimal.valueOf(getFactureItems().get(i).getTax()))));
//            }

            getSelected().setSousTotal(getSelected().getSousTotal().add(getFactureItem().getSousTotalHT()));
            getSelected().setTotalTax(getSelected().getTotalTax().add(getFactureItem().getSousTotalHT().multiply(BigDecimal.valueOf(getFactureItem().getTax()/100))));

            getSelected().setNouveauSousTotal(getSelected().getSousTotal().add(getSelected().getTotalTax()));
            getSelected().setTotalAvecRemise(getSelected().getNouveauSousTotal().subtract(getSelected().getRemise()));
            getSelected().setMontantPaye(getSelected().getMontantPaye());
            getSelected().setMontantDu(getSelected().getTotalAvecRemise().subtract(getSelected().getMontantPaye()));

            setFactureItem(null);
            quantitStock = 0.0;
            prix = 0;
            p = new Produit();
            getFactureItem().setRemise(BigDecimal.ZERO);
            //getFactureItem().getProduit().setReference(-1);
          //  factureItem = new FactureItem();
  

            //System.out.println(getFactureItems().size() + "");

        }

    }
    
    
        //------------------------------test
    
        public void addServiceToFacture() {
        //factureItemFacade.addFactureItemToItem(getFactureItem(), selected);
        if (getFactureItem2().getProduit().getReference() == null) {
            JsfUtil.addErrorMessage("Veuillez entrer la réference du produit"); 
        }else if(getFactureItem2().getProduit().getLibelle().equals("")){
           // System.out.println("haaaaaaa smiyaaaaaa" + getFactureItem2().getProduit().getLibelle());
           JsfUtil.addErrorMessage("Veuillez entrer le nom du produit"); 
            } else if (getFactureItem2().getQuantite() == null) {
            JsfUtil.addErrorMessage("Veuillez entrer la quantité voulu");
        } else if (getFactureItem2().getQuantite() <= 0) {
            JsfUtil.addErrorMessage("La quantitéte doit étre supérieur à 0");
        }else if(getFactureItem2().getPrixDevise()==0.0){
          JsfUtil.addErrorMessage("Veuillez entrer le prix unitaire");
        } else if (getFactureItem2().getRemise().signum() == -1) {
            JsfUtil.addErrorMessage("Remise doit étre supérieur à 0Dh");
        } else if (getFactureItem2().getRemise().compareTo(factureItem.getSousTotalHT()) == 1) {
            JsfUtil.addErrorMessage("Le remise saisi est supérieur au total");
        } else if (produitExist(getFactureItem2(), getFactureItems())) {
            JsfUtil.addErrorMessage("Article déja choisi");
        } else if (getSelected().getDevise().equals("")) {
            JsfUtil.addErrorMessage("Veuillez choisir un devise");
        } else {
            //getFactureItem2().setPrixDevise(getFactureItem2().getProduit().getPrixDirham());
            getFactureItems().add(clone(getFactureItem2()));
             for (int i = 0; i < factureItems.size(); i++) {
           System.out.println("haaa les ref kamlin" + factureItems.get(i).getProduit().getReference());
             }
            //modifier les stock
            // getFactureItem().getProduit().setQuantiteStock(getFactureItem().getProduit().getQuantiteStock() - getFactureItem().getQuantite());
            //produitFacade.edit(getFactureItem().getProduit());
//            for (int i = 0; i < getFactureItems().size(); i++) {
//                getSelected().setSousTotal(getSelected().getSousTotal().add(getFactureItems().get(i).getSousTotalHT()));
//                getSelected().setTotalTax(getSelected().getTotalTax().add(getFactureItems().get(i).getSousTotalHT().multiply(BigDecimal.valueOf(getFactureItems().get(i).getTax()))));
//            }

            getSelected().setSousTotal(getSelected().getSousTotal().add(getFactureItem2().getSousTotalHT()));
            getSelected().setTotalTax(getSelected().getTotalTax().add(getFactureItem2().getSousTotalHT().multiply(BigDecimal.valueOf(getFactureItem2().getTax()/100))));

            getSelected().setNouveauSousTotal(getSelected().getSousTotal().add(getSelected().getTotalTax()));
            getSelected().setTotalAvecRemise(getSelected().getNouveauSousTotal().subtract(getSelected().getRemise()));
            getSelected().setMontantPaye(getSelected().getMontantPaye());
            getSelected().setMontantDu(getSelected().getTotalAvecRemise().subtract(getSelected().getMontantPaye()));

            setFactureItem2(null);
            quantitStock = 0.0;
            prix = 0;
            p = new Produit();
            getFactureItem().setRemise(BigDecimal.ZERO);
            //getFactureItem().getProduit().setReference(-1);
          //  factureItem = new FactureItem();
  

            //System.out.println(getFactureItems().size() + "");

        }

    }
    
    
    //-----------------------------
    
        
        public String taxToString(Double tax){
            String text="";
            if(tax == 0.0)text="0%";
            else if(tax == 20.0)text="20%";
            else if(tax == 14.0)text ="14%";
            else if(tax == 10.0)text ="10%";
            else if(tax == 14.0)text ="14%";
            else if(tax == 7.0)text ="7%";
            System.out.println("haaa tva " + text);
            return text;
        }
    
    
    
    
    

    public void addItemToFactureEdit() {
        //factureItemFacade.addFactureItemToItem(getFactureItem(), selected);
        if (getFactureItem().getProduit() == null) {
            JsfUtil.addErrorMessage("Veuillez choisir un produit");
        } else if (getFactureItem().getQuantite() > getFactureItem().getProduit().getQuantiteStock()) {
            JsfUtil.addErrorMessage("Stock insuffisant");
        } else if (getFactureItem().getQuantite() <= 0) {
            JsfUtil.addErrorMessage("La quantitéte saisie est invalide");
        } else if (getFactureItem().getRemise().signum() == -1) {
            JsfUtil.addErrorMessage("Remise doit étre supérieur à 0Dh");
        } else if (getFactureItem().getRemise().compareTo(factureItem.getSousTotalHT()) == 1) {
            JsfUtil.addErrorMessage("Le remise saisi est supérieur au total");
        } else if (produitExist(factureItem, selected.getFactureItems())) {
            JsfUtil.addErrorMessage("Ligne de facture déja existée");
        } else {
            
            getFactureItems().add(clone(getFactureItem()));
            //modifier les stock
            // getFactureItem().getProduit().setQuantiteStock(getFactureItem().getProduit().getQuantiteStock() - getFactureItem().getQuantite());
            //produitFacade.edit(getFactureItem().getProduit());
//            for (int i = 0; i < getFactureItems().size(); i++) {
//                getSelected().setSousTotal(getSelected().getSousTotal().add(getFactureItems().get(i).getSousTotalHT()));
//                getSelected().setTotalTax(getSelected().getTotalTax().add(getFactureItems().get(i).getSousTotalHT().multiply(BigDecimal.valueOf(getFactureItems().get(i).getTax()))));
//            }

            getSelected().setSousTotal(getSelected().getSousTotal().add(getFactureItem().getSousTotalHT()));
            getSelected().setTotalTax(getSelected().getTotalTax().add(getFactureItem().getSousTotalHT().multiply(BigDecimal.valueOf(getFactureItem().getTax()/100))));

            getSelected().setNouveauSousTotal(getSelected().getSousTotal().add(getSelected().getTotalTax()));
            getSelected().setTotalAvecRemise(getSelected().getNouveauSousTotal().subtract(getSelected().getRemise()));
            getSelected().setMontantPaye(getSelected().getMontantPaye());
            getSelected().setMontantDu(getSelected().getTotalAvecRemise().subtract(getSelected().getMontantPaye()));
            getSelected().setEtat(getEtatFacture(selected));
            getFacade().edit(selected);
            factureWithItems(selected, getFactureItems());
            JsfUtil.addSuccessMessage("Ligne de facture ajoutée avec succès");
            getFactureItems().clear();
            factureItem = null;
            quantitStock = 0.0;
            prix = 0;
            //allFacures = null;

           // System.out.println(getFactureItems().size() + "");

        }
    }
    

    

    
    
    
    
    
    
    
    public Boolean produitExist(FactureItem factureItem, List<FactureItem> factureItems) {
        Boolean exist = false;
          System.out.println("haaa ref dyal artcile jdid "+ factureItem.getProduit().getReference());
        for (int i = 0; i < factureItems.size(); i++) {
            System.out.println("haaa ref dyal artcile l9dam "+ factureItems.get(i).getProduit().getReference());
            if (getFactureItem().getProduit().getReference() == factureItems.get(i).getProduit().getReference()) {
                exist = true;
            }

        }
        return exist;
    }

    public void enregisterFacture() {
       // System.out.println(getFactureItems().size() + "");

        if (selected.getClient() == null) {
            JsfUtil.addErrorMessage("Veuillez sélectionner un client");
        } else if (selected.getDateFacture() == null) {
            JsfUtil.addErrorMessage("Veuillez choisir la date de la facture");
        } else if (selected.getDateEcheance() == null) {
            JsfUtil.addErrorMessage("Veuillez choisir la date d'échenace de la facture");
        } else if (selected.getDateFacture().after(selected.getDateEcheance())) {
            JsfUtil.addErrorMessage("Veuillez vérifier les dates saisies");
        } else if (getFactureItems().isEmpty()) {
            JsfUtil.addErrorMessage("Veuillez ajouter des articles");
        } else {
            selected.setEtat(getEtatFacture(selected));
            selected.setUtilisateur(SessionUtil.getConnectedUser());
            getFacade().create(selected);
            factureWithItems(selected, getFactureItems());
            
            setAllFacures(getFacade().listFactureSociete(SessionUtil.getConnectedUser(),recherche,etat));

            JsfUtil.addSuccessMessage("Facture enregistrée avec succès");
            selected = null;
            setEtat(null);
            getFactureItems().clear();
            try {
                SessionUtil.redirect("List.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(FactureController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public void AnnulerFacture() {
//        for (int i = 0; i < factureItems.size(); i++) {
//            factureItems.get(i).getProduit().setQuantiteStock(factureItems.get(i).getProduit().getQuantiteStock() + factureItems.get(i).getQuantite());
//            produitFacade.edit(factureItems.get(i).getProduit());
//        }
        //selected = null;
        getFactureItems().clear();
        try {
            SessionUtil.redirect("List.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(FactureController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void calculerMontantDu() {
        if (getSelected().getRemise() != null) {
          //  System.out.println("dkheeel");
           // System.out.println(selected.getRemise());
            getSelected().setTotalAvecRemise(selected.getNouveauSousTotal().subtract(selected.getRemise()));
            getSelected().setMontantDu(selected.getTotalAvecRemise().subtract(selected.getMontantPaye()));
           // System.out.println("khrej");
            if (selected.getClient() != null) {
                getFacade().edit(selected);
            }
        }

    }

    public FactureItem clone(FactureItem factureItem) {
        FactureItem clone = new FactureItem();
        clone.setFacture(factureItem.getFacture());
        clone.setProduit(factureItem.getProduit());
        clone.setQuantite(factureItem.getQuantite());
        clone.setTax(factureItem.getTax());
        clone.setRemise(factureItem.getRemise());
        clone.setPrixDevise(factureItem.getPrixDevise());
        clone.setSousTotalHT(factureItem.getSousTotalHT());
        return clone;
    }

    public List<Paiement> getPaimentsByFacture() {
        return getFacade().getPaimentsByFacture(selected);
    }

    public String formatDateFacture(Date date) {
       // System.out.println("dkheeeeeeeeeeeeeeeeeeeeeeeeeeeel");
       // System.out.println("haaa date" + date.toString());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        String currentTime = simpleDateFormat.format(date);
       // System.out.println("khreeeeeej");
        return currentTime;

    }

    public String getEtatFacture(Facture facture) {
        if (facture.getMontantDu().compareTo(BigDecimal.ZERO) == 1 || facture.getMontantDu().compareTo(BigDecimal.ZERO) == 0 && facture.getMontantPaye().compareTo(BigDecimal.ZERO) == 0) {
            return "Non payée";
        } else if (facture.getMontantDu() == BigDecimal.ZERO && facture.getMontantPaye().compareTo(BigDecimal.ZERO) == 1) {
            return "payée";
        }
        return "";
    }

    public List<FactureItem> findFactureItemsByFacture() {
        return factureItemFacade.findFactureItemsByFacture(selected);
    }

    public int maxIdFacture() {
        return getFacade().generateId("Facture", "id") + 1;

    }

    public FactureItem getFactureItem() {
        if (factureItem == null) {
            factureItem = new FactureItem();
            factureItem.setSousTotalHT(BigDecimal.ZERO);
            factureItem.setRemise(BigDecimal.ZERO);
            factureItem.setProduit(new Produit());
            factureItem.getProduit().setPrixDirham(0.0);
            factureItem.getProduit().setQuantiteStock(0.0);
            factureItem.setPrixDevise(0.0);
        }
        return factureItem;
    }
    
    //--------------test
    public FactureItem getFactureItem2() {
        if (factureItem == null) {
            factureItem = new FactureItem();
            factureItem.setSousTotalHT(BigDecimal.ZERO);
            factureItem.setRemise(BigDecimal.ZERO);
            factureItem.setProduit(getP());
            factureItem.setQuantite(0.0);
            factureItem.setPrixDevise(0.0);
            //factureItem.getProduit().setQuantiteStock(0.0);
        }
        return factureItem;
    }
     public void setFactureItem2(FactureItem factureItem) {
        this.factureItem = factureItem;
    }
    
//------------------------------------------
    public void setFactureItem(FactureItem factureItem) {
        this.factureItem = factureItem;
    }

    public Produit getProduit() {
        if (produit == null) {
            produit = new Produit();
        }
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public double getQuantitStock() {
        if (quantitStock == null) {
            quantitStock = 0.0;
        }
        return quantitStock;
    }

    public void setQuantitStock(Double quantitStock) {
        this.quantitStock = quantitStock;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public List<FactureItem> getFactureItems() {
        if (factureItems == null) {
            factureItems = new ArrayList<>();
        }
        return factureItems;
    }

    public void setFactureItems(List<FactureItem> factureItems) {
        this.factureItems = factureItems;
    }

    public void factureWithItems(Facture facture, List<FactureItem> factureItems) {
        for (int i = 0; i < factureItems.size(); i++) {
            factureItems.get(i).setFacture(facture);
            if(produitExist( getFactureItems().get(i).getProduit().getReference())==false){
            produitFacade.create(getFactureItems().get(i).getProduit());
            }else{
                  
            getFactureItems().get(i).getProduit().setQuantiteStock(getFactureItems().get(i).getProduit().getQuantiteStock() - getFactureItems().get(i).getQuantite());
            produitFacade.edit(getFactureItems().get(i).getProduit());
         
            }
            factureItemFacade.create(factureItems.get(i));
          
            //modifier stock
         
        }
    }
    
    public Boolean produitExist(Integer reference){
        List<Produit> p;
        p=produitFacade.produitExist(reference);
        if(p == null)return false;
        else return true;
    }

    public FactureController() {
    }

    public Facture getSelected() {
        if (selected == null) {
            selected = new Facture();
            //selected.setClient(new Client());
            selected.setDateFacture(new Date());
            selected.setRemise(BigDecimal.ZERO);
            selected.setMontantDu(BigDecimal.ZERO);
            selected.setTotalAvecRemise(BigDecimal.ZERO);
            selected.setTotalTax(BigDecimal.ZERO);
            selected.setMontantPaye(BigDecimal.ZERO);
            selected.setNouveauSousTotal(BigDecimal.ZERO);
            selected.setSousTotal(BigDecimal.ZERO);
        }

        return selected;
    }

    public void setSelected(Facture selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private FactureFacade getFacade() {
        return ejbFacade;
    }

    public Facture prepareCreate() {
        selected = new Facture();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("FactureCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("FactureUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("FactureDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Facture> getItems() {

        items = getFacade().findAll();

        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction == PersistAction.CREATE) {
//                    if (SessionUtil.getConnectedUser() != null) {
//                        selected.setUtilisateur(SessionUtil.getConnectedUser());
//                    }
                    //Utilisateur u =SessionUtil.getConnectedUser();
                  
                    //setAllFacures(getFacade().listFactureSociete(u));
                    //System.out.println("trrrrrrrrrrr");
                    SessionUtil.redirect("List.xhtml");
                   // setAllFacures(getFacade().listFactureSociete(u));

                } else if (persistAction == PersistAction.UPDATE) {
                    getFacade().edit(selected);
                    SessionUtil.redirect("List.xhtml");
                } else {
                    getFacade().remove(selected);
                    setAllFacures(getFacade().listFactureSociete(SessionUtil.getConnectedUser(),recherche,etat));
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

    public Facture getFacture(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Facture> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Facture> getItemsAvailableSelectOne() {
        return getFacade().findAll();

    }

    @FacesConverter(forClass = Facture.class)
    public static class FactureControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            FactureController controller = (FactureController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "factureController");
            return controller.getFacture(getKey(value));
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
            if (object instanceof Facture) {
                Facture o = (Facture) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Facture.class.getName()});
                return null;
            }
        }

    }
      public void popupa() {
          Paiement paiement = new Paiement();
          if(selected.getMontantDu().compareTo(BigDecimal.ZERO)== 0  || selected.getMontantDu().compareTo(BigDecimal.ZERO)== -1 && selected.getMontantPaye().compareTo(BigDecimal.ZERO) == 1){
              JsfUtil.addErrorMessage("La facture est déja payée");
          }else if(selected.getMontantDu().compareTo(BigDecimal.ZERO)==1 && (selected.getMontantPaye().compareTo(BigDecimal.ZERO) == 1 || selected.getMontantPaye().compareTo(BigDecimal.ZERO) == 0)) {
             paiement.setMontant(selected.getMontantDu());
             paiement.setDatePaiement(new Date());
             paiement.setFacture(selected);
             paiement.setRemarque("Paiement du montant total restant");
             selected.setMontantPaye(selected.getMontantPaye().add(selected.getMontantDu()));
              selected.setMontantDu(BigDecimal.ZERO);
              selected.setEtat(getEtatFacture(selected));
              getFacade().edit(selected);
              paiementFacade.create(paiement);
              
//              try {
//                  SessionUtil.redirect("Edit.xhtml");
//              } catch (IOException ex) {
//                  Logger.getLogger(FactureController.class.getName()).log(Level.SEVERE, null, ex);
//              }
              JsfUtil.addSuccessMessage("Le paiement du montant total a été effectué avec succès");
          }
       
    }
     
    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    
    public String getTypePaiement(Boolean type){
        if(type == true) return "Avance";
        else return "Normal";
    }
    
    
      public void imprimerFacture() throws JRException, IOException {
          FacesContext.getCurrentInstance().getResponseComplete();
          Calendar calendar = Calendar.getInstance();
          calendar.setTime(selected.getDateFacture());
          int annee = calendar.get(calendar.YEAR);
          int mois = calendar.get(calendar.MONTH);
           String RC="", ICE= "" ,IF="" ,TP="" ,Tel="" ,Fax="" ,Email="" ,Site = "";
//          System.out.println("haaa  l id = "+ selected.getId());
//          System.out.println("haaa  l date = "+ selected.getDateFacture());
//          System.out.println("haaa  l RC  = "+ selected.getUtilisateur().getSociete().getRaisonSociale());
//          System.out.println("haaa  l client = "+ selected.getClient().getRaisonSociale());
//          System.out.println("haaa  l montant du = "+ selected.getMontantDu());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("numeroFacture", "#0"+mois+""+annee+"/"+selected.getId());
        params.put("dateFacture", formatDateFacture(selected.getDateFacture())+"");
          params.put("devise", selected.getDevise()+"");
          params.put("logo", "C://uploads//" + SessionUtil.getConnectedUser().getSociete().getLogo()+"");
          
       // params.put("mois", selected.getDateFacture().getMonth()+1+"");
        //params.put("annee", selected.getDateFacture().getYear()+1900+"");
        
         if(!selected.getUtilisateur().getSociete().getRC().equals("")) {
            RC = "RC";
           
        }
        if(!selected.getUtilisateur().getSociete().getICE().equals("")) {
            ICE = "ICE";
            
        }
        if(!selected.getUtilisateur().getSociete().getNumeroIF().equals("")) {
            IF= "IF";
           
        }
        if(!selected.getUtilisateur().getSociete().getTP().equals("")|| selected.getUtilisateur().getSociete().getTP()!=null) {
            TP = "TP";
           
        }
        
        if(!selected.getUtilisateur().getSociete().getTelephone().equals("")) {
            Tel = "Tél";
            
        }
        if(!selected.getUtilisateur().getSociete().getFax().equals("")) {
            Fax = "Fax";
           
        }
        if(!selected.getUtilisateur().getSociete().getEmail().equals("")) {
            Email = "Email";
            
        }
        if(!selected.getUtilisateur().getSociete().getSiteWeb().equals("")) {
            Site = "Site web";
            
        }
        params.put("condition", selected.getConditionFacture()+"");
        params.put("dateEcheance", formatDateFacture(selected.getDateEcheance())+"");
        params.put("raisonSocialeCLient", selected.getClient().getRaisonSociale()+"");
        params.put("adresseClient", selected.getClient().getAdresse()+"");
        params.put("RC", selected.getUtilisateur().getSociete().getRC()+"");
        params.put("activite", selected.getUtilisateur().getSociete().getActivitePrincipale()+"");
        params.put("adresse", selected.getUtilisateur().getSociete().getAdresse()+"");
        params.put("codePostale", selected.getUtilisateur().getSociete().getCodePostale()+"");
        params.put("pays", selected.getUtilisateur().getSociete().getPays()+"");
        params.put("ville", selected.getUtilisateur().getSociete().getVille()+"");
        params.put("ICE", selected.getUtilisateur().getSociete().getICE()+"");
        params.put("IF", selected.getUtilisateur().getSociete().getNumeroIF()+"");
        params.put("TP", selected.getUtilisateur().getSociete().getTP()+"");
        params.put("tel", selected.getUtilisateur().getSociete().getTelephone()+"");
        params.put("fax", selected.getUtilisateur().getSociete().getFax()+"");
        params.put("email", selected.getUtilisateur().getSociete().getEmail()+"");
        params.put("raisonSociale", selected.getUtilisateur().getSociete().getRaisonSociale()+"");
        params.put("siteWeb", selected.getUtilisateur().getSociete().getSiteWeb()+"");
    
        
         params.put("P_RC",RC);
        params.put("P_ICE",ICE);
         params.put("P_IF",IF);
         params.put("P_TP",TP);
             params.put("P_Tel",Tel);
          params.put("P_Fax",Fax);
          params.put("P_Email", Email);
          params.put("P_Site", Site);
          
        
        
        
        params.put("totalHT", selected.getSousTotal()+"");
        params.put("totalTVA", selected.getTotalTax()+"");
        params.put("totalTTC", selected.getNouveauSousTotal()+"");
        params.put("remiseFacture", selected.getRemise()+"");
        params.put("totalAvecRemise", selected.getTotalAvecRemise()+"");
        params.put("montantPaye", selected.getMontantPaye()+"");
        params.put("montantDu", selected.getMontantDu()+"");
          System.out.println("haaa client = "+selected.getClient().getRaisonSociale());
          System.out.println("haaa total ttc = "+selected.getSousTotal());
          System.out.println("haaa total tva = "+selected.getTotalTax());
          System.out.println("haaa montant du = "+selected.getMontantDu());
          System.out.println("haaa slected = "+getSelected());
          System.out.println("haaa factureItems= "+getSelected().getFactureItems());
          System.out.println("haaa size factureItems= "+findFactureItemsByFacture().size());
          
          System.out.println("haaa client= "+getSelected().getClient());
          System.out.println("haaa client= "+getSelected().getClient().getUtilisateur().getSociete());
          
          
        
        

        PdfUtil.generatePdf(findFactureItemsByFacture(), params, "facture-" + formatDateFacture(selected.getDateFacture()) +"/" +selected.getId() , "/jasper/ImprimerFacture.jasper");
    }
      
      
      
//      public void imprimerFactureItem(FactureItem factureItem) throws JRException, IOException {
//
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("article", factureItem.getProduit().getLibelle()+"");
//        params.put("description", factureItem.getProduit().getDescription()+"");
//        params.put("description", factureItem.getProduit().getDescription()+"");
//        params.put("TVA", factureItem.getTax()+"");
//        params.put("qte", factureItem.getQuantite()+"");
//        params.put("prixUnitaire", factureItem.getProduit().getPrixUnitaire()+"");
//        params.put("remise", factureItem.getRemise()+"");
//        params.put("montantHT", factureItem.getSousTotalHT()+"");
//        List<Client>list = new ArrayList<>();
//        list.add(factureItem.getFacture().getClient());
//    
//
//        PdfUtil.generatePdf(list, params, "factureItem-" + selected.getId() + "/" + selected.getDateFacture(), "/jasper/ImprimerFacture.jasper");
//
//      
//    }
      
   public BigDecimal getMontantTotalPayeDh(){
       if(SessionUtil.getConnectedUser()==null)return BigDecimal.ZERO;
       else return getFacade().getMontantTotalPayeDh(SessionUtil.getConnectedUser());
   }   
   public BigDecimal getMontantTotalNonPayeDh(){
       if(SessionUtil.getConnectedUser()==null)return BigDecimal.ZERO;
       else  return getFacade().getMontantTotalNonPayeDh(SessionUtil.getConnectedUser());
   }   
   
   public BigDecimal getMontantTotalEnRetardDh(){
       if(SessionUtil.getConnectedUser()==null)return BigDecimal.ZERO;
       else return getFacade().getMontantTotalEnRetardDh(SessionUtil.getConnectedUser());
   }   
   public BigDecimal getMontantTotalPayeEuro(){
       if(SessionUtil.getConnectedUser()==null)return BigDecimal.ZERO;
       else return getFacade().getMontantTotalPayeEuro(SessionUtil.getConnectedUser());
   }   
   public BigDecimal getMontantTotalNonPayeEuro(){
       if(SessionUtil.getConnectedUser()==null)return BigDecimal.ZERO;
       else  return getFacade().getMontantTotalNonPayeEuro(SessionUtil.getConnectedUser());
   }   
   
   public BigDecimal getMontantTotalEnRetardEuro(){
       if(SessionUtil.getConnectedUser()==null)return BigDecimal.ZERO;
       else return getFacade().getMontantTotalEnRetardEuro(SessionUtil.getConnectedUser());
   }   
   public BigDecimal getMontantTotalPayeDollar(){
       if(SessionUtil.getConnectedUser()==null)return BigDecimal.ZERO;
       else return getFacade().getMontantTotalPayeDollar(SessionUtil.getConnectedUser());
   }   
   public BigDecimal getMontantTotalNonPayeDollar(){
       if(SessionUtil.getConnectedUser()==null)return BigDecimal.ZERO;
       else  return getFacade().getMontantTotalNonPayeDollar(SessionUtil.getConnectedUser());
   }   
   
   public BigDecimal getMontantTotalEnRetardDollar(){
       if(SessionUtil.getConnectedUser()==null)return BigDecimal.ZERO;
       else return getFacade().getMontantTotalEnRetardDollar(SessionUtil.getConnectedUser());
   }   
  
   public long nbrFactureCrees(){
        if(SessionUtil.getConnectedUser()==null)return 0;
       else return getFacade().nbrFactureCrees(SessionUtil.getConnectedUser());
   }
   
   public long nbrFactureNonPayee(){
        if(SessionUtil.getConnectedUser()==null)return 0;
       else return getFacade().nbrFactureNonPayee(SessionUtil.getConnectedUser());
   }
   
   
   public long nbrFactureEnRetard(){
        if(SessionUtil.getConnectedUser()==null)return 0;
       else return getFacade().nbrFactureEnRetard(SessionUtil.getConnectedUser());
   }
   
   
   public long nbrFacturePayee(){
        if(SessionUtil.getConnectedUser()==null)return 0;
       else return getFacade().nbrFacturePayee(SessionUtil.getConnectedUser());
   }
   
   
   public long nbrProduit(){
        if(SessionUtil.getConnectedUser()==null)return 0;
       else return getFacade().nbrProduit(SessionUtil.getConnectedUser());
   }
   
   
   public long nbrClient(){
        if(SessionUtil.getConnectedUser()==null)return 0;
       else return getFacade().nbrClient(SessionUtil.getConnectedUser());
   }
   
   
   public long nbrFournisseur(){
        if(SessionUtil.getConnectedUser()==null)return 0;
       else return getFacade().nbrFournisseur(SessionUtil.getConnectedUser());
   }
   
   public long nbrUsers(){
        if(SessionUtil.getConnectedUser()==null)return 0;
       else return getFacade().nbrUsers(SessionUtil.getConnectedUser());
   }
   
   public BigDecimal getChiffreAffaireByAnnee(int annee){
       if(SessionUtil.getConnectedUser()==null)return BigDecimal.ZERO;
       else return getFacade().getChiffreAffaireByAnnee(SessionUtil.getConnectedUser(), annee);
   }
}

