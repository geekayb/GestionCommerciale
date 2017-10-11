/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Client;
import bean.Paiement;
import bean.Utilisateur;
import controler.util.DateUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
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
public class PaiementFacade extends AbstractFacade<Paiement> {

    @PersistenceContext(unitName = "Gestion_commercialePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PaiementFacade() {
        super(Paiement.class);
    }

    public List<Paiement> findPaimentByCritere(Utilisateur u, Date dateDebute, Date dateFin, Client client) {
        //System.out.println("haa raison sociale: " + client.getRaisonSociale());
        String requete = "select p from Paiement p where p.facture.utilisateur.societe.id = " + u.getSociete().getId();
        if (client != null && client.getRaisonSociale() != null) {
            requete += " and p.facture.client.id = " + client.getId();
        }
        if (dateDebute != null && dateFin != null) {
            requete += " and FUNC('DATEDIFF','" + DateUtil.getSqlDate(dateDebute) + "' , p.datePaiement)<0 "
                    + " and FUNC('DATEDIFF','" + DateUtil.getSqlDate(dateFin) + "' , p.datePaiement)>0";
        }
        if (em.createQuery(requete).getResultList() != null) {
            System.out.println("haa les requste dyal les paiements : " + requete);
            System.out.println("haa rsulta" + em.createQuery(requete).getResultList());
            return em.createQuery(requete).getResultList();
        } else {
            return new ArrayList<>();
        }
    }

    public BigDecimal getMontantTotalPaiementsDh(Utilisateur connectedUser, Date dateDebute, Date dateFin, Client client) {
        String requete = "select sum(p.montant) from Paiement p where p.facture.devise = 'Dhs' and p.facture.utilisateur.societe.id = " + connectedUser.getSociete().getId();
        if (client != null && client.getRaisonSociale() != null) {
            requete += " and p.facture.client.id = " + client.getId();
        }
         if (dateDebute != null && dateFin != null) {
            requete += " and FUNC('DATEDIFF','" + DateUtil.getSqlDate(dateDebute) + "' , p.datePaiement)<0 "
                    + " and FUNC('DATEDIFF','" + DateUtil.getSqlDate(dateFin) + "' , p.datePaiement)>0";
        }
 
        if (em.createQuery(requete).getSingleResult() != null) {
            System.out.println("haa les requste dyal les montantTotal : " + requete);
            System.out.println("haa rsulta" + em.createQuery(requete).getResultList());
            return (BigDecimal) em.createQuery(requete).getSingleResult();
        } else {
            return BigDecimal.ZERO;
        }
    }
    
    
    public BigDecimal getMontantTotalPaiementsEuro(Utilisateur connectedUser, Date dateDebute, Date dateFin, Client client) {
        String requete = "select sum(p.montant) from Paiement p where p.facture.devise = '€' and p.facture.utilisateur.societe.id = " + connectedUser.getSociete().getId();
        if (client != null && client.getRaisonSociale() != null) {
            requete += " and p.facture.client.id = " + client.getId();
        }
         if (dateDebute != null && dateFin != null) {
            requete += " and FUNC('DATEDIFF','" + DateUtil.getSqlDate(dateDebute) + "' , p.datePaiement)<0 "
                    + " and FUNC('DATEDIFF','" + DateUtil.getSqlDate(dateFin) + "' , p.datePaiement)>0";
        }
 
        if (em.createQuery(requete).getSingleResult() != null) {
            System.out.println("haa les requste dyal les montantTotal : " + requete);
            System.out.println("haa rsulta" + em.createQuery(requete).getResultList());
            return (BigDecimal) em.createQuery(requete).getSingleResult();
        } else {
            return BigDecimal.ZERO;
        }
    }
    
    
    
    public BigDecimal getMontantTotalPaiementsDollar(Utilisateur connectedUser, Date dateDebute, Date dateFin, Client client) {
        String requete = "select sum(p.montant) from Paiement p where p.facture.devise = '$' and p.facture.utilisateur.societe.id = " + connectedUser.getSociete().getId();
        if (client != null && client.getRaisonSociale() != null) {
            requete += " and p.facture.client.id = " + client.getId();
        }
         if (dateDebute != null && dateFin != null) {
            requete += " and FUNC('DATEDIFF','" + DateUtil.getSqlDate(dateDebute) + "' , p.datePaiement)<0 "
                    + " and FUNC('DATEDIFF','" + DateUtil.getSqlDate(dateFin) + "' , p.datePaiement)>0";
        }
 
        if (em.createQuery(requete).getSingleResult() != null) {
            System.out.println("haa les requste dyal les montantTotal : " + requete);
            System.out.println("haa rsulta" + em.createQuery(requete).getResultList());
            return (BigDecimal) em.createQuery(requete).getSingleResult();
        } else {
            return BigDecimal.ZERO;
        }
    }

}
