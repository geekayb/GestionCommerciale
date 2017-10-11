package controler;

import controler.*;
import bean.Livraison;
import bean.LivraisonItem;
import bean.Livraison;
import bean.LivraisonItem;
import bean.Livraison;
import bean.LivraisonItem;
import bean.Facture;
import bean.FactureItem;

import bean.Produit;
import bean.Utilisateur;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import controler.util.PdfUtil;
import controler.util.SessionUtil;
import java.io.IOException;
import service.LivraisonFacade;

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
import service.LivraisonItemFacade;
import service.FactureFacade;
import service.FactureItemFacade;
import service.PaiementFacade;
import service.ProduitFacade;

@Named("livraisonController")
@SessionScoped
public class LivraisonController implements Serializable {

     @EJB
    private service.LivraisonFacade ejbFacade;
    private List<Livraison> items = null;
    private Livraison selected;
    private List<LivraisonItem> livraisonItems = null;
    private LivraisonItem livraisonItem;
    private LivraisonItem livraisonItem2;
    private Double quantitStock;
    private double prix;
    private Produit produit;
    private String etat="";
    private List<Livraison> allLivraison;
         private List<Produit> listProduitLivraison = null;
          private List<Produit> listProduitLivraisonEdit = null;
    
    private String recherche="";
    Utilisateur utilisateur;
    private Produit p = new Produit();
   
