/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;


import bean.Devis;
import bean.DevisItem;
import bean.Utilisateur;
import controler.util.DateUtil;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ayoub
 */
@Stateless
public class DevisFacade extends AbstractFacade<Devis> {

    @PersistenceContext(unitName = "Gestion_commercialePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DevisFacade() {
        super(Devis.class);
    }
    
     public List<Devis> listDevisSociete(Utilisateur utilisateur) {
        String requete = "select d from Devis c where d.utilisateur.societe.id = " + utilisateur.getSociete().getId();
        
        return em.createQuery(requete).getResultList();

    }
     
      public Double getQteByDevisItem(DevisItem devisItem) {
        return (Double) em.createQuery("SELECT item.quantite from DevisItem item where item.id = " + devisItem.getId()).getSingleResult();
    }

  

    public List<Devis> findDevissByEtat(String etat, Utilisateur utilisateur) {
        String requete = "";
        if (etat.equals("tout")) {
            requete = "select f from Devis f where f.utilisateur.societe.id = " + utilisateur.getSociete().getId();
        } else {
            requete = "select f from Devis f where f.utilisateur.societe.id = " + utilisateur.getSociete().getId() + " and f.etat = '" + etat + "'";

        }
        return em.createQuery(requete).getResultList();
    }

    public List<Devis> findDevisByCritere(String recherche, Utilisateur utilisateur) {

        String requete = "SELECT d FROM Devis d where d.utilisateur.societe.id = " + utilisateur.getSociete().getId();
        if (!recherche.equals("")) {
            requete
                    += " and (d.client.raisonSociale like '%" + recherche + "%' "
                    + " or d.dateEmission like '%" + recherche + "%' "
                    + " or d.id like '%" + recherche + "%' "
                    + " or d.montantDu like '%" + recherche + "%' "
                    
                    + " or d.etat like '%" + recherche + "%') "
                    + " and d.utilisateur.societe.id = " + utilisateur.getSociete().getId();
        }

        //  System.out.println(requete);
        return em.createQuery(requete).getResultList();
    }



    public List<Devis> findDevisRetardByCritere(String recherche, Utilisateur utilisateur) {
        Date currentDate = new Date();
        String requete = "select f from Devis f where FUNC('DATEDIFF','" + DateUtil.getSqlDate(currentDate) + "' , f.dateEcheance)>0 and f.utilisateur.societe.id = " + utilisateur.getSociete().getId() + " and f.etat = 'Non payée'";
        //System.out.println("haaaaa recherche" + recherche);
        if (!recherche.equals("")) {
            requete
                    += " and (d.client.raisonSociale like '%" + recherche + "%' "
                    + " or d.dateEmission like '%" + recherche + "%' "
                    + " or d.id like '%" + recherche + "%' "
                    + " or d.montantDu like '%" + recherche + "%' "
                    
                    + " or d.etat like '%" + recherche + "%') "
                    + " and d.utilisateur.societe.id = " + utilisateur.getSociete().getId();
        }
        //System.out.println("haa requte"+ requete);
        //System.out.println("haa lista"+ em.createQuery(requete).getResultList());

        return em.createQuery(requete).getResultList();
    }

    public List<Devis> listDevisSociete(Utilisateur utilisateur, String recherche, String etat) {
        String requete = "select d from Devis d where d.utilisateur.societe.id = " + utilisateur.getSociete().getId();
        if (!recherche.equals("")) {
            requete
                    += " and (d.client.raisonSociale like '%" + recherche + "%' "
                    + " or d.dateEmission like '%" + recherche + "%' "
                    + " or d.id like '%" + recherche + "%' "
                    + " or d.montantDu like '%" + recherche + "%' "
                    
                    + " or d.etat like '%" + recherche + "%') "
                    + " and d.utilisateur.societe.id = " + utilisateur.getSociete().getId();

        }
        if (!etat.equals("")) {
            if (!etat.equals("tout")) {
                requete += " and d.etat = '" + etat + "'";

            }
        }
        //System.out.println("haaaa requete dyal les deviss " + requete);
        // System.out.println(em.createQuery(requete).getResultList());
        return em.createQuery(requete).getResultList();

    }

    public BigDecimal getMontantTotalPaye(Utilisateur connectedUser) {
        String requete = "select sum(f.montantPaye) from Devis f where f.etat = 'payée' and f.utilisateur.societe.id = " + connectedUser.getSociete().getId();
        if (em.createQuery(requete).getSingleResult() != null) {
            return (BigDecimal) em.createQuery(requete).getSingleResult();
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getMontantTotalNonPaye(Utilisateur connectedUser) {
        String requete = "select sum(f.montantDu) from Devis f where f.etat = 'Non payée' and f.utilisateur.societe.id = " + connectedUser.getSociete().getId();
        if (em.createQuery(requete).getSingleResult() != null) {
            return (BigDecimal) em.createQuery(requete).getSingleResult();
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getMontantTotalEnRetard(Utilisateur connectedUser) {
        Date currentDate = new Date();

        String requete = "select sum(f.montantDu) from Devis f where FUNC('DATEDIFF','" + DateUtil.getSqlDate(currentDate)
                + "' , f.dateEcheance)>0 and f.etat = 'Non payée' and f.utilisateur.societe.id = " + connectedUser.getSociete().getId();

        if (em.createQuery(requete).getSingleResult() != null) {
            return (BigDecimal) em.createQuery(requete).getSingleResult();
        } else {
            return BigDecimal.ZERO;
        }
    }



    
}
