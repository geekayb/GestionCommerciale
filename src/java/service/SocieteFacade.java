/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Societe;
import bean.Utilisateur;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ayoub
 */
@Stateless
public class SocieteFacade extends AbstractFacade<Societe> {

    @PersistenceContext(unitName = "Gestion_commercialePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SocieteFacade() {
        super(Societe.class);
    }

    public List<Societe> listSociete(Utilisateur u, String recherche, String etat) {
        String requete = "select s from Societe s where 1=1";
        if (!recherche.equals("")) {
            requete += " and s.raisonSociale like '%" + recherche + "%' ";

        }
        if (!etat.equals("")) {
            if (etat.equals("active")) {
                requete += " and s.droitExploitation = 1 ";

            }else if(etat.equals("horsService")){
                  requete += " and s.droitExploitation = 0 ";
            }
        }
        //System.out.println("haaaa requete dyal les societes " + requete);
        // System.out.println(em.createQuery(requete).getResultList());
        return em.createQuery(requete).getResultList();
    }
    
    public List<Societe> findSocietesByEtat(String etat, Utilisateur utilisateur) {
        String requete = "";
        if (etat.equals("tout")) {
            requete = "select s from Societe s";
        } else if(etat.equals("active")){
            requete = "select s from Societe s where s.droitExploitation = 1";

        }else if(etat.equals("horsService")){
            requete = "select s from Societe s where s.droitExploitation = 0";
        }
        return em.createQuery(requete).getResultList();
    }

    public List<Societe> findSocieteByCritere(String recherche, Utilisateur utilisateur) {

        String requete = "SELECT s FROM Societe s where 1=1";
        if (!recherche.equals("")) {
            requete 
                    += " and s.raisonSociale like '%" + recherche + "%' "
                    + " or s.secteurActivite like '%" + recherche + "%' "
                    + " or s.activitePrincipale like '%" + recherche + "%' "
                    + " or s.statutJuridique like '%" + recherche + "%' "
                    + " or s.adresse like '%" + recherche + "%' "
                    + " or s.adresse like '%" + recherche + "%' ";
              
        }

          System.out.println(requete);
        return em.createQuery(requete).getResultList();
    }
    
    public long getNumberFacture(Societe societe) {
        String requete = "select count(f) from Facture f where f.utilisateur.societe.id = " + societe.getId();
        if(em.createQuery(requete).getSingleResult()!=null)return (long) em.createQuery(requete).getSingleResult();
        else return 0;
    }
    public long getNumberDevis(Societe societe) {
        String requete = "select count(d) from Devis d where d.utilisateur.societe.id = " + societe.getId();
        if(em.createQuery(requete).getSingleResult()!=null)return (long) em.createQuery(requete).getSingleResult();
        else return 0;
    }
    public long getNumberLivraison(Societe societe) {
        String requete = "select count(l) from Livraison l where l.utilisateur.societe.id = " + societe.getId();
        if(em.createQuery(requete).getSingleResult()!=null)return (long) em.createQuery(requete).getSingleResult();
        else return 0;
    }
    public long getNumberCommande(Societe societe) {
        String requete = "select count(c) from Commande c where c.utilisateur.societe.id = " + societe.getId();
        if(em.createQuery(requete).getSingleResult()!=null)return (long) em.createQuery(requete).getSingleResult();
        else return 0;
    }

    public String getEmailSuperUser() {
        String requete = "select u.email from Utilisateur u where u.profil = 'super utilisateur' ";
        if(em.createQuery(requete).getSingleResult()!=null) return (String) em.createQuery(requete).getSingleResult();
        else return "";
    }
    public String getPasswordSuperUser() {
        String requete = "select u.password from Utilisateur u where u.profil = 'super utilisateur' ";
        if(em.createQuery(requete).getSingleResult()!=null) return (String) em.createQuery(requete).getSingleResult();
        else return "";
    }
    
}
