/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Livraison;
import bean.LivraisonItem;
import bean.LivraisonItem;
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
public class LivraisonItemFacade extends AbstractFacade<LivraisonItem> {

    @PersistenceContext(unitName = "Gestion_commercialePU")
    private EntityManager em;
    @EJB
    LivraisonFacade livraisonFacade;
    @EJB
    ProduitFacade produitFacade;
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LivraisonItemFacade() {
        super(LivraisonItem.class);
    }
    
     public int removeLivraisonItem(LivraisonItem livraisonItem, Livraison livraison) {
        if (livraisonItem != null) {

            livraison.setSousTotal(livraison.getSousTotal().subtract(livraisonItem.getSousTotalHT()));
            livraison.setTotalTax(livraison.getTotalTax().subtract(livraisonItem.getSousTotalHT().multiply(BigDecimal.valueOf(livraisonItem.getTax()/100))));
            livraison.setNouveauSousTotal(livraison.getSousTotal().add(livraison.getRemise()));
            livraison.setMontantDu(livraison.getNouveauSousTotal().add(livraison.getTotalTax()));
            livraisonFacade.edit(livraison);
            livraisonItem.getProduit().setQuantiteStock(livraisonItem.getProduit().getQuantiteStock() + livraisonItem.getQuantite());
            produitFacade.edit(livraisonItem.getProduit());
            this.remove(livraisonItem);
            return 1;

        
        }else return -1;
        
    }


     

    public void addLivraisonItemToItem(LivraisonItem livraisonItem, Livraison livraison) {
        //List<LivraisonItem> livraisonItems = livraison.getLivraisonItems();
        livraison.setMontantDu(livraison.getSousTotal().add(livraisonItem.getSousTotalHT()));
        livraison.getLivraisonItems().add(clone(livraisonItem));
    }

    public LivraisonItem clone(LivraisonItem livraisonItem) {
        LivraisonItem clone = new LivraisonItem();
        clone.setLivraison(livraisonItem.getLivraison());
        clone.setProduit(livraisonItem.getProduit());
        clone.setQuantite(livraisonItem.getQuantite());
        clone.setTax(livraisonItem.getTax());
        clone.setRemise(livraisonItem.getRemise());
        clone.setSousTotalHT(livraisonItem.getSousTotalHT());
        return clone;
    }
    
    public List<LivraisonItem>findLivraisonItemsByLivraison(Livraison livraison){
        String requete = "SELECT list from LivraisonItem list where list.livraison.id = " + livraison.getId();
        //System.out.println(requete);
       // System.out.println("haa list " + em.createQuery(requete).getResultList().toString());
        return em.createQuery(requete).getResultList();
    }
    
    
}
