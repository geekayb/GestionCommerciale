/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Commande;
import bean.Commande;
import bean.CommandeItem;
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
public class CommandeFacade extends AbstractFacade<Commande> {

    @PersistenceContext(unitName = "Gestion_commercialePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CommandeFacade() {
        super(Commande.class);
    }

     public List<Commande> listCommandeSociete(Utilisateur utilisateur) {
        String requete = "select d from Commande c where d.utilisateur.societe.id = " + utilisateur.getSociete().getId();
        System.out.println("haaaa requete dyal les commande " + requete);
        System.out.println(em.createQuery(requete).getResultList());
        return em.createQuery(requete).getResultList();

    }
     
      public Double getQteByCommandeItem(CommandeItem commandeItem) {
        return (Double) em.createQuery("SELECT item.quantite from CommandeItem item where item.id = " + commandeItem.getId()).getSingleResult();
    }

  

    public List<Commande> findCommandesByEtat(String etat, Utilisateur utilisateur) {
        String requete = "";
        if (etat.equals("tout")) {
            requete = "select f from Commande f where f.utilisateur.societe.id = " + utilisateur.getSociete().getId();
        } else {
            requete = "select f from Commande f where f.utilisateur.societe.id = " + utilisateur.getSociete().getId() + " and f.status = '" + etat + "'";

        }
        return em.createQuery(requete).getResultList();
    }

    public List<Commande> findCommandeByCritere(String recherche, Utilisateur utilisateur) {

        String requete = "SELECT c FROM Commande c where c.utilisateur.societe.id = " + utilisateur.getSociete().getId();
        if (!recherche.equals("")) {
            requete
                    += " and (c.fournisseur.nom like '%" + recherche + "%' "
                    + " or c.dateCommande like '%" + recherche + "%' "
                    + " or c.id like '%" + recherche + "%' "
                    + " or c.montantDu like '%" + recherche + "%' "
                    
                    + " or c.status like '%" + recherche + "%') "
                    + " and c.utilisateur.societe.id = " + utilisateur.getSociete().getId();
        }

        //  System.out.println(requete);
        return em.createQuery(requete).getResultList();
    }



    public List<Commande> findCommandeRetardByCritere(String recherche, Utilisateur utilisateur) {
        Date currentDate = new Date();
        String requete = "select c from Commande c where FUNC('DATEDIFF','" + DateUtil.getSqlDate(currentDate) + "' , c.dateEcheance)>0 and c.utilisateur.societe.id = " + utilisateur.getSociete().getId() + " and f.status = 'Non payée'";
        //System.out.println("haaaaa recherche" + recherche);
        if (!recherche.equals("")) {
            requete
                    += " and (c.fournisseur.nom like '%" + recherche + "%' "
                    + " or c.dateCommande like '%" + recherche + "%' "
                    + " or c.id like '%" + recherche + "%' "
                    + " or c.montantDu like '%" + recherche + "%' "
                    
                    + " or c.status like '%" + recherche + "%') "
                    + " and c.utilisateur.societe.id = " + utilisateur.getSociete().getId();
        }
        //System.out.println("haa requte"+ requete);
        //System.out.println("haa lista"+ em.createQuery(requete).getResultList());

        return em.createQuery(requete).getResultList();
    }

    public List<Commande> listCommandeSociete(Utilisateur utilisateur, String recherche, String etat) {
        String requete = "select c from Commande c where c.utilisateur.societe.id = " + utilisateur.getSociete().getId();
        if (!recherche.equals("")) {
           requete
                    += " and (c.fournisseur.nom like '%" + recherche + "%' "
                    + " or c.dateCommande like '%" + recherche + "%' "
                    + " or c.id like '%" + recherche + "%' "
                    + " or c.montantDu like '%" + recherche + "%' "
                    
                    + " or c.status like '%" + recherche + "%') "
                    + " and c.utilisateur.societe.id = " + utilisateur.getSociete().getId();

        }
        if (!etat.equals("")) {
            if (!etat.equals("tout")) {
                requete += " and c.status = '" + etat + "'";

            }
        }
        //System.out.println("haaaa requete dyal les commandes " + requete);
        // System.out.println(em.createQuery(requete).getResultList());
        return em.createQuery(requete).getResultList();

    }

    public BigDecimal getMontantTotalPaye(Utilisateur connectedUser) {
        String requete = "select sum(f.montantPaye) from Commande f where f.status = 'payée' and f.utilisateur.societe.id = " + connectedUser.getSociete().getId();
        if (em.createQuery(requete).getSingleResult() != null) {
            return (BigDecimal) em.createQuery(requete).getSingleResult();
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getMontantTotalNonPaye(Utilisateur connectedUser) {
        String requete = "select sum(f.montantDu) from Commande f where f.status = 'Non payée' and f.utilisateur.societe.id = " + connectedUser.getSociete().getId();
        if (em.createQuery(requete).getSingleResult() != null) {
            return (BigDecimal) em.createQuery(requete).getSingleResult();
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getMontantTotalEnRetard(Utilisateur connectedUser) {
        Date currentDate = new Date();

        String requete = "select sum(f.montantDu) from Commande f where FUNC('DATEDIFF','" + DateUtil.getSqlDate(currentDate)
                + "' , f.dateEcheance)>0 and f.status = 'Non payée' and f.utilisateur.societe.id = " + connectedUser.getSociete().getId();

        if (em.createQuery(requete).getSingleResult() != null) {
            return (BigDecimal) em.createQuery(requete).getSingleResult();
        } else {
            return BigDecimal.ZERO;
        }
    }


    }


