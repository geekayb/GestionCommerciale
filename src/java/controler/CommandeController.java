package controler;

import bean.Commande;
import bean.Commande;
import bean.CommandeItem;
import bean.Commande;
import bean.CommandeItem;
import bean.Produit;
import bean.Utilisateur;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import controler.util.PdfUtil;
import controler.util.SessionUtil;
import java.io.IOException;
import service.CommandeFacade;

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
import service.CommandeFacade;
import service.FactureFacade;
import service.FactureItemFacade;
import service.CommandeItemFacade;
import service.ProduitFacade;

@Named("commandeController")
@SessionScoped
public class CommandeController implements Serializable {

    @EJB
    private service.CommandeFacade ejbFacade;
    private List<Commande> items = null;
    private Commande selected;
    private List<CommandeItem> commandeItems = null;
    private CommandeItem commandeItem;
    private CommandeItem commandeItem2;
    private Double quantitStock;
    private double prix;
    private Produit produit;
    private String etat="";
    private List<Commande> allCommande;
     private List<Produit> listProduitCommande = null;
          private List<Produit> listProduitCommandeEdit = null;
    
    private String recherche="";
    Utilisateur utilisateur;
    private Produit p = new Produit();
   
    @EJB
    CommandeItemFacade commandeItemFacade;
    @EJB
    ProduitFacade produitFacade;
    @EJB
    FactureFacade factureFacade;
    @EJB
    FactureItemFacade factureItemFacade;

