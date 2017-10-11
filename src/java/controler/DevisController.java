package controler;

import controler.*;
import bean.Devis;
import bean.DevisItem;
import bean.Devis;
import bean.DevisItem;
import bean.Devis;
import bean.DevisItem;
import bean.Facture;
import bean.FactureItem;

import bean.Produit;
import bean.Utilisateur;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import controler.util.PdfUtil;
import controler.util.SessionUtil;
import java.io.IOException;
import service.DevisFacade;

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
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import net.sf.jasperreports.engine.JRException;
import service.DevisItemFacade;
import service.FactureFacade;
import service.FactureItemFacade;
import service.PaiementFacade;
import service.ProduitFacade;

@Named("devisController")
@SessionScoped
public class DevisController implements Serializable {

     @EJB
    private service.DevisFacade ejbFacade;
    private List<Devis> items = null;
    private Devis selected;
    private List<DevisItem> devisItems = null;
    private DevisItem devisItem;
    private DevisItem devisItem2;
    private Double quantitStock;
    private double prix;
    private Produit produit;
    private String etat="";
    private List<Devis> allDevis;
     private List<Produit> listProduitDevise = null;
          private List<Produit> listProduitDeviseEdit = null;

    
    private String recherche="";
    Utilisateur utilisateur;
    private Produit p = new Produit();
   
