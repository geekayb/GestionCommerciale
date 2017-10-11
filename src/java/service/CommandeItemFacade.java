/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Commande;
import bean.CommandeItem;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ayoub
 */
@Stateless
public class CommandeItemFacade extends AbstractFacade<CommandeItem> {

    @PersistenceContext(unitName = "Gestion_commercialePU")
    private EntityManager em;

    
    @EJB
    CommandeFacade commandeFacade;
    @EJB
    ProduitFacade produitFacade;
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CommandeItemFacade() {
        super(CommandeItem.class);
    }
    
     public int removeCommandeItem(CommandeItem commandeItem, Commande commande) {
        if (commandeItem != null) {

            commande.setSousTotal(commande.getSousTotal().subtract(commandeItem.getSousTotalHT()));
            commande.setTotalTax(commande.getTotalTax().subtract(commandeItem.getSousTotalHT().multiply(BigDecimal.valueOf(commandeItem.getTax()/100))));
            commande.setNouveauSousTotal(commande.getSousTotal().add(commande.getRemise()));
            commande.setMontantDu(commande.getNouveauSousTotal().add(commande.getTotalTax()));
            commandeFacade.edit(commande);
            commandeItem.getProduit().setQuantiteStock(commandeItem.getProduit().getQuantiteStock() + commandeItem.getQuantite());
            produitFacade.edit(commandeItem.getProduit());
            this.remove(commandeItem);
            return 1;

        
        }else return -1;
        
    }


     

    public void addCommandeItemToItem(CommandeItem commandeItem, Commande commande) {
        //List<CommandeItem> commandeItems = commande.getCommandeItems();
        commande.setMontantDu(commande.getSousTotal().add(commandeItem.getSousTotalHT()));
        commande.getCommandeItems().add(clone(commandeItem));
    }

    public CommandeItem clone(CommandeItem commandeItem) {
        CommandeItem clone = new CommandeItem();
        clone.setCommande(commandeItem.getCommande());
        clone.setProduit(commandeItem.getProduit());
        clone.setQuantite(commandeItem.getQuantite());
        clone.setTax(commandeItem.getTax());
        clone.setRemise(commandeItem.getRemise());
        clone.setSousTotalHT(commandeItem.getSousTotalHT());
        return clone;
    }
    
    public List<CommandeItem>findCommandeItemsByCommande(Commande commande){
        String requete = "SELECT list from CommandeItem list where list.commande.id = " + commande.getId();
        //System.out.println(requete);
       // System.out.println("haa list " + em.createQuery(requete).getResultList().toString());
        return em.createQuery(requete).getResultList();
    }
    
    
}