    public void edite(){
        try {
            SessionUtil.redirect("/Gestion_commerciale/faces/commande/Edit.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
       public void redirectListe(){
        try {
            SessionUtil.redirect("/Gestion_commerciale/faces/commande/List.xhtml");
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
     public List<Produit> getListProduitDevise() {
      Utilisateur utilisateur = SessionUtil.getConnectedUser();
        if(listProduitCommande==null){
            listProduitCommande = new ArrayList<>();
        }
           if(!selected.getDevise().equals(""))listProduitCommande = factureFacade.getProduitByDevise(selected.getDevise(), utilisateur);

        return listProduitCommande;
    }

    public void setListProduitDevise(List<Produit> listProduitDevise) {
        this.listProduitCommande = listProduitDevise;
    }

     public List<Produit> getListProduitDeviseEdit() {
          Utilisateur utilisateur = SessionUtil.getConnectedUser();
        if(listProduitCommandeEdit==null){
            listProduitCommandeEdit = new ArrayList<>();
            
        }
        listProduitCommandeEdit=factureFacade.getProduitByDevise(selected.getDevise(), utilisateur);
        return listProduitCommandeEdit;
    }
      public void setListProduitDeviseEdit(List<Produit> listProduitDevise) {
        this.listProduitCommandeEdit = listProduitDevise;
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
            if (selected.getFournisseur()!= null) {
                getFacade().edit(selected);
            }
        }
  }
 public void getProduitByDevise(){
        System.out.println("dkheeeeel l getProduitByDevise");
        Utilisateur utilisateur = SessionUtil.getConnectedUser();
       setListProduitDevise(factureFacade.getProduitByDevise(selected.getDevise(), utilisateur));
       //factureItem.setProduit(null);
         setQuantitStock(0.0);
         setPrix(0);
         
 }

    public List<Commande> getAllCommande() {
        
            Utilisateur u = SessionUtil.getConnectedUser();
            if (u != null) {
                allCommande = new ArrayList<>();
                allCommande = getFacade().listCommandeSociete(u, recherche, etat);
            } else {        
                allCommande = getItems();
            }
       
        return allCommande;
    }

    public void setAllCommande(List<Commande> allCommande) {
        this.allCommande = allCommande;
    }

   
    
  

    public String getEtat() {
        if(etat == null) etat = "";
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

//    public void calculerSousTotal() {
//        if (getCommandeItem().getQuantite() != null) {
//            BigDecimal total = BigDecimal.valueOf(getCommandeItem().getQuantite() * getCommandeItem().getProduit().getPrixUnitaire());
//            getCommandeItem().setSousTotalHT(total);
//        } else {
//            getCommandeItem().setSousTotalHT(BigDecimal.ZERO);
//        }
//    }
    public List<Commande> findCommandesByEtat() {
        Utilisateur u = SessionUtil.getConnectedUser();
        setAllCommande(getFacade().findCommandesByEtat(etat, u));
        return allCommande;
    }

    public List<Commande> findCommandeByCritere() {
        Utilisateur u = SessionUtil.getConnectedUser();
        setAllCommande(getFacade().findCommandeByCritere(recherche, u));
        return allCommande;
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
        double qteAvant = getQteByCommandeItem();
     //   System.out.println("qte avant" + qteAvant);
        double qteApres = commandeItem.getQuantite();
        //double res = (qteAvant - commandeItem.getQuantite());
     //   System.out.println("qte aprés" + (qteApres - qteAvant));
        //modfier stock
        if((commandeItem.getProduit().getQuantiteStock() + qteAvant) - qteApres >= 0){
        commandeItem.getProduit().setQuantiteStock((commandeItem.getProduit().getQuantiteStock() + qteAvant) - qteApres);

        commandeItemFacade.edit(commandeItem);
        produitFacade.edit(commandeItem.getProduit());
        selected.setMontantDu(BigDecimal.ZERO);  
        selected.setTotalTax(BigDecimal.ZERO);
        selected.setNouveauSousTotal(BigDecimal.ZERO);
        selected.setSousTotal(BigDecimal.ZERO);
       // selected.setEtat("non payée");

        List<CommandeItem> list = commandeItemFacade.findCommandeItemsByCommande(selected);
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

    public Double getQteByCommandeItem() {
        return getFacade().getQteByCommandeItem(commandeItem);
    }

    public void deleteCommandeItem(CommandeItem commandeItem) {
        int res = commandeItemFacade.removeCommandeItem(commandeItem, selected);
        if (res > 0) {
           // System.out.println("haa list f lwl=" + selected.getCommandeItems().size());
            selected.getCommandeItems().remove(commandeItem);
           // System.out.println("haa list f lekher=" + selected.getCommandeItems().size());

           // System.out.println("delete CommandeItem new Montant=>" + selected.getMontantDu());
            JsfUtil.addSuccessMessage("Ligne de Commande Supprimé avec succes!!");
        } else {
            JsfUtil.addErrorMessage("Erreur lors de la suppression !!");
        }
    }

    public void calculerSousTotal() {
       // System.out.println("dkheeel");
        BigDecimal total = BigDecimal.ZERO;
//        if(getCommandeItem().getProduit().getPrixUnitaire()==null)System.out.println("rah null asat");
//        System.out.println("haaa l qte "+ getCommandeItem().getQuantite());
        if (getCommandeItem().getQuantite() != null) {
            total = total.add(BigDecimal.valueOf((getCommandeItem().getQuantite() * getCommandeItem().getPrixDevise())));
        }
        if (getCommandeItem().getRemise() != null) {
            total = total.subtract(getCommandeItem().getRemise());
        }
        getCommandeItem().setSousTotalHT(total);

    }
     public void calculerSousTotalService() {
       // System.out.println("dkheeel");
        BigDecimal total = BigDecimal.ZERO;
//        if(getFactureItem().getProduit().getPrixUnitaire()==null)System.out.println("rah null asat");
//        System.out.println("haaa l qte "+ getFactureItem().getQuantite());
        if (getCommandeItem2().getQuantite() != null) {
            total = total.add(BigDecimal.valueOf((getCommandeItem2().getQuantite() * getCommandeItem2().getPrixDevise())));
        }
        if (getCommandeItem2().getRemise() != null) {
            total = total.subtract(getCommandeItem2().getRemise());
        }
        getCommandeItem2().setSousTotalHT(total);

    }

    public void getQtePixByProduit() {
        setQuantitStock(produitFacade.getQteByProduit(commandeItem.getProduit()));
        setPrix(produitFacade.getPrixByProduit(commandeItem.getProduit(), selected.getDevise()));
        getCommandeItem().setSousTotalHT(BigDecimal.ZERO);
        getCommandeItem().setQuantite(0.0);
        getCommandeItem().setRemise(BigDecimal.ZERO);
        getCommandeItem().setTax(null);
    }

    public void addToCommande() {
        //commandeItemFacade.addCommandeItemToItem(getCommandeItem(), selected);
        if (getCommandeItem().getProduit() == null) {
            JsfUtil.addErrorMessage("Veuillez choisir un produit");
        } else if (getCommandeItem().getQuantite() > getCommandeItem().getProduit().getQuantiteStock()) {
            JsfUtil.addErrorMessage("La quantitéte spécifier est superieur à la quantité restée en stock merçi de la verifier");
        } else if (getCommandeItem().getQuantite() <= 0) {
            JsfUtil.addErrorMessage("La quantitéte saisie est invalide");
        } else if (getCommandeItem().getRemise().signum() == -1) {
            JsfUtil.addErrorMessage("Remise doit étre supérieur à 0Dh");
        } else if (getCommandeItem().getRemise().compareTo(commandeItem.getSousTotalHT()) == 1) {
            JsfUtil.addErrorMessage("Le remise saisi est supérieur au total");
        } else if (produitExist(getCommandeItem(), getCommandeItems())) {
            JsfUtil.addErrorMessage("Article déja choisi");
        } else {
            getCommandeItems().add(clone(getCommandeItem()));
             for (int i = 0; i < commandeItems.size(); i++) {
           System.out.println("haaa les ref kamlin" + commandeItems.get(i).getProduit().getReference());
             }
            //modifier les stock
            // getCommandeItem().getProduit().setQuantiteStock(getCommandeItem().getProduit().getQuantiteStock() - getCommandeItem().getQuantite());
            //produitFacade.edit(getCommandeItem().getProduit());
//            for (int i = 0; i < getCommandeItems().size(); i++) {
//                getSelected().setSousTotal(getSelected().getSousTotal().add(getCommandeItems().get(i).getSousTotalHT()));
//                getSelected().setTotalTax(getSelected().getTotalTax().add(getCommandeItems().get(i).getSousTotalHT().multiply(BigDecimal.valueOf(getCommandeItems().get(i).getTax()))));
//            }

            getSelected().setSousTotal(getSelected().getSousTotal().add(getCommandeItem().getSousTotalHT()));
            getSelected().setTotalTax(getSelected().getTotalTax().add(getCommandeItem().getSousTotalHT().multiply(BigDecimal.valueOf(getCommandeItem().getTax()/100))));

            getSelected().setNouveauSousTotal(getSelected().getSousTotal().subtract(getSelected().getRemise()));
            getSelected().setMontantDu(getSelected().getNouveauSousTotal().add(getSelected().getTotalTax()));

            setCommandeItem(null);
            quantitStock = 0.0;
            prix = 0;
            p = new Produit();
            getCommandeItem().setRemise(BigDecimal.ZERO);
            //getCommandeItem().getProduit().setReference(-1);
          //  commandeItem = new CommandeItem();
  

            //System.out.println(getCommandeItems().size() + "");

        }

    }
    
    
        //------------------------------test
    
        public void addServiceToCommande() {
        //commandeItemFacade.addCommandeItemToItem(getCommandeItem(), selected);
        if (getCommandeItem2().getProduit().getReference() == null) {
            JsfUtil.addErrorMessage("Veuillez entrer la réference du produit"); 
        }else if(getCommandeItem2().getProduit().getLibelle().equals("")){
           // System.out.println("haaaaaaa smiyaaaaaa" + getCommandeItem2().getProduit().getLibelle());
           JsfUtil.addErrorMessage("Veuillez entrer le nom du produit"); 
            } else if (getCommandeItem2().getQuantite() == null) {
            JsfUtil.addErrorMessage("Veuillez entrer la quantité voulu");
        } else if (getCommandeItem2().getQuantite() <= 0) {
            JsfUtil.addErrorMessage("La quantitéte doit étre supérieur à 0");
        }else if(getCommandeItem2().getPrixDevise()==0.0){
          JsfUtil.addErrorMessage("Veuillez entrer le prix unitaire");
        } else if (getCommandeItem2().getRemise().signum() == -1) {
            JsfUtil.addErrorMessage("Remise doit étre supérieur à 0Dh");
        } else if (getCommandeItem2().getRemise().compareTo(getCommandeItem2().getSousTotalHT()) == 1) {
            JsfUtil.addErrorMessage("Le remise saisi est supérieur au total");
        } else if (produitExist(getCommandeItem2(), getCommandeItems())) {
            JsfUtil.addErrorMessage("Article déja choisi");
        } else if (getSelected().getDevise().equals("")) {
            JsfUtil.addErrorMessage("Veuillez choisir un devise");
        } else {
            getCommandeItems().add(clone(getCommandeItem2()));
             for (int i = 0; i < commandeItems.size(); i++) {
           System.out.println("haaa les ref kamlin" + commandeItems.get(i).getProduit().getReference());
             }
            //modifier les stock
            // getCommandeItem().getProduit().setQuantiteStock(getCommandeItem().getProduit().getQuantiteStock() - getCommandeItem().getQuantite());
            //produitFacade.edit(getCommandeItem().getProduit());
//            for (int i = 0; i < getCommandeItems().size(); i++) {
//                getSelected().setSousTotal(getSelected().getSousTotal().add(getCommandeItems().get(i).getSousTotalHT()));
//                getSelected().setTotalTax(getSelected().getTotalTax().add(getCommandeItems().get(i).getSousTotalHT().multiply(BigDecimal.valueOf(getCommandeItems().get(i).getTax()))));
//            }

             getSelected().setSousTotal(getSelected().getSousTotal().add(getCommandeItem2().getSousTotalHT()));
            getSelected().setTotalTax(getSelected().getTotalTax().add(getCommandeItem2().getSousTotalHT().multiply(BigDecimal.valueOf(getCommandeItem2().getTax()/100))));

            getSelected().setNouveauSousTotal(getSelected().getSousTotal().subtract(getSelected().getRemise()));
            getSelected().setMontantDu(getSelected().getNouveauSousTotal().add(getSelected().getTotalTax()));
            setCommandeItem(null);
            setCommandeItem2(null);
//            quantitStock = 0.0;
//            prix = 0;
            p = new Produit();
            //getCommandeItem2().setRemise(BigDecimal.ZERO);
            //getCommandeItem().getProduit().setReference(-1);
          //  commandeItem = new CommandeItem();
  

            //System.out.println(getCommandeItems().size() + "");

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
    
    
    
    
    

    public void addItemToCommandeEdit() {
        //commandeItemFacade.addCommandeItemToItem(getCommandeItem(), selected);
        if (getCommandeItem().getProduit() == null) {
            JsfUtil.addErrorMessage("Veuillez choisir un produit");
        } else if (getCommandeItem().getQuantite() > getCommandeItem().getProduit().getQuantiteStock()) {
            JsfUtil.addErrorMessage("Stock insuffisant");
        } else if (getCommandeItem().getQuantite() <= 0) {
            JsfUtil.addErrorMessage("La quantitéte saisie est invalide");
        } else if (getCommandeItem().getRemise().signum() == -1) {
            JsfUtil.addErrorMessage("Remise doit étre supérieur à 0Dh");
        } else if (getCommandeItem().getRemise().compareTo(commandeItem.getSousTotalHT()) == 1) {
            JsfUtil.addErrorMessage("Le remise saisi est supérieur au total");
        } else if (produitExist(commandeItem, selected.getCommandeItems())) {
            JsfUtil.addErrorMessage("Ligne de commande déja existée");
        } else {
            getCommandeItems().add(clone(getCommandeItem()));
            //modifier les stock
            // getCommandeItem().getProduit().setQuantiteStock(getCommandeItem().getProduit().getQuantiteStock() - getCommandeItem().getQuantite());
            //produitFacade.edit(getCommandeItem().getProduit());
//            for (int i = 0; i < getCommandeItems().size(); i++) {
//                getSelected().setSousTotal(getSelected().getSousTotal().add(getCommandeItems().get(i).getSousTotalHT()));
//                getSelected().setTotalTax(getSelected().getTotalTax().add(getCommandeItems().get(i).getSousTotalHT().multiply(BigDecimal.valueOf(getCommandeItems().get(i).getTax()))));
//            }

            getSelected().setSousTotal(getSelected().getSousTotal().add(getCommandeItem().getSousTotalHT()));
            getSelected().setTotalTax(getSelected().getTotalTax().add(getCommandeItem().getSousTotalHT().multiply(BigDecimal.valueOf(getCommandeItem().getTax()/100))));

            getSelected().setNouveauSousTotal(getSelected().getSousTotal().subtract(getSelected().getRemise()));
            getSelected().setMontantDu(getSelected().getNouveauSousTotal().add(getSelected().getTotalTax()));

            getFacade().edit(selected);
            commandeWithItems(selected, getCommandeItems());
            JsfUtil.addSuccessMessage("Ligne de commande ajoutée avec succès");
            getCommandeItems().clear();
            commandeItem = null;
            quantitStock = 0.0;
            prix = 0;
            //allFacures = null;

           // System.out.println(getCommandeItems().size() + "");

        }
    }
    

    

    
    
    
    
    
    
    
    public Boolean produitExist(CommandeItem commandeItem, List<CommandeItem> commandeItems) {
        Boolean exist = false;
          System.out.println("haaa ref dyal artcile jdid "+ commandeItem.getProduit().getReference());
        for (int i = 0; i < commandeItems.size(); i++) {
            System.out.println("haaa ref dyal artcile l9dam "+ commandeItems.get(i).getProduit().getReference());
            if (getCommandeItem().getProduit().getReference() == commandeItems.get(i).getProduit().getReference()) {
                exist = true;
            }

        }
        return exist;
    }

    public void enregisterCommande() {
       // System.out.println(getCommandeItems().size() + "");

        if (selected.getFournisseur()== null) {
            JsfUtil.addErrorMessage("Veuillez sélectionner un client");
        } else if (selected.getDateCommande()== null) {
            JsfUtil.addErrorMessage("Veuillez choisir la date de la commande");
        } else if (selected.getDateEcheance() == null) {
            JsfUtil.addErrorMessage("Veuillez choisir la date d'échenace de la commande");
        } else if (selected.getDateEcheance().after(selected.getDateEcheance())) {
            JsfUtil.addErrorMessage("Veuillez vérifier les dates saisies");
        } else if (getCommandeItems().isEmpty()) {
            JsfUtil.addErrorMessage("Veuillez ajouter des articles");
        } else {
            selected.setStatus("Brouillon");
            selected.setUtilisateur(SessionUtil.getConnectedUser());
            getFacade().create(selected);
            commandeWithItems(selected, getCommandeItems());
            
            setAllCommande(getFacade().listCommandeSociete(SessionUtil.getConnectedUser(),recherche,etat));

            JsfUtil.addSuccessMessage("Commande enregistrée avec succès");
            selected = null;
            setEtat(null);
            getCommandeItems().clear();
            try {
                SessionUtil.redirect("List.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(CommandeController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public void AnnulerCommande() {
//        for (int i = 0; i < commandeItems.size(); i++) {
//            commandeItems.get(i).getProduit().setQuantiteStock(commandeItems.get(i).getProduit().getQuantiteStock() + commandeItems.get(i).getQuantite());
//            produitFacade.edit(commandeItems.get(i).getProduit());
//        }
        //selected = null;
        getCommandeItems().clear();
        try {
            SessionUtil.redirect("List.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(CommandeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   

    public CommandeItem clone(CommandeItem commandeItem) {
        CommandeItem clone = new CommandeItem();
        clone.setCommande(commandeItem.getCommande());
        clone.setProduit(commandeItem.getProduit());
        clone.setQuantite(commandeItem.getQuantite());
        clone.setTax(commandeItem.getTax());
        clone.setRemise(commandeItem.getRemise());
        clone.setSousTotalHT(commandeItem.getSousTotalHT());
        clone.setPrixDevise(commandeItem.getPrixDevise());
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

 

    public String formatDateCommande(Date date) {
       // System.out.println("dkheeeeeeeeeeeeeeeeeeeeeeeeeeeel");
       // System.out.println("haaa date" + date.toString());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        String currentTime = simpleDateFormat.format(date);
       // System.out.println("khreeeeeej");
        return currentTime;

    }



    public List<CommandeItem> findCommandeItemsByCommande() {
        return commandeItemFacade.findCommandeItemsByCommande(selected);
    }

    public int maxIdCommande() {
        return getFacade().generateId("Commande", "id") + 1;

    }

    public CommandeItem getCommandeItem() {
        if (commandeItem == null) {
            commandeItem = new CommandeItem();
            commandeItem.setSousTotalHT(BigDecimal.ZERO);
            commandeItem.setRemise(BigDecimal.ZERO);
            commandeItem.setProduit(new Produit());
            commandeItem.getProduit().setPrixDirham(0.0);
            commandeItem.getProduit().setQuantiteStock(0.0);
        }
        return commandeItem;
    }
    
    //--------------test
    public CommandeItem getCommandeItem2() {
        if (commandeItem2 == null) {
            commandeItem2 = new CommandeItem();
            commandeItem2.setSousTotalHT(BigDecimal.ZERO);
            commandeItem2.setRemise(BigDecimal.ZERO);
            commandeItem2.setProduit(getP());
            commandeItem2.setQuantite(0.0);
          
            //commandeItem.getProduit().setQuantiteStock(0.0);
        }
        return commandeItem2;
    }
     public void setCommandeItem2(CommandeItem commandeItem) {
        this.commandeItem = commandeItem;
    }
    
//------------------------------------------
    public void setCommandeItem(CommandeItem commandeItem) {
        this.commandeItem = commandeItem;
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

    public List<CommandeItem> getCommandeItems() {
        if (commandeItems == null) {
            commandeItems = new ArrayList<>();
        }
        return commandeItems;
    }

    public void setCommandeItems(List<CommandeItem> commandeItems) {
        this.commandeItems = commandeItems;
    }

    public void commandeWithItems(Commande commande, List<CommandeItem> commandeItems) {
        for (int i = 0; i < commandeItems.size(); i++) {
            commandeItems.get(i).setCommande(commande);
            if(produitExist( getCommandeItems().get(i).getProduit().getReference())==false){
            produitFacade.create(getCommandeItems().get(i).getProduit());
            }else{
                  
           // getCommandeItems().get(i).getProduit().setQuantiteStock(getCommandeItems().get(i).getProduit().getQuantiteStock() - 0);
            produitFacade.edit(getCommandeItems().get(i).getProduit());
         
            }
            commandeItemFacade.create(commandeItems.get(i));
          
            //modifier stock
         
        }
    }
    
    public Boolean produitExist(Integer reference){
        List<Produit> p;
        p=produitFacade.produitExist(reference);
        if(p == null)return false;
        else return true;
    }
    
   
    
     public void imprimerCommande() throws JRException, IOException {
          Calendar calendar = Calendar.getInstance();
          calendar.setTime(selected.getDateCommande());
          int annee = calendar.get(calendar.YEAR);
          int mois = calendar.get(calendar.MONTH);
          String RC="", ICE= "" ,IF=""  ,TP="" ,Tel="" ,Fax="" ,Email="" ,Site = "";
//          System.out.println("haaa  l id = "+ selected.getId());
//          System.out.println("haaa  l date = "+ selected.getDateFacture());
//          System.out.println("haaa  l RC  = "+ selected.getUtilisateur().getSociete().getRaisonSociale());
//          System.out.println("haaa  l client = "+ selected.getClient().getRaisonSociale());
//          System.out.println("haaa  l montant du = "+ selected.getMontantDu());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("numeroCommande", "#0"+mois+""+annee+"/"+selected.getId());
        params.put("dateCommande", formatDateCommande(selected.getDateCommande())+"");
        params.put("devise", selected.getDevise()+"");
       // params.put("mois", selected.getDateFacture().getMonth()+1+"");
        //params.put("annee", selected.getDateFacture().getYear()+1900+"");
                params.put("condition", selected.getConditionCommande()+"");

        params.put("dateEcheance", formatDateCommande(selected.getDateEcheance())+"");
        params.put("nomFournisseur", selected.getFournisseur().getNom()+"");
        params.put("adresseFournisseur", selected.getFournisseur().getAdresse()+"");
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
        params.put("remiseCommande", selected.getRemise()+"");       
        params.put("montantDu", selected.getMontantDu()+"");
        params.put("logo", "C://uploads//" + SessionUtil.getConnectedUser().getSociete().getLogo()+"");

        

        PdfUtil.generatePdf(findCommandeItemsByCommande(), params, "commande-" + formatDateCommande(selected.getDateCommande()) +"/" +selected.getId() , "/jasper/ImprimerCommande.jasper");
    }
      
    
    
    
    
    
    
    
    public CommandeController() {
    }

     public Commande getSelected() {
        if (selected == null) {
            selected = new Commande();
            //selected.setClient(new Client());
            selected.setDateCommande(new Date());
            selected.setRemise(BigDecimal.ZERO);
            selected.setMontantDu(BigDecimal.ZERO);
          
            selected.setTotalTax(BigDecimal.ZERO);
           
            selected.setNouveauSousTotal(BigDecimal.ZERO);
            selected.setSousTotal(BigDecimal.ZERO);
        }

        return selected;
    }

    public void setSelected(Commande selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private CommandeFacade getFacade() {
        return ejbFacade;
    }

    public Commande prepareCreate() {
        selected = new Commande();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CommandeCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CommandeUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CommandeDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Commande> getItems() {
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
                    while(findCommandeItemsByCommande().size()>0){
                    for(int i = 0; i<findCommandeItemsByCommande().size();i++){
                        commandeItemFacade.remove(findCommandeItemsByCommande().get(i));
                    }
                    }
                      //Thread.sleep(1000);
                    getFacade().remove(selected);
                    SessionUtil.redirect("List.xhtml");
                    setAllCommande(getFacade().listCommandeSociete(SessionUtil.getConnectedUser(),recherche,etat));
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
    public Commande getCommande(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Commande> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Commande> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Commande.class)
    public static class CommandeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CommandeController controller = (CommandeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "commandeController");
            return controller.getCommande(getKey(value));
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
            if (object instanceof Commande) {
                Commande o = (Commande) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Commande.class.getName()});
                return null;
            }
        }

    }

}