    @EJB
    LivraisonItemFacade livraisonItemFacade;
    @EJB
    ProduitFacade produitFacade;
    @EJB
    FactureFacade factureFacade;
    @EJB
    FactureItemFacade factureItemFacade;


    
    
    
     public void edite(){
        try {
            SessionUtil.redirect("/Gestion_commerciale/faces/livraison/Edit.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
       public void redirectListe(){
        try {
            SessionUtil.redirect("/Gestion_commerciale/faces/livraison/List.xhtml");
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

   

    public List<Livraison> getAllLivraison() {
        
            Utilisateur u = SessionUtil.getConnectedUser();
            if (u != null) {
                allLivraison = new ArrayList<>();
                allLivraison = getFacade().listLivraisonSociete(u, recherche, etat);
            } else {        
                allLivraison = getItems();
            }
       
        return allLivraison;
    }

    public void setAllLivraison(List<Livraison> allLivraison) {
        this.allLivraison = allLivraison;
    }

    public List<Produit> getListProduitDevise() {
      Utilisateur utilisateur = SessionUtil.getConnectedUser();
        if(listProduitLivraison==null){
            listProduitLivraison = new ArrayList<>();
        }
           if(!selected.getDevise().equals(""))listProduitLivraison = factureFacade.getProduitByDevise(selected.getDevise(), utilisateur);

        return listProduitLivraison;
    }

    public void setListProduitDevise(List<Produit> listProduitDevise) {
        this.listProduitLivraison = listProduitDevise;
    }

     public List<Produit> getListProduitDeviseEdit() {
          Utilisateur utilisateur = SessionUtil.getConnectedUser();
        if(listProduitLivraisonEdit==null){
            listProduitLivraisonEdit = new ArrayList<>();
            
        }
        listProduitLivraisonEdit=factureFacade.getProduitByDevise(selected.getDevise(), utilisateur);
        return listProduitLivraisonEdit;
    }
      public void setListProduitDeviseEdit(List<Produit> listProduitDevise) {
        this.listProduitLivraisonEdit = listProduitDevise;
    }
    
       public void getProduitByDevise(){
        System.out.println("dkheeeeel l getProduitByDevise");
        Utilisateur utilisateur = SessionUtil.getConnectedUser();
       setListProduitDevise(factureFacade.getProduitByDevise(selected.getDevise(), utilisateur));
       //factureItem.setProduit(null);
         setQuantitStock(0.0);
         setPrix(0);
         
       
       
    }
  

    public String getEtat() {
        if(etat == null) etat = "";
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

//    public void calculerSousTotal() {
//        if (getLivraisonItem().getQuantite() != null) {
//            BigDecimal total = BigDecimal.valueOf(getLivraisonItem().getQuantite() * getLivraisonItem().getProduit().getPrixUnitaire());
//            getLivraisonItem().setSousTotalHT(total);
//        } else {
//            getLivraisonItem().setSousTotalHT(BigDecimal.ZERO);
//        }
//    }
    public List<Livraison> findLivraisonsByEtat() {
        Utilisateur u = SessionUtil.getConnectedUser();
        setAllLivraison(getFacade().findLivraisonsByEtat(etat, u));
        return allLivraison;
    }

    public List<Livraison> findLivraisonByCritere() {
        Utilisateur u = SessionUtil.getConnectedUser();
        setAllLivraison(getFacade().findLivraisonByCritere(recherche, u));
        return allLivraison;
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
        double qteAvant = getQteByLivraisonItem();
     //   System.out.println("qte avant" + qteAvant);
        double qteApres = livraisonItem.getQuantite();
        //double res = (qteAvant - livraisonItem.getQuantite());
     //   System.out.println("qte aprés" + (qteApres - qteAvant));
        //modfier stock
        if((livraisonItem.getProduit().getQuantiteStock() + qteAvant) - qteApres >= 0){
        livraisonItem.getProduit().setQuantiteStock((livraisonItem.getProduit().getQuantiteStock() + qteAvant) - qteApres);

        livraisonItemFacade.edit(livraisonItem);
        produitFacade.edit(livraisonItem.getProduit());
        selected.setMontantDu(BigDecimal.ZERO);  
        selected.setTotalTax(BigDecimal.ZERO);
        selected.setNouveauSousTotal(BigDecimal.ZERO);
        selected.setSousTotal(BigDecimal.ZERO);
       // selected.setEtat("non payée");

        List<LivraisonItem> list = livraisonItemFacade.findLivraisonItemsByLivraison(selected);
       // System.out.println("ha liste : " + list.size());
        for (int i = 0; i < list.size(); i++) {
            getSelected().setSousTotal(getSelected().getSousTotal().add(list.get(i).getSousTotalHT()));
            getSelected().setTotalTax(getSelected().getTotalTax().add(list.get(i).getSousTotalHT().multiply(BigDecimal.valueOf(list.get(i).getTax()/100))));
            getSelected().setNouveauSousTotal(getSelected().getSousTotal().add(getSelected().getTotalTax()));
            getSelected().setNouveauSousTotal(getSelected().getSousTotal().subtract(getSelected().getRemise()));
            getSelected().setMontantDu(getSelected().getNouveauSousTotal().add(getSelected().getTotalTax()));
        }
        getFacade().edit(getSelected());

    }else {
            JsfUtil.addErrorMessage("Opération echouée ! stock insuffisant");
        }
    }

    public Double getQteByLivraisonItem() {
        return getFacade().getQteByLivraisonItem(livraisonItem);
    }

    public void deleteLivraisonItem(LivraisonItem livraisonItem) {
        int res = livraisonItemFacade.removeLivraisonItem(livraisonItem, selected);
        if (res > 0) {
           // System.out.println("haa list f lwl=" + selected.getLivraisonItems().size());
            selected.getLivraisonItems().remove(livraisonItem);
           // System.out.println("haa list f lekher=" + selected.getLivraisonItems().size());

           // System.out.println("delete LivraisonItem new Montant=>" + selected.getMontantDu());
            JsfUtil.addSuccessMessage("Ligne de Livraison Supprimé avec succes!!");
        } else {
            JsfUtil.addErrorMessage("Erreur lors de la suppression !!");
        }
    }

    public void calculerSousTotal() {
       // System.out.println("dkheeel");
        BigDecimal total = BigDecimal.ZERO;
//        if(getLivraisonItem().getProduit().getPrixUnitaire()==null)System.out.println("rah null asat");
//        System.out.println("haaa l qte "+ getLivraisonItem().getQuantite());
        if (getLivraisonItem().getQuantite() != null) {
            total = total.add(BigDecimal.valueOf((getLivraisonItem().getQuantite() * getLivraisonItem().getPrixDevise())));
        }
        if (getLivraisonItem().getRemise() != null) {
            total = total.subtract(getLivraisonItem().getRemise());
        }
        getLivraisonItem().setSousTotalHT(total);

    }
      public void calculerSousTotalService() {
       // System.out.println("dkheeel");
        BigDecimal total = BigDecimal.ZERO;
//        if(getFactureItem().getProduit().getPrixUnitaire()==null)System.out.println("rah null asat");
//        System.out.println("haaa l qte "+ getFactureItem().getQuantite());
        if (getLivraisonItem2().getQuantite() != null) {
            total = total.add(BigDecimal.valueOf((getLivraisonItem2().getQuantite() * getLivraisonItem2().getPrixDevise())));
        }
        if (getLivraisonItem2().getRemise() != null) {
            total = total.subtract(getLivraisonItem2().getRemise());
        }
        getLivraisonItem2().setSousTotalHT(total);

    }

    public void getQtePixByProduit() {
        setQuantitStock(produitFacade.getQteByProduit(livraisonItem.getProduit()));
        setPrix(produitFacade.getPrixByProduit(livraisonItem.getProduit(), selected.getDevise()));
        getLivraisonItem().setSousTotalHT(BigDecimal.ZERO);
        getLivraisonItem().setQuantite(0.0);
        getLivraisonItem().setRemise(BigDecimal.ZERO);
        getLivraisonItem().setTax(null);
    }

    public void addToLivraison() {
        //livraisonItemFacade.addLivraisonItemToItem(getLivraisonItem(), selected);
        if (getLivraisonItem().getProduit() == null) {
            JsfUtil.addErrorMessage("Veuillez choisir un produit");
        } else if (getLivraisonItem().getQuantite() > getLivraisonItem().getProduit().getQuantiteStock()) {
            JsfUtil.addErrorMessage("La quantitéte spécifier est superieur à la quantité restée en stock merçi de la verifier");
        } else if (getLivraisonItem().getQuantite() <= 0) {
            JsfUtil.addErrorMessage("La quantitéte saisie est invalide");
        } else if (getLivraisonItem().getRemise().signum() == -1) {
            JsfUtil.addErrorMessage("Remise doit étre supérieur à 0Dh");
        } else if (getLivraisonItem().getRemise().compareTo(livraisonItem.getSousTotalHT()) == 1) {
            JsfUtil.addErrorMessage("Le remise saisi est supérieur au total");
        } else if (produitExist(getLivraisonItem(), getLivraisonItems())) {
            JsfUtil.addErrorMessage("Article déja choisi");
        } else {
            getLivraisonItems().add(clone(getLivraisonItem()));
             for (int i = 0; i < livraisonItems.size(); i++) {
           System.out.println("haaa les ref kamlin" + livraisonItems.get(i).getProduit().getReference());
             }
            //modifier les stock
            // getLivraisonItem().getProduit().setQuantiteStock(getLivraisonItem().getProduit().getQuantiteStock() - getLivraisonItem().getQuantite());
            //produitFacade.edit(getLivraisonItem().getProduit());
//            for (int i = 0; i < getLivraisonItems().size(); i++) {
//                getSelected().setSousTotal(getSelected().getSousTotal().add(getLivraisonItems().get(i).getSousTotalHT()));
//                getSelected().setTotalTax(getSelected().getTotalTax().add(getLivraisonItems().get(i).getSousTotalHT().multiply(BigDecimal.valueOf(getLivraisonItems().get(i).getTax()))));
//            }

            getSelected().setSousTotal(getSelected().getSousTotal().add(getLivraisonItem().getSousTotalHT()));
            getSelected().setTotalTax(getSelected().getTotalTax().add(getLivraisonItem().getSousTotalHT().multiply(BigDecimal.valueOf(getLivraisonItem().getTax()/100))));

            getSelected().setNouveauSousTotal(getSelected().getSousTotal().subtract(getSelected().getRemise()));
            getSelected().setMontantDu(getSelected().getNouveauSousTotal().add(getSelected().getTotalTax()));

            setLivraisonItem(null);
            quantitStock = 0.0;
            prix = 0;
            p = new Produit();
            getLivraisonItem().setRemise(BigDecimal.ZERO);
            //getLivraisonItem().getProduit().setReference(-1);
          //  livraisonItem = new LivraisonItem();
  

            //System.out.println(getLivraisonItems().size() + "");

        }

    }
    
    
        //------------------------------test
    
        public void addServiceToLivraison() {
        //livraisonItemFacade.addLivraisonItemToItem(getLivraisonItem(), selected);
        if (getLivraisonItem2().getProduit().getReference() == null) {
            JsfUtil.addErrorMessage("Veuillez entrer la réference du produit"); 
        }else if(getLivraisonItem2().getProduit().getLibelle().equals("")){
           // System.out.println("haaaaaaa smiyaaaaaa" + getLivraisonItem2().getProduit().getLibelle());
           JsfUtil.addErrorMessage("Veuillez entrer le nom du produit"); 
            } else if (getLivraisonItem2().getQuantite() == null) {
            JsfUtil.addErrorMessage("Veuillez entrer la quantité voulu");
        } else if (getLivraisonItem2().getQuantite() <= 0) {
            JsfUtil.addErrorMessage("La quantitéte doit étre supérieur à 0");
        }else if(getLivraisonItem2().getPrixDevise()==0.0){
          JsfUtil.addErrorMessage("Veuillez entrer le prix unitaire");
        } else if (getLivraisonItem2().getRemise().signum() == -1) {
            JsfUtil.addErrorMessage("Remise doit étre supérieur à 0Dh");
        } else if (getLivraisonItem2().getRemise().compareTo(livraisonItem.getSousTotalHT()) == 1) {
            JsfUtil.addErrorMessage("Le remise saisi est supérieur au total");
        } else if (produitExist(getLivraisonItem2(), getLivraisonItems())) {
            JsfUtil.addErrorMessage("Article déja choisi");
        }  else if (getSelected().getDevise().equals("")) {
            JsfUtil.addErrorMessage("Veuillez choisir un devise");
        } else {
            getLivraisonItems().add(clone(getLivraisonItem2()));
             for (int i = 0; i < livraisonItems.size(); i++) {
           System.out.println("haaa les ref kamlin" + livraisonItems.get(i).getProduit().getReference());
             }
            //modifier les stock
            // getLivraisonItem().getProduit().setQuantiteStock(getLivraisonItem().getProduit().getQuantiteStock() - getLivraisonItem().getQuantite());
            //produitFacade.edit(getLivraisonItem().getProduit());
//            for (int i = 0; i < getLivraisonItems().size(); i++) {
//                getSelected().setSousTotal(getSelected().getSousTotal().add(getLivraisonItems().get(i).getSousTotalHT()));
//                getSelected().setTotalTax(getSelected().getTotalTax().add(getLivraisonItems().get(i).getSousTotalHT().multiply(BigDecimal.valueOf(getLivraisonItems().get(i).getTax()))));
//            }

             getSelected().setSousTotal(getSelected().getSousTotal().add(getLivraisonItem().getSousTotalHT()));
            getSelected().setTotalTax(getSelected().getTotalTax().add(getLivraisonItem().getSousTotalHT().multiply(BigDecimal.valueOf(getLivraisonItem().getTax()/100))));

            getSelected().setNouveauSousTotal(getSelected().getSousTotal().subtract(getSelected().getRemise()));
            getSelected().setMontantDu(getSelected().getNouveauSousTotal().add(getSelected().getTotalTax()));

            setLivraisonItem2(null);
            quantitStock = 0.0;
            prix = 0;
            p = new Produit();
            getLivraisonItem().setRemise(BigDecimal.ZERO);
            //getLivraisonItem().getProduit().setReference(-1);
          //  livraisonItem = new LivraisonItem();
  

            //System.out.println(getLivraisonItems().size() + "");

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
    
    
    
    
    

    public void addItemToLivraisonEdit() {
        //livraisonItemFacade.addLivraisonItemToItem(getLivraisonItem(), selected);
        if (getLivraisonItem().getProduit() == null) {
            JsfUtil.addErrorMessage("Veuillez choisir un produit");
        } else if (getLivraisonItem().getQuantite() > getLivraisonItem().getProduit().getQuantiteStock()) {
            JsfUtil.addErrorMessage("Stock insuffisant");
        } else if (getLivraisonItem().getQuantite() <= 0) {
            JsfUtil.addErrorMessage("La quantitéte saisie est invalide");
        } else if (getLivraisonItem().getRemise().signum() == -1) {
            JsfUtil.addErrorMessage("Remise doit étre supérieur à 0Dh");
        } else if (getLivraisonItem().getRemise().compareTo(livraisonItem.getSousTotalHT()) == 1) {
            JsfUtil.addErrorMessage("Le remise saisi est supérieur au total");
        } else if (produitExist(livraisonItem, selected.getLivraisonItems())) {
            JsfUtil.addErrorMessage("Ligne de livraison déja existée");
        } else {
            getLivraisonItems().add(clone(getLivraisonItem()));
            //modifier les stock
            // getLivraisonItem().getProduit().setQuantiteStock(getLivraisonItem().getProduit().getQuantiteStock() - getLivraisonItem().getQuantite());
            //produitFacade.edit(getLivraisonItem().getProduit());
//            for (int i = 0; i < getLivraisonItems().size(); i++) {
//                getSelected().setSousTotal(getSelected().getSousTotal().add(getLivraisonItems().get(i).getSousTotalHT()));
//                getSelected().setTotalTax(getSelected().getTotalTax().add(getLivraisonItems().get(i).getSousTotalHT().multiply(BigDecimal.valueOf(getLivraisonItems().get(i).getTax()))));
//            }

            getSelected().setSousTotal(getSelected().getSousTotal().add(getLivraisonItem().getSousTotalHT()));
            getSelected().setTotalTax(getSelected().getTotalTax().add(getLivraisonItem().getSousTotalHT().multiply(BigDecimal.valueOf(getLivraisonItem().getTax()/100))));

            getSelected().setNouveauSousTotal(getSelected().getSousTotal().subtract(getSelected().getRemise()));
            getSelected().setMontantDu(getSelected().getNouveauSousTotal().add(getSelected().getTotalTax()));

            getFacade().edit(selected);
            livraisonWithItems(selected, getLivraisonItems());
            JsfUtil.addSuccessMessage("Ligne de livraison ajoutée avec succès");
            getLivraisonItems().clear();
            livraisonItem = null;
            quantitStock = 0.0;
            prix = 0;
            //allFacures = null;

           // System.out.println(getLivraisonItems().size() + "");

        }
    }
    

    

    
    
    
    
    
    
    
    public Boolean produitExist(LivraisonItem livraisonItem, List<LivraisonItem> livraisonItems) {
        Boolean exist = false;
          System.out.println("haaa ref dyal artcile jdid "+ livraisonItem.getProduit().getReference());
        for (int i = 0; i < livraisonItems.size(); i++) {
            System.out.println("haaa ref dyal artcile l9dam "+ livraisonItems.get(i).getProduit().getReference());
            if (getLivraisonItem().getProduit().getReference() == livraisonItems.get(i).getProduit().getReference()) {
                exist = true;
            }

        }
        return exist;
    }

    public void enregisterLivraison() {
       // System.out.println(getLivraisonItems().size() + "");

        if (selected.getClient() == null) {
            JsfUtil.addErrorMessage("Veuillez sélectionner un client");
        } else if (selected.getDateLivraison()== null) {
            JsfUtil.addErrorMessage("Veuillez choisir la date de la livraison");
        } else if (selected.getDateEcheance() == null) {
            JsfUtil.addErrorMessage("Veuillez choisir la date d'échenace de la livraison");
        } else if (selected.getDateEcheance().after(selected.getDateEcheance())) {
            JsfUtil.addErrorMessage("Veuillez vérifier les dates saisies");
        } else if (getLivraisonItems().isEmpty()) {
            JsfUtil.addErrorMessage("Veuillez ajouter des articles");
        } else {
            selected.setStatus("Brouillon");
            selected.setUtilisateur(SessionUtil.getConnectedUser());
            getFacade().create(selected);
            livraisonWithItems(selected, getLivraisonItems());
            
            setAllLivraison(getFacade().listLivraisonSociete(SessionUtil.getConnectedUser(),recherche,etat));

            JsfUtil.addSuccessMessage("Livraison enregistrée avec succès");
            selected = null;
            setEtat(null);
            getLivraisonItems().clear();
            try {
                SessionUtil.redirect("List.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(LivraisonController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public void AnnulerLivraison() {
//        for (int i = 0; i < livraisonItems.size(); i++) {
//            livraisonItems.get(i).getProduit().setQuantiteStock(livraisonItems.get(i).getProduit().getQuantiteStock() + livraisonItems.get(i).getQuantite());
//            produitFacade.edit(livraisonItems.get(i).getProduit());
//        }
        //selected = null;
        getLivraisonItems().clear();
        try {
            SessionUtil.redirect("List.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(LivraisonController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   

    public LivraisonItem clone(LivraisonItem livraisonItem) {
        LivraisonItem clone = new LivraisonItem();
        clone.setLivraison(livraisonItem.getLivraison());
        clone.setProduit(livraisonItem.getProduit());
        clone.setQuantite(livraisonItem.getQuantite());
        clone.setTax(livraisonItem.getTax());
        clone.setRemise(livraisonItem.getRemise());
        clone.setSousTotalHT(livraisonItem.getSousTotalHT());
        clone.setPrixDevise(livraisonItem.getPrixDevise());
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

 

    public String formatDateLivraison(Date date) {
       // System.out.println("dkheeeeeeeeeeeeeeeeeeeeeeeeeeeel");
       // System.out.println("haaa date" + date.toString());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        String currentTime = simpleDateFormat.format(date);
       // System.out.println("khreeeeeej");
        return currentTime;

    }



    public List<LivraisonItem> findLivraisonItemsByLivraison() {
        return livraisonItemFacade.findLivraisonItemsByLivraison(selected);
    }

    public int maxIdLivraison() {
        return getFacade().generateId("Livraison", "id") + 1;

    }

    public LivraisonItem getLivraisonItem() {
        if (livraisonItem == null) {
            livraisonItem = new LivraisonItem();
            livraisonItem.setSousTotalHT(BigDecimal.ZERO);
            livraisonItem.setRemise(BigDecimal.ZERO);
            livraisonItem.setProduit(new Produit());
            livraisonItem.getProduit().setPrixDirham(0.0);
            livraisonItem.getProduit().setQuantiteStock(0.0);
            livraisonItem.setPrixDevise(0.0);
        }
        return livraisonItem;
    }
    
    //--------------test
    public LivraisonItem getLivraisonItem2() {
        if (livraisonItem2 == null) {
            livraisonItem2 = new LivraisonItem();
            livraisonItem2.setSousTotalHT(BigDecimal.ZERO);
            livraisonItem2.setRemise(BigDecimal.ZERO);
            livraisonItem2.setProduit(getP());
            livraisonItem2.setQuantite(0.0);
            livraisonItem2.setPrixDevise(0.0);

          
            //livraisonItem.getProduit().setQuantiteStock(0.0);
        }
        return livraisonItem;
    }
     public void setLivraisonItem2(LivraisonItem livraisonItem) {
        this.livraisonItem2 = livraisonItem;
    }
    
//------------------------------------------
    public void setLivraisonItem(LivraisonItem livraisonItem) {
        this.livraisonItem = livraisonItem;
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

    public List<LivraisonItem> getLivraisonItems() {
        if (livraisonItems == null) {
            livraisonItems = new ArrayList<>();
        }
        return livraisonItems;
    }

    public void setLivraisonItems(List<LivraisonItem> livraisonItems) {
        this.livraisonItems = livraisonItems;
    }

    public void livraisonWithItems(Livraison livraison, List<LivraisonItem> livraisonItems) {
        for (int i = 0; i < livraisonItems.size(); i++) {
            livraisonItems.get(i).setLivraison(livraison);
            if(produitExist( getLivraisonItems().get(i).getProduit().getReference())==false){
            produitFacade.create(getLivraisonItems().get(i).getProduit());
            }else{
                  
           // getLivraisonItems().get(i).getProduit().setQuantiteStock(getLivraisonItems().get(i).getProduit().getQuantiteStock() - 0);
            produitFacade.edit(getLivraisonItems().get(i).getProduit());
         
            }
            livraisonItemFacade.create(livraisonItems.get(i));
          
            //modifier stock
         
        }
    }
    
    public Boolean produitExist(Integer reference){
        List<Produit> p;
        p=produitFacade.produitExist(reference);
        if(p == null)return false;
        else return true;
    }
    
   
    
     public void imprimerLivraison() throws JRException, IOException {
          Calendar calendar = Calendar.getInstance();
          calendar.setTime(selected.getDateLivraison());
          int annee = calendar.get(calendar.YEAR);
          int mois = calendar.get(calendar.MONTH);
          String RC="", ICE= "" ,IF="" ,TP="" ,Tel="" ,Fax="" ,Email="" ,Site = "";
//          System.out.println("haaa  l id = "+ selected.getId());
//          System.out.println("haaa  l date = "+ selected.getDateFacture());
//          System.out.println("haaa  l RC  = "+ selected.getUtilisateur().getSociete().getRaisonSociale());
//          System.out.println("haaa  l client = "+ selected.getClient().getRaisonSociale());
//          System.out.println("haaa  l montant du = "+ selected.getMontantDu());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("numeroLivraison", "#0"+mois+""+annee+"/"+selected.getId());
        params.put("dateLivraison", formatDateLivraison(selected.getDateLivraison())+"");
        params.put("devise", selected.getDevise()+"");
       // params.put("mois", selected.getDateFacture().getMonth()+1+"");
        //params.put("annee", selected.getDateFacture().getYear()+1900+"");
                params.put("condition", selected.getConditionLivraison()+"");

        params.put("dateEcheance", formatDateLivraison(selected.getDateEcheance())+"");
        params.put("raisonSocialeCLient", selected.getClient().getRaisonSociale()+"");
        params.put("adresseClient", selected.getClient().getAdresse()+"");
        params.put("RC", selected.getUtilisateur().getSociete().getRC()+"");
        params.put("activite", selected.getUtilisateur().getSociete().getActivitePrincipale()+"");
        params.put("adresse", selected.getUtilisateur().getSociete().getAdresse()+"");
        params.put("codePostale", selected.getUtilisateur().getSociete().getCodePostale()+"");
        params.put("pays", selected.getUtilisateur().getSociete().getPays()+"");
        params.put("ville", selected.getUtilisateur().getSociete().getVille()+"");
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
         params.put("P_Tel", Tel);
         params.put("P_Fax", Fax);
         params.put("P_Email", Email);
         params.put("P_Site", Site);

          
        params.put("totalHT", selected.getSousTotal()+"");
        params.put("totalTVA", selected.getTotalTax()+"");
        params.put("totalTTC", selected.getNouveauSousTotal()+"");
        params.put("remiseLivraison", selected.getRemise()+"");       
        params.put("montantDu", selected.getMontantDu()+"");
         params.put("logo", "C://uploads//" + SessionUtil.getConnectedUser().getSociete().getLogo()+"");

        
        

        PdfUtil.generatePdf(findLivraisonItemsByLivraison(), params, "livraison-" + formatDateLivraison(selected.getDateLivraison()) +"/" +selected.getId() , "/jasper/ImprimerLivraison.jasper");
    }
      
    
    
    
    
    
    
    
    public LivraisonController() {
    }

     public Livraison getSelected() {
        if (selected == null) {
            selected = new Livraison();
            //selected.setClient(new Client());
            selected.setDateLivraison(new Date());
            selected.setRemise(BigDecimal.ZERO);
            selected.setMontantDu(BigDecimal.ZERO);
          
            selected.setTotalTax(BigDecimal.ZERO);
           
            selected.setNouveauSousTotal(BigDecimal.ZERO);
            selected.setSousTotal(BigDecimal.ZERO);
        }

        return selected;
    }

    public void setSelected(Livraison selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private LivraisonFacade getFacade() {
        return ejbFacade;
    }

    public Livraison prepareCreate() {
        selected = new Livraison();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("LivraisonCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("LivraisonUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("LivraisonDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Livraison> getItems() {
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
                    while(findLivraisonItemsByLivraison().size()>0){
                    for(int i = 0; i<findLivraisonItemsByLivraison().size();i++){
                        livraisonItemFacade.remove(findLivraisonItemsByLivraison().get(i));
                    }
                    }
                      //Thread.sleep(1000);
                    getFacade().remove(selected);
                    SessionUtil.redirect("List.xhtml");
                    setAllLivraison(getFacade().listLivraisonSociete(SessionUtil.getConnectedUser(),recherche,etat));
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

    public Livraison getLivraison(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Livraison> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Livraison> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Livraison.class)
    public static class LivraisonControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            LivraisonController controller = (LivraisonController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "livraisonController");
            return controller.getLivraison(getKey(value));
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
            if (object instanceof Livraison) {
                Livraison o = (Livraison) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Livraison.class.getName()});
                return null;
            }
        }

    }

}
