/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Utilisateur;
import controler.util.SessionUtil;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ayoub
 */
@Stateless
public class UtilisateurFacade extends AbstractFacade<Utilisateur> {

    @PersistenceContext(unitName = "Gestion_commercialePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UtilisateurFacade() {
        super(Utilisateur.class);
    }

    public int getNombreUtilisatuerSociete() {
        return (int) em.createQuery("select u from Utilisateur u where u.societe.id = " + SessionUtil.getConnectedUser().getSociete().getId()).getSingleResult();
    }

    public long getNumberAbonnee() {
        String requete="select count(s) from Societe s";
        if(em.createQuery(requete).getSingleResult()!=null)return (long) em.createQuery(requete).getSingleResult();
        else return 0;

    }
    public long getNumberAbonneeHorsService() {
        String requete = "select count(s) from Societe s where s.droitExploitation = 0";
        if(em.createQuery(requete).getSingleResult()!=null)return (long) em.createQuery(requete).getSingleResult();
        else return 0;

    }
    public long getNumberAbonneeActive() {
        String requete = "select count(s) from Societe s where s.droitExploitation = 1";
        if(em.createQuery(requete).getSingleResult()!=null)return (long) em.createQuery(requete).getSingleResult();
        else return 0;
    }
    public long getNumberFacture() {
        String requete = "select count(f) from Facture f";
        if(em.createQuery(requete).getSingleResult()!=null)return (long) em.createQuery(requete).getSingleResult();
        else return 0;
    }
    public long getNumberDevis() {
        String requete = "select count(d) from Devis d";
        if(em.createQuery(requete).getSingleResult()!=null)return (long) em.createQuery(requete).getSingleResult();
        else return 0;
    }
    public long getNumberLivraison() {
        String requete = "select count(l) from Livraison l";
        if(em.createQuery(requete).getSingleResult()!=null)return (long) em.createQuery(requete).getSingleResult();
        else return 0;
    }
    public long getNumberCommande() {
        String requete = "select count(c) from Commande c";
        if(em.createQuery(requete).getSingleResult()!=null)return (long) em.createQuery(requete).getSingleResult();
        else return 0;
    }

    public Utilisateur authentification(String email, String password) {

        try {
            Utilisateur user = (Utilisateur) em.createQuery("SELECT u FROM Utilisateur AS u WHERE u.email ='" + email + "' AND u.password ='" + password + "'").getSingleResult();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Utilisateur> listUsersSociete(Utilisateur utilisateur, String recherche) {
        String requete = "select u from Utilisateur u where u.societe.id = " + utilisateur.getSociete().getId();
         System.out.println("haaaa requete dyal les users " + requete);
        System.out.println(em.createQuery(requete).getResultList());
        if (!recherche.equals("")) {
            requete += " and (u.nom like '%" + recherche + "%' "
                    + "or u.prenom like '%" + recherche + "%' "
                    + "or u.email like '%" + recherche + "%' "
                    + "or u.telephone like '%" + recherche + "%' "
                    + "or u.profil like '%" + recherche + "%') "
                    + "and u.societe.id = " + utilisateur.getSociete().getId();

        }
         System.out.println("haaaa requete dyal les users " + requete);
        System.out.println(em.createQuery(requete).getResultList());
        if(em.createQuery(requete).getResultList()!=null) return em.createQuery(requete).getResultList();
        else return new ArrayList<>();

    }

    public List<Utilisateur> userExiste(String email) {
        String requete = "select u from Utilisateur u where u.email = '" + email + "'";
        System.out.println("haa wach utilisateur kayn : " + requete);
        System.out.println("ha wach kayn: " + em.createQuery(requete).getResultList());
        return em.createQuery(requete).getResultList();
    }

    public String getPasswordByMail(String email) {
        String requete = "select u.password from Utilisateur u where u.email = '" + email + "'";
        System.out.println("haa wach utilisateur kayn : " + requete);
        System.out.println("ha wach kayn: " + em.createQuery(requete).getResultList());
        return (String) em.createQuery(requete).getSingleResult();
    }
}
