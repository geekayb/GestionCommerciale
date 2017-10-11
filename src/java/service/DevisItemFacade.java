/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Devis;
import bean.DevisItem;
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
public class DevisItemFacade extends AbstractFacade<DevisItem> {

    @PersistenceContext(unitName = "Gestion_commercialePU")
    private EntityManager em;
    @EJB
    DevisFacade devisFacade;
    @EJB
    ProduitFacade produitFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DevisItemFacade() {
        super(DevisItem.class);
    }
    
      public int removeDevisItem(DevisItem devisItem, Devis devis) {
        if (devisItem != null) {

            devis.setSousTotal(devis.getSousTotal().subtract(devisItem.getSousTotalHT()));
            devis.setTotalTax(devis.getTotalTax().subtract(devisItem.getSousTotalHT().multiply(BigDecimal.valueOf(devisItem.getTax()/100))));
            devis.setNouveauSousTotal(devis.getSousTotal().add(devis.getRemise()));
            devis.setMontantDu(devis.getNouveauSousTotal().add(devis.getTotalTax()));
            devisFacade.edit(devis);
            devisItem.getProduit().setQuantiteStock(devisItem.getProduit().getQuantiteStock() + devisItem.getQuantite());
            produitFacade.edit(devisItem.getProduit());
            this.remove(devisItem);
            return 1;

        
        }else return -1;
        
    }


     

    public void addDevisItemToItem(DevisItem devisItem, Devis devis) {
        //List<DevisItem> devisItems = devis.getDevisItems();
        devis.setMontantDu(devis.getSousTotal().add(devisItem.getSousTotalHT()));
        devis.getDevisItems().add(clone(devisItem));
    }

    public DevisItem clone(DevisItem devisItem) {
        DevisItem clone = new DevisItem();
        clone.setDevis(devisItem.getDevis());
        clone.setProduit(devisItem.getProduit());
        clone.setQuantite(devisItem.getQuantite());
        clone.setTax(devisItem.getTax());
        clone.setRemise(devisItem.getRemise());
        clone.setSousTotalHT(devisItem.getSousTotalHT());
        return clone;
    }
    
    public List<DevisItem>findDevisItemsByDevis(Devis devis){
        String requete = "SELECT list from DevisItem list where list.devis.id = " + devis.getId();
        //System.out.println(requete);
       // System.out.println("haa list " + em.createQuery(requete).getResultList().toString());
        return em.createQuery(requete).getResultList();
    }
    
}
