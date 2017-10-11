/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Livraison;
import bean.LivraisonItem;
import bean.Livraison;
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
public class LivraisonFacade extends AbstractFacade<Livraison> {

    @PersistenceContext(unitName = "Gestion_commercialePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LivraisonFacade() {
        super(Livraison.class);
    }

    public List<Livraison> listLivraisonSociete(Utilisateur utilisateur) {
        String requete = "select d from Livraison c where d.utilisateur.societe.id = " + utilisateur.getSociete().getId();
        System.out.println("haaaa requete dyal les livraison " + requete);
        System.out.println(em.createQuery(requete).getResultList());
        return em.createQuery(requete).getResultList();

    }

    public Double getQteByLivraisonItem(LivraisonItem livraisonItem) {
        return (Double) em.createQuery("SELECT item.quantite from LivraisonItem item where item.id = " + livraisonItem.getId()).getSingleResult();
    }

    public List<Livraison> findLivraisonsByEtat(String etat, Utilisateur utilisateur) {
        String requete = "";
        if (etat.equals("tout")) {
            requete = "select f from Livraison f where f.utilisateur.societe.id = " + utilisateur.getSociete().getId();
        } else {
            requete = "select f from Livraison f where f.utilisateur.societe.id = " + utilisateur.getSociete().getId() + " and f.status = '" + etat + "'";

        }
        return em.createQuery(requete).getResultList();
    }

    public List<Livraison> findLivraisonByCritere(String recherche, Utilisateur utilisateur) {

        String requete = "SELECT l FROM Livraison l where l.utilisateur.societe.id = " + utilisateur.getSociete().getId();
        if (!recherche.equals("")) {
            requete
                    += " and (l.client.raisonSociale like '%" + recherche + "%' "
                    + " or l.dateLivraison like '%" + recherche + "%' "
                    + " or l.id like '%" + recherche + "%' "
                    + " or l.montantDu like '%" + recherche + "%' "
                    + " or l.status like '%" + recherche + "%') "
                    + " and l.utilisateur.societe.id = " + utilisateur.getSociete().getId();
        }

        //  System.out.println(requete);
        return em.createQuery(requete).getResultList();
    }

    public List<Livraison> findLivraisonRetardByCritere(String recherche, Utilisateur utilisateur) {
        Date currentDate = new Date();
        String requete = "select l from Livraison l where FUNC('DATEDIFF','" + DateUtil.getSqlDate(currentDate) + "' , l.dateEcheance)>0 and f.utilisateur.societe.id = " + utilisateur.getSociete().getId() + " and l.status = 'Non payée'";
        //System.out.println("haaaaa recherche" + recherche);
        if (!recherche.equals("")) {
             requete
                    += " and (l.client.raisonSociale like '%" + recherche + "%' "
                    + " or l.dateLivraison like '%" + recherche + "%' "
                    + " or l.id like '%" + recherche + "%' "
                    + " or l.montantDu like '%" + recherche + "%' "
                    + " or l.status like '%" + recherche + "%') "
                    + " and l.utilisateur.societe.id = " + utilisateur.getSociete().getId();
        }
        //System.out.println("haa requte"+ requete);
        //System.out.println("haa lista"+ em.createQuery(requete).getResultList());

        return em.createQuery(requete).getResultList();
    }

    public List<Livraison> listLivraisonSociete(Utilisateur utilisateur, String recherche, String etat) {
        String requete = "select l from Livraison l where l.utilisateur.societe.id = " + utilisateur.getSociete().getId();
        if (!recherche.equals("")) {
             requete
                    += " and (l.client.raisonSociale like '%" + recherche + "%' "
                    + " or l.dateLivraison like '%" + recherche + "%' "
                    + " or l.id like '%" + recherche + "%' "
                    + " or l.montantDu like '%" + recherche + "%' "
                    + " or l.status like '%" + recherche + "%') "
                    + " and l.utilisateur.societe.id = " + utilisateur.getSociete().getId();
       
        }if (!etat.equals("")) {
            if (!etat.equals("tout")) {
                requete += " and f.status = '" + etat + "'";

            }
        }
        //System.out.println("haaaa requete dyal les livraisons " + requete);
        // System.out.println(em.createQuery(requete).getResultList());
        return em.createQuery(requete).getResultList();

    }
    

    public BigDecimal getMontantTotalPaye(Utilisateur connectedUser) {
        String requete = "select sum(f.montantPaye) from Livraison f where f.status = 'payée' and f.utilisateur.societe.id = " + connectedUser.getSociete().getId();
        if (em.createQuery(requete).getSingleResult() != null) {
            return (BigDecimal) em.createQuery(requete).getSingleResult();
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getMontantTotalNonPaye(Utilisateur connectedUser) {
        String requete = "select sum(f.montantDu) from Livraison f where f.status = 'Non payée' and f.utilisateur.societe.id = " + connectedUser.getSociete().getId();
        if (em.createQuery(requete).getSingleResult() != null) {
            return (BigDecimal) em.createQuery(requete).getSingleResult();
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getMontantTotalEnRetard(Utilisateur connectedUser) {
        Date currentDate = new Date();

        String requete = "select sum(f.montantDu) from Livraison f where FUNC('DATEDIFF','" + DateUtil.getSqlDate(currentDate)
                + "' , f.dateEcheance)>0 and f.status = 'Non payée' and f.utilisateur.societe.id = " + connectedUser.getSociete().getId();

        if (em.createQuery(requete).getSingleResult() != null) {
            return (BigDecimal) em.createQuery(requete).getSingleResult();
        } else {
            return BigDecimal.ZERO;
        }
    }

}
