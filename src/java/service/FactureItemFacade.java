/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Facture;
import bean.FactureItem;
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
public class FactureItemFacade extends AbstractFacade<FactureItem> {

    @PersistenceContext(unitName = "Gestion_commercialePU")
    private EntityManager em;
    
    @EJB
    private FactureFacade factureFacade;
    @EJB
    private ProduitFacade produitFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FactureItemFacade() {
        super(FactureItem.class);
    }
    
    
     public int removeFactureItem(FactureItem factureItem, Facture facture) {
        if (factureItem != null) {

            facture.setSousTotal(facture.getSousTotal().subtract(factureItem.getSousTotalHT()));
            facture.setTotalTax(facture.getTotalTax().subtract(factureItem.getSousTotalHT().multiply(BigDecimal.valueOf(factureItem.getTax()/100))));
            facture.setNouveauSousTotal(facture.getSousTotal().add(facture.getTotalTax()));
            facture.setTotalAvecRemise(facture.getNouveauSousTotal().subtract(facture.getRemise()));
            facture.setMontantPaye(facture.getMontantPaye());
            facture.setMontantDu(facture.getTotalAvecRemise().subtract(facture.getMontantPaye()));
            factureFacade.edit(facture);
            factureItem.getProduit().setQuantiteStock(factureItem.getProduit().getQuantiteStock() + factureItem.getQuantite());
            produitFacade.edit(factureItem.getProduit());
            this.remove(factureItem);
            return 1;

        
        }else return -1;
        
    }


     

    public void addFactureItemToItem(FactureItem factureItem, Facture facture) {
        //List<FactureItem> factureItems = facture.getFactureItems();
        facture.setMontantDu(facture.getSousTotal().add(factureItem.getSousTotalHT()));
        facture.getFactureItems().add(clone(factureItem));
    }

    public FactureItem clone(FactureItem factureItem) {
        FactureItem clone = new FactureItem();
        clone.setFacture(factureItem.getFacture());
        clone.setProduit(factureItem.getProduit());
        clone.setQuantite(factureItem.getQuantite());
        clone.setTax(factureItem.getTax());
        clone.setRemise(factureItem.getRemise());
        clone.setSousTotalHT(factureItem.getSousTotalHT());
        return clone;
    }
    
    public List<FactureItem>findFactureItemsByFacture(Facture facture){
        String requete = "SELECT list from FactureItem list where list.facture.id = " + facture.getId();
        //System.out.println(requete);
       // System.out.println("haa list " + em.createQuery(requete).getResultList().toString());
        return em.createQuery(requete).getResultList();
    }
    
}