    @EJB
    DevisItemFacade devisItemFacade;
    @EJB
    ProduitFacade produitFacade;
    @EJB
    FactureFacade factureFacade;
    @EJB
    FactureItemFacade factureItemFacade;


    
    public void convertirToFacture(){
        Facture facture = new Facture();
        FactureItem factureItem = new FactureItem();
        facture.setClient(selected.getClient());
        facture.setDateFacture(selected.getDateEmission());
        facture.setDateEcheance(selected.getDateEcheance());
        facture.setConditionFacture(selected.getConditionDevis());
        facture.setDevise(selected.getDevise());
        List<FactureItem> list = new ArrayList();
       // System.out.println("haaaaaaaa siiiiiiize "+ findDevisItemsByDevis().size());
        for(int i = 0; i< findDevisItemsByDevis().size();i++){
            factureItem.setProduit(cloneProduit(findDevisItemsByDevis().get(i).getProduit()));
            factureItem.setQuantite(findDevisItemsByDevis().get(i).getQuantite());
            factureItem.setRemise(findDevisItemsByDevis().get(i).getRemise());
            factureItem.setTax(findDevisItemsByDevis().get(i).getTax());
            factureItem.setSousTotalHT(findDevisItemsByDevis().get(i).getSousTotalHT());
            //factureItem.setFacture(facture);
            list.add(factureItem);
            //selected.getDevisItems().remove(selected.getDevisItems().get(i));
            
            //modifier stock
            factureItem.getProduit().setQuantiteStock(factureItem.getProduit().getQuantiteStock() - factureItem.getQuantite());
            produitFacade.edit(factureItem.getProduit());
            devisItemFacade.remove(findDevisItemsByDevis().get(i));
     
        }
        facture.setMontantDu(selected.getMontantDu());
        facture.setMontantPaye(BigDecimal.ZERO);
        facture.setSousTotal(selected.getSousTotal());
        facture.setRemise(selected.getRemise());
        facture.setTotalTax(selected.getTotalTax());
        facture.setNouveauSousTotal(selected.getSousTotal().add(selected.getTotalTax()));
        facture.setTotalAvecRemise(facture.getNouveauSousTotal().subtract(selected.getRemise()));
        facture.setEtat("Non payée");
        facture.setUtilisateur(selected.getUtilisateur());
        factureFacade.create(facture);
        
//        for(int i = 0; i< selected.getDevisItems().size(); i++){
//            System.out.println("haaaa l items " + selected.getDevisItems().get(i).getProduit().getLibelle() );
//            devisItemFacade.remove(selected.getDevisItems().get(i));
//            //selected.getDevisItems().remove(getDevisItems().get(i));
//        }
       // getFacade().remove(selected);
        
        for(int i = 0; i< list.size();i++){
        list.get(i).setFacture(facture);
        factureItemFacade.create(list.get(i));
        

        }
       selected.getDevisItems().clear();
        getFacade().remove(selected);
         try {
                SessionUtil.redirect("/Gestion_commerciale/faces/facture/List.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(DevisController.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    }
    
     public void edite(){
        try {
            SessionUtil.redirect("/Gestion_commerciale/faces/devis/Edit.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
       public void redirectListe(){
        try {
            SessionUtil.redirect("/Gestion_commerciale/faces/devis/List.xhtml");
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
    
    

    public String getRecherche() {
        if (recherche == null) {
            recherche = "";
        }
        return recherche;
    }

    public void setRecherche(String recherche) {
        this.recherche = recherche;
    }

  public void calculerNouveauSousTotal(){
      if (getSelected().getRemise() != null) {
          //  System.out.println("dkheeel");
           // System.out.println(selected.getRemise());
            getSelected().setNouveauSousTotal(selected.getSousTotal().subtract(selected.getRemise()));
            getSelected().setMontantDu(selected.getNouveauSousTotal().add(selected.getTotalTax()));
           // System.out.println("khrej");
            if (selected.getClient() != null) {
                getFacade().edit(selected);
            }
        }
  }

   

    public List<Devis> getAllDevis() {
        
            Utilisateur u = SessionUtil.getConnectedUser();
            if (u != null) {
                allDevis = new ArrayList<>();
                allDevis = getFacade().listDevisSociete(u, recherche, etat);
            } else {        
                allDevis = getItems();
            }
       
        return allDevis;
    }

    public void setAllDevis(List<Devis> allDevis) {
        this.allDevis = allDevis;
    }

   
    
  

    public String getEtat() {
        if(etat == null) etat = "";
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

//    public void calculerSousTotal() {
//        if (getDevisItem().getQuantite() != null) {
//            BigDecimal total = BigDecimal.valueOf(getDevisItem().getQuantite() * getDevisItem().getProduit().getPrixUnitaire());
//            getDevisItem().setSousTotalHT(total);
//        } else {
//            getDevisItem().setSousTotalHT(BigDecimal.ZERO);
//        }
//    }
    public List<Devis> findDevissByEtat() {
        Utilisateur u = SessionUtil.getConnectedUser();
        setAllDevis(getFacade().findDevissByEtat(etat, u));
        return allDevis;
    }

    public List<Devis> findDevisByCritere() {
        Utilisateur u = SessionUtil.getConnectedUser();
        setAllDevis(getFacade().findDevisByCritere(recherche, u));
        return allDevis;
    }

    public List<Produit> getListProduitDevise() {
      Utilisateur utilisateur = SessionUtil.getConnectedUser();
        if(listProduitDevise==null){
            listProduitDevise = new ArrayList<>();
        }
           if(!selected.getDevise().equals(""))listProduitDevise = factureFacade.getProduitByDevise(selected.getDevise(), utilisateur);

        return listProduitDevise;
    }

    public void setListProduitDevise(List<Produit> listProduitDevise) {
        this.listProduitDevise = listProduitDevise;
    }

     public List<Produit> getListProduitDeviseEdit() {
          Utilisateur utilisateur = SessionUtil.getConnectedUser();
        if(listProduitDeviseEdit==null){
            listProduitDeviseEdit = new ArrayList<>();
            
        }
        listProduitDeviseEdit=factureFacade.getProduitByDevise(selected.getDevise(), utilisateur);
        return listProduitDeviseEdit;
    }
      public void setListProduitDeviseEdit(List<Produit> listProduitDevise) {
        this.listProduitDeviseEdit = listProduitDevise;
    }
  

  

    public String getColor(String etat) {
        String res = "";
       // System.out.println("haaa l etat" + etat);
        if (etat != null) {

            if (etat.equals("Brouillon")) {
                res = "#8bdc43";
            } else if(etat.equals("Envoyé")){
                res = "#FF9933";
            }else if(etat.equals("Consulté")){
                res = "#33cdae";
            }else if(etat.equals("Approuvé")){
                res = "#eea761";
            }else if(etat.equals("Rejeté")){
                res = "#f28150";
            }else if(etat.equals("Annulé")){
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
        double qteAvant = getQteByDevisItem();
     //   System.out.println("qte avant" + qteAvant);
        double qteApres = devisItem.getQuantite();
        //double res = (qteAvant - devisItem.getQuantite());
     //   System.out.println("qte aprés" + (qteApres - qteAvant));
        //modfier stock
        if((devisItem.getProduit().getQuantiteStock() + qteAvant) - qteApres >= 0){
        devisItem.getProduit().setQuantiteStock((devisItem.getProduit().getQuantiteStock() + qteAvant) - qteApres);

        devisItemFacade.edit(devisItem);
        produitFacade.edit(devisItem.getProduit());
        selected.setMontantDu(BigDecimal.ZERO);  
        selected.setTotalTax(BigDecimal.ZERO);
        selected.setNouveauSousTotal(BigDecimal.ZERO);
        selected.setSousTotal(BigDecimal.ZERO);
       // selected.setEtat("non payée");

        List<DevisItem> list = devisItemFacade.findDevisItemsByDevis(selected);
       // System.out.println("ha liste : " + list.size());
        for (int i = 0; i < list.size(); i++) {
            getSelected().setSousTotal(getSelected().getSousTotal().add(list.get(i).getSousTotalHT()));
            getSelected().setTotalTax(getSelected().getTotalTax().add(list.get(i).getSousTotalHT().multiply(BigDecimal.valueOf(list.get(i).getTax()))));
            getSelected().setNouveauSousTotal(getSelected().getSousTotal().add(getSelected().getTotalTax()));
            getSelected().setNouveauSousTotal(getSelected().getSousTotal().subtract(getSelected().getRemise()));
            getSelected().setMontantDu(getSelected().getNouveauSousTotal().add(getSelected().getTotalTax()));
        }
        getFacade().edit(getSelected());

    }else {
            JsfUtil.addErrorMessage("Opération echouée ! stock insuffisant");
        }
    }

    public Double getQteByDevisItem() {
        return getFacade().getQteByDevisItem(devisItem);
    }

    public void deleteDevisItem(DevisItem devisItem) {
        int res = devisItemFacade.removeDevisItem(devisItem, selected);
        if (res > 0) {
           // System.out.println("haa list f lwl=" + selected.getDevisItems().size());
            selected.getDevisItems().remove(devisItem);
           // System.out.println("haa list f lekher=" + selected.getDevisItems().size());

           // System.out.println("delete DevisItem new Montant=>" + selected.getMontantDu());
            JsfUtil.addSuccessMessage("Ligne de Devis Supprimé avec succes!!");
        } else {
            JsfUtil.addErrorMessage("Erreur lors de la suppression !!");
        }
    }

    public void calculerSousTotal() {
       // System.out.println("dkheeel");
        BigDecimal total = BigDecimal.ZERO;
//        if(getDevisItem().getProduit().getPrixUnitaire()==null)System.out.println("rah null asat");
//        System.out.println("haaa l qte "+ getDevisItem().getQuantite());
        if (getDevisItem().getQuantite() != null) {
            total = total.add(BigDecimal.valueOf((getDevisItem().getQuantite() * getDevisItem().getPrixDevise())));
        }
        if (getDevisItem().getRemise() != null) {
            total = total.subtract(getDevisItem().getRemise());
        }
        getDevisItem().setSousTotalHT(total);

    }
    public void calculerSousTotalService() {
       // System.out.println("dkheeel");
        BigDecimal total = BigDecimal.ZERO;
//        if(getFactureItem().getProduit().getPrixUnitaire()==null)System.out.println("rah null asat");
//        System.out.println("haaa l qte "+ getFactureItem().getQuantite());
        if (getDevisItem2().getQuantite() != null) {
            total = total.add(BigDecimal.valueOf((getDevisItem2().getQuantite() * getDevisItem2().getPrixDevise())));
        }
        if (getDevisItem2().getRemise() != null) {
            total = total.subtract(getDevisItem2().getRemise());
        }
        getDevisItem2().setSousTotalHT(total);

    }

    public void getQtePixByProduit() {
        setQuantitStock(produitFacade.getQteByProduit(devisItem.getProduit()));
        setPrix(produitFacade.getPrixByProduit(devisItem.getProduit(),selected.getDevise()));
        getDevisItem().setSousTotalHT(BigDecimal.ZERO);
        getDevisItem().setQuantite(0.0);
        getDevisItem().setRemise(BigDecimal.ZERO);
        getDevisItem().setTax(null);
    }
    
    public void getProduitByDevise(){
        System.out.println("dkheeeeel l getProduitByDevise");
        Utilisateur utilisateur = SessionUtil.getConnectedUser();
       setListProduitDevise(factureFacade.getProduitByDevise(selected.getDevise(), utilisateur));
       //factureItem.setProduit(null);
         setQuantitStock(0.0);
         setPrix(0);
         
       
       
    }

    public void addToDevis() {
        //devisItemFacade.addDevisItemToItem(getDevisItem(), selected);
        if (getDevisItem().getProduit() == null) {
            JsfUtil.addErrorMessage("Veuillez choisir un produit");
        } else if (getDevisItem().getQuantite() > getDevisItem().getProduit().getQuantiteStock()) {
            JsfUtil.addErrorMessage("La quantitéte spécifier est superieur à la quantité restée en stock merçi de la verifier");
        } else if (getDevisItem().getQuantite() <= 0) {
            JsfUtil.addErrorMessage("La quantitéte saisie est invalide");
        } else if (getDevisItem().getRemise().signum() == -1) {
            JsfUtil.addErrorMessage("Remise doit étre supérieur à 0Dh");
        } else if (getDevisItem().getRemise().compareTo(devisItem.getSousTotalHT()) == 1) {
            JsfUtil.addErrorMessage("Le remise saisi est supérieur au total");
        } else if (produitExist(getDevisItem(), getDevisItems())) {
            JsfUtil.addErrorMessage("Article déja choisi");
        } else {
            getDevisItems().add(clone(getDevisItem()));
             for (int i = 0; i < devisItems.size(); i++) {
           System.out.println("haaa les ref kamlin" + devisItems.get(i).getProduit().getReference());
             }
            //modifier les stock
            // getDevisItem().getProduit().setQuantiteStock(getDevisItem().getProduit().getQuantiteStock() - getDevisItem().getQuantite());
            //produitFacade.edit(getDevisItem().getProduit());
//            for (int i = 0; i < getDevisItems().size(); i++) {
//                getSelected().setSousTotal(getSelected().getSousTotal().add(getDevisItems().get(i).getSousTotalHT()));
//                getSelected().setTotalTax(getSelected().getTotalTax().add(getDevisItems().get(i).getSousTotalHT().multiply(BigDecimal.valueOf(getDevisItems().get(i).getTax()))));
//            }

            getSelected().setSousTotal(getSelected().getSousTotal().add(getDevisItem().getSousTotalHT()));
            getSelected().setTotalTax(getSelected().getTotalTax().add(getDevisItem().getSousTotalHT().multiply(BigDecimal.valueOf(getDevisItem().getTax()/100))));

            getSelected().setNouveauSousTotal(getSelected().getSousTotal().subtract(getSelected().getRemise()));
            getSelected().setMontantDu(getSelected().getNouveauSousTotal().add(getSelected().getTotalTax()));

            setDevisItem(null);
            quantitStock = 0.0;
            prix = 0;
            p = new Produit();
            getDevisItem().setRemise(BigDecimal.ZERO);
            //getDevisItem().getProduit().setReference(-1);
          //  devisItem = new DevisItem();
  

            //System.out.println(getDevisItems().size() + "");

        }

    }
    
    
        //------------------------------test
    
        public void addServiceToDevis() {
        //devisItemFacade.addDevisItemToItem(getDevisItem(), selected);
        if (getDevisItem2().getProduit().getReference() == null) {
            JsfUtil.addErrorMessage("Veuillez entrer la réference du produit"); 
        }else if(getDevisItem2().getProduit().getLibelle().equals("")){
           // System.out.println("haaaaaaa smiyaaaaaa" + getDevisItem2().getProduit().getLibelle());
           JsfUtil.addErrorMessage("Veuillez entrer le nom du produit"); 
            } else if (getDevisItem2().getQuantite() == null) {
            JsfUtil.addErrorMessage("Veuillez entrer la quantité voulu");
        } else if (getDevisItem2().getQuantite() <= 0) {
            JsfUtil.addErrorMessage("La quantitéte doit étre supérieur à 0");
        }else if(getDevisItem2().getPrixDevise()==0.0){
          JsfUtil.addErrorMessage("Veuillez entrer le prix unitaire");
        } else if (getDevisItem2().getRemise().signum() == -1) {
            JsfUtil.addErrorMessage("Remise doit étre supérieur à 0Dh");
        } else if (getDevisItem2().getRemise().compareTo(devisItem.getSousTotalHT()) == 1) {
            JsfUtil.addErrorMessage("Le remise saisi est supérieur au total");
        } else if (produitExist(getDevisItem2(), getDevisItems())) {
            JsfUtil.addErrorMessage("Article déja choisi");
        }  else if (getSelected().getDevise().equals("")) {
            JsfUtil.addErrorMessage("Veuillez choisir un devise");
        } else {
            getDevisItems().add(clone(getDevisItem2()));
             for (int i = 0; i < devisItems.size(); i++) {
           System.out.println("haaa les ref kamlin" + devisItems.get(i).getProduit().getReference());
             }
            //modifier les stock
            // getDevisItem().getProduit().setQuantiteStock(getDevisItem().getProduit().getQuantiteStock() - getDevisItem().getQuantite());
            //produitFacade.edit(getDevisItem().getProduit());
//            for (int i = 0; i < getDevisItems().size(); i++) {
//                getSelected().setSousTotal(getSelected().getSousTotal().add(getDevisItems().get(i).getSousTotalHT()));
//                getSelected().setTotalTax(getSelected().getTotalTax().add(getDevisItems().get(i).getSousTotalHT().multiply(BigDecimal.valueOf(getDevisItems().get(i).getTax()))));
//            }

             getSelected().setSousTotal(getSelected().getSousTotal().add(getDevisItem().getSousTotalHT()));
            getSelected().setTotalTax(getSelected().getTotalTax().add(getDevisItem().getSousTotalHT().multiply(BigDecimal.valueOf(getDevisItem().getTax()/100))));

            getSelected().setNouveauSousTotal(getSelected().getSousTotal().subtract(getSelected().getRemise()));
            getSelected().setMontantDu(getSelected().getNouveauSousTotal().add(getSelected().getTotalTax()));

            setDevisItem2(null);
            quantitStock = 0.0;
            prix = 0;
            p = new Produit();
            getDevisItem().setRemise(BigDecimal.ZERO);
            //getDevisItem().getProduit().setReference(-1);
          //  devisItem = new DevisItem();
  

            //System.out.println(getDevisItems().size() + "");

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
    
    
    
    
    

    public void addItemToDevisEdit() {
        //devisItemFacade.addDevisItemToItem(getDevisItem(), selected);
        if (getDevisItem().getProduit() == null) {
            JsfUtil.addErrorMessage("Veuillez choisir un produit");
        } else if (getDevisItem().getQuantite() > getDevisItem().getProduit().getQuantiteStock()) {
            JsfUtil.addErrorMessage("Stock insuffisant");
        } else if (getDevisItem().getQuantite() <= 0) {
            JsfUtil.addErrorMessage("La quantitéte saisie est invalide");
        } else if (getDevisItem().getRemise().signum() == -1) {
            JsfUtil.addErrorMessage("Remise doit étre supérieur à 0Dh");
        } else if (getDevisItem().getRemise().compareTo(devisItem.getSousTotalHT()) == 1) {
            JsfUtil.addErrorMessage("Le remise saisi est supérieur au total");
        } else if (produitExist(devisItem, selected.getDevisItems())) {
            JsfUtil.addErrorMessage("Ligne de devis déja existée");
        } else {
            getDevisItems().add(clone(getDevisItem()));
            //modifier les stock
            // getDevisItem().getProduit().setQuantiteStock(getDevisItem().getProduit().getQuantiteStock() - getDevisItem().getQuantite());
            //produitFacade.edit(getDevisItem().getProduit());
//            for (int i = 0; i < getDevisItems().size(); i++) {
//                getSelected().setSousTotal(getSelected().getSousTotal().add(getDevisItems().get(i).getSousTotalHT()));
//                getSelected().setTotalTax(getSelected().getTotalTax().add(getDevisItems().get(i).getSousTotalHT().multiply(BigDecimal.valueOf(getDevisItems().get(i).getTax()))));
//            }

            getSelected().setSousTotal(getSelected().getSousTotal().add(getDevisItem().getSousTotalHT()));
            getSelected().setTotalTax(getSelected().getTotalTax().add(getDevisItem().getSousTotalHT().multiply(BigDecimal.valueOf(getDevisItem().getTax()/100))));

            getSelected().setNouveauSousTotal(getSelected().getSousTotal().subtract(getSelected().getRemise()));
            getSelected().setMontantDu(getSelected().getNouveauSousTotal().add(getSelected().getTotalTax()));

            getFacade().edit(selected);
            devisWithItems(selected, getDevisItems());
            JsfUtil.addSuccessMessage("Ligne de devis ajoutée avec succès");
            getDevisItems().clear();
            devisItem = null;
            quantitStock = 0.0;
            prix = 0;
            //allFacures = null;

           // System.out.println(getDevisItems().size() + "");

        }
    }
    

    

    
    
    
    
    
    
    
    public Boolean produitExist(DevisItem devisItem, List<DevisItem> devisItems) {
        Boolean exist = false;
          System.out.println("haaa ref dyal artcile jdid "+ devisItem.getProduit().getReference());
        for (int i = 0; i < devisItems.size(); i++) {
            System.out.println("haaa ref dyal artcile l9dam "+ devisItems.get(i).getProduit().getReference());
            if (getDevisItem().getProduit().getReference() == devisItems.get(i).getProduit().getReference()) {
                exist = true;
            }

        }
        return exist;
    }

    public void enregisterDevis() {
       // System.out.println(getDevisItems().size() + "");

        if (selected.getClient() == null) {
            JsfUtil.addErrorMessage("Veuillez sélectionner un client");
        } else if (selected.getDateEmission()== null) {
            JsfUtil.addErrorMessage("Veuillez choisir la date de la devis");
        } else if (selected.getDateEcheance() == null) {
            JsfUtil.addErrorMessage("Veuillez choisir la date d'échenace de la devis");
        } else if (selected.getDateEcheance().after(selected.getDateEcheance())) {
            JsfUtil.addErrorMessage("Veuillez vérifier les dates saisies");
        } else if (getDevisItems().isEmpty()) {
            JsfUtil.addErrorMessage("Veuillez ajouter des articles");
        } else {
            selected.setEtat("Brouillon");
            selected.setUtilisateur(SessionUtil.getConnectedUser());
            getFacade().create(selected);
            devisWithItems(selected, getDevisItems());
            
            setAllDevis(getFacade().listDevisSociete(SessionUtil.getConnectedUser(),recherche,etat));

            JsfUtil.addSuccessMessage("Devis enregistrée avec succès");
            selected = null;
            setEtat(null);
            getDevisItems().clear();
            try {
                SessionUtil.redirect("List.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(DevisController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public void AnnulerDevis() {
//        for (int i = 0; i < devisItems.size(); i++) {
//            devisItems.get(i).getProduit().setQuantiteStock(devisItems.get(i).getProduit().getQuantiteStock() + devisItems.get(i).getQuantite());
//            produitFacade.edit(devisItems.get(i).getProduit());
//        }
        //selected = null;
        getDevisItems().clear();
        try {
            SessionUtil.redirect("List.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(DevisController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   

    public DevisItem clone(DevisItem devisItem) {
        DevisItem clone = new DevisItem();
        clone.setDevis(devisItem.getDevis());
        clone.setProduit(devisItem.getProduit());
        clone.setQuantite(devisItem.getQuantite());
        clone.setTax(devisItem.getTax());
        clone.setRemise(devisItem.getRemise());
        clone.setPrixDevise(devisItem.getPrixDevise());
        clone.setSousTotalHT(devisItem.getSousTotalHT());
        return clone;
    }
    public Produit cloneProduit(Produit produit) {
        Produit clone = new Produit();
     clone.setReference(produit.getReference());
     clone.setUtilisateur(produit.getUtilisateur());
     clone.setDescription(produit.getDescription());
     clone.setFournisseur(produit.getFournisseur());
     clone.setLibelle(produit.getLibelle());
     clone.setPrixDirham(produit.getPrixDirham());
     clone.setQuantiteStock(produit.getQuantiteStock());
     
        return clone;
    }

 

    public String formatDateDevis(Date date) {
       // System.out.println("dkheeeeeeeeeeeeeeeeeeeeeeeeeeeel");
       // System.out.println("haaa date" + date.toString());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        String currentTime = simpleDateFormat.format(date);
       // System.out.println("khreeeeeej");
        return currentTime;

    }



    public List<DevisItem> findDevisItemsByDevis() {
        return devisItemFacade.findDevisItemsByDevis(selected);
    }

    public int maxIdDevis() {
        return getFacade().generateId("Devis", "id") + 1;

    }

    public DevisItem getDevisItem() {
        if (devisItem == null) {
            devisItem = new DevisItem();
            devisItem.setSousTotalHT(BigDecimal.ZERO);
            devisItem.setRemise(BigDecimal.ZERO);
            devisItem.setProduit(new Produit());
            devisItem.getProduit().setPrixDirham(0.0);
            devisItem.getProduit().setQuantiteStock(0.0);
            devisItem.setPrixDevise(0.0);
        }
        return devisItem;
    }
    
    //--------------test
    public DevisItem getDevisItem2() {
         if (devisItem == null) {
            devisItem = new DevisItem();
            devisItem.setSousTotalHT(BigDecimal.ZERO);
            devisItem.setRemise(BigDecimal.ZERO);
            devisItem.setProduit(new Produit());
            devisItem.getProduit().setPrixDirham(0.0);
            devisItem.getProduit().setQuantiteStock(0.0);
            devisItem.setPrixDevise(0.0);
        }
        return devisItem;
    }
     public void setDevisItem2(DevisItem devisItem) {
        this.devisItem = devisItem;
    }
    
//------------------------------------------
    public void setDevisItem(DevisItem devisItem) {
        this.devisItem = devisItem;
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

    public List<DevisItem> getDevisItems() {
        if (devisItems == null) {
            devisItems = new ArrayList<>();
        }
        return devisItems;
    }

    public void setDevisItems(List<DevisItem> devisItems) {
        this.devisItems = devisItems;
    }

    public void devisWithItems(Devis devis, List<DevisItem> devisItems) {
        for (int i = 0; i < devisItems.size(); i++) {
            devisItems.get(i).setDevis(devis);
            if(produitExist( getDevisItems().get(i).getProduit().getReference())==false){
            produitFacade.create(getDevisItems().get(i).getProduit());
            }else{
                  
            //getDevisItems().get(i).getProduit().setQuantiteStock(getDevisItems().get(i).getProduit().getQuantiteStock() - 0);
            produitFacade.edit(getDevisItems().get(i).getProduit());
         
            }
            devisItemFacade.create(devisItems.get(i));
          
            //modifier stock
         
        }
    }
    
    public Boolean produitExist(Integer reference){
        List<Produit> p;
        p=produitFacade.produitExist(reference);
        if(p == null)return false;
        else return true;
    }
    
   
    
     public void imprimerDevis() throws JRException, IOException {
          Calendar calendar = Calendar.getInstance();
          calendar.setTime(selected.getDateEmission());
          int annee = calendar.get(calendar.YEAR);
          int mois = calendar.get(calendar.MONTH);
                    String RC="", ICE= "" ,IF="" ,Patente="" ,TP="" ,Tel="" ,Fax="" ,Email="" ,Site = "";

//          System.out.println("haaa  l id = "+ selected.getId());
//          System.out.println("haaa  l date = "+ selected.getDateFacture());
//          System.out.println("haaa  l RC  = "+ selected.getUtilisateur().getSociete().getRaisonSociale());
//          System.out.println("haaa  l client = "+ selected.getClient().getRaisonSociale());
//          System.out.println("haaa  l montant du = "+ selected.getMontantDu());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("numeroDevis", "#0"+mois+""+annee+"/"+selected.getId());
        params.put("dateDevis", formatDateDevis(selected.getDateEmission())+"");
        params.put("devise", selected.getDevise()+"");
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
                params.put("condition", selected.getConditionDevis()+"");

        params.put("dateEcheance", formatDateDevis(selected.getDateEcheance())+"");
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
        params.put("P_RC", RC);
         params.put("P_ICE", ICE);
         params.put("P_IF", IF);
         params.put("P_TP", TP);
         params.put("P_Patente", Patente);
         params.put("P_Tel", Tel);
         params.put("P_Fax", Fax);
         params.put("P_Email", Email);
         params.put("P_Site", Site);
              params.put("logo", "C://uploads//" + SessionUtil.getConnectedUser().getSociete().getLogo()+"");

        
        
        
        params.put("totalHT", selected.getSousTotal()+"");
        params.put("totalTVA", selected.getTotalTax()+"");
        params.put("totalTTC", selected.getNouveauSousTotal()+"");
        params.put("remiseDevis", selected.getRemise()+"");       
        params.put("montantDu", selected.getMontantDu()+"");
        
        

        PdfUtil.generatePdf(findDevisItemsByDevis(), params, "devis-" + formatDateDevis(selected.getDateEmission()) +"/" +selected.getId() , "/jasper/ImprimerDevis.jasper");
    }
      
    
    
    
    
    
    
    
    public DevisController() {
    }

     public Devis getSelected() {
        if (selected == null) {
            selected = new Devis();
            //selected.setClient(new Client());
            selected.setDateEmission(new Date());
            selected.setRemise(BigDecimal.ZERO);
            selected.setMontantDu(BigDecimal.ZERO);
          
            selected.setTotalTax(BigDecimal.ZERO);
           
            selected.setNouveauSousTotal(BigDecimal.ZERO);
            selected.setSousTotal(BigDecimal.ZERO);
        }

        return selected;
    }

    public void setSelected(Devis selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private DevisFacade getFacade() {
        return ejbFacade;
    }

    public Devis prepareCreate() {
        selected = new Devis();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("DevisCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("DevisUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("DevisDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Devis> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
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
                    while(findDevisItemsByDevis().size()>0){
                    for(int i = 0; i<findDevisItemsByDevis().size();i++){
                        devisItemFacade.remove(findDevisItemsByDevis().get(i));
                    }
                    }
                      //Thread.sleep(1000);
                    getFacade().remove(selected);
                    SessionUtil.redirect("List.xhtml");
                    setAllDevis(getFacade().listDevisSociete(SessionUtil.getConnectedUser(),recherche,etat));
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

    public Devis getDevis(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Devis> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Devis> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Devis.class)
    public static class DevisControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DevisController controller = (DevisController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "devisController");
            return controller.getDevis(getKey(value));
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
            if (object instanceof Devis) {
                Devis o = (Devis) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Devis.class.getName()});
                return null;
            }
        }

    }

}
