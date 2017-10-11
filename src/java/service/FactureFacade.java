/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Client;
import bean.Facture;
import bean.FactureItem;
import bean.Paiement;
import bean.Produit;
import bean.Utilisateur;
import controler.util.DateUtil;
import controler.util.SessionUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
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
public class FactureFacade extends AbstractFacade<Facture> {

    @PersistenceContext(unitName = "Gestion_commercialePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FactureFacade() {
        super(Facture.class);
    }

    public Double getQteByFactureItem(FactureItem factureItem) {
        return (Double) em.createQuery("SELECT item.quantite from FactureItem item where item.id = " + factureItem.getId()).getSingleResult();
    }

    public List<Paiement> getPaimentsByFacture(Facture selected) {
        String requete = "select p from Paiement p where p.facture.id = " + selected.getId();
        return em.createQuery(requete).getResultList();
    }

    public List<Facture> findFacturesByEtat(String etat, Utilisateur utilisateur) {
        String requete = "";
        if (etat.equals("tout")) {
            requete = "select f from Facture f where f.utilisateur.societe.id = " + utilisateur.getSociete().getId();
        } else {
            requete = "select f from Facture f where f.utilisateur.societe.id = " + utilisateur.getSociete().getId() + " and f.etat = '" + etat + "'";

        }
        return em.createQuery(requete).getResultList();
    }

    public List<Facture> findFactureByCritere(String recherche, Utilisateur utilisateur) {

        String requete = "SELECT f FROM Facture f where f.utilisateur.societe.id = " + utilisateur.getSociete().getId();
        if (!recherche.equals("")) {
            requete
                    += " and f.client.raisonSociale like '%" + recherche + "%' "
                    + " or f.dateFacture like '%" + recherche + "%' "
                    + " or f.id like '%" + recherche + "%' "
                    + " or f.montantDu like '%" + recherche + "%' "
                    + " or f.montantPaye like '%" + recherche + "%' "
                    + " or f.etat like '%" + recherche + "%' "
                    + " and f.utilisateur.societe.id = " + utilisateur.getSociete().getId();
        }

        //  System.out.println(requete);
        return em.createQuery(requete).getResultList();
    }

    public List<Facture> findFactureEnRetard(Utilisateur utilisateur, String recherche) {
        Date currentDate = new Date();
        String requete = "select f from Facture f where FUNC('DATEDIFF','" + DateUtil.getSqlDate(currentDate) + "' , f.dateEcheance)>0 and f.etat = 'Non payée' and f.utilisateur.societe.id = " + utilisateur.getSociete().getId();

        if (!recherche.equals("")) {
            requete += " and (f.client.raisonSociale like '%" + recherche + "%' "
            + "or f.dateFacture like '%" + recherche + "%' "
            + "or f.id like '%" + recherche + "%' "
            + "or f.montantDu like '%" + recherche + "%' "
            + "or f.montantPaye like '%" + recherche + "%') ";
        }
        //System.out.println("haa requete " + requete);
        //System.out.println("haaa les factures en retard: " + em.createQuery(requete).getResultList());
        return em.createQuery(requete).getResultList();

        //SELECT l FROM Locale l where  FUNC('DATEDIFF','" + DateUtil.getSqlDate(currentDate) + "' ,l.infoNotification." + nomDate + ") >30 "
    }

    public List<Facture> findFactureRetardByCritere(String recherche, Utilisateur utilisateur) {
        Date currentDate = new Date();
        String requete = "select f from Facture f where FUNC('DATEDIFF','" + DateUtil.getSqlDate(currentDate) + "' , f.dateEcheance)>0 and f.utilisateur.societe.id = " + utilisateur.getSociete().getId() + " and f.etat = 'Non payée'";
        //System.out.println("haaaaa recherche" + recherche);
        if (!recherche.equals("")) {
            requete += " and (f.client.raisonSociale like '%" + recherche + "%' "
            + "or f.dateFacture like '%" + recherche + "%' "
            + "or f.id like '%" + recherche + "%' "
            + "or f.montantDu like '%" + recherche + "%' "
            + "or f.montantPaye like '%" + recherche + "%') ";
        }
        //System.out.println("haa requte"+ requete);
        //System.out.println("haa lista"+ em.createQuery(requete).getResultList());

        return em.createQuery(requete).getResultList();
    }

    public List<Facture> listFactureSociete(Utilisateur utilisateur, String recherche, String etat) {
        String requete = "select f from Facture f where f.utilisateur.societe.id = " + utilisateur.getSociete().getId();
        if (!recherche.equals("")) {
            requete += " and (f.client.raisonSociale like '%" + recherche + "%' "
            + "or f.dateFacture like '%" + recherche + "%' "
            + "or f.id like '%" + recherche + "%' "
            + "or f.montantDu like '%" + recherche + "%' "
            + "or f.montantPaye like '%" + recherche + "%') ";

        }
        if (!etat.equals("")) {
            if (!etat.equals("tout")) {
                requete += " and f.etat = '" + etat + "'";

            }
        }
        //System.out.println("haaaa requete dyal les factures " + requete);
        // System.out.println(em.createQuery(requete).getResultList());
        return em.createQuery(requete).getResultList();

    }

    public BigDecimal getMontantTotalPayeDh(Utilisateur connectedUser) {
        if (SessionUtil.getConnectedUser() != null) {

            String requete = "select sum(f.montantPaye) from Facture f where f.devise = 'Dhs' and f.etat = 'payée' and f.utilisateur.societe.id = " + connectedUser.getSociete().getId();
            //if (em.createQuery(requete).getSingleResult() != null) {
             if (em.createQuery(requete).getSingleResult() != null) {
                return (BigDecimal) em.createQuery(requete).getSingleResult();
            } else {
                return BigDecimal.ZERO;
            }
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getMontantTotalNonPayeDh(Utilisateur connectedUser) {
        if (SessionUtil.getConnectedUser() != null) {

            String requete = "select sum(f.montantDu) from Facture f where f.devise = 'Dhs' and f.etat = 'Non payée'  and f.utilisateur.societe.id = " + connectedUser.getSociete().getId();
            // if (em.createQuery(requete).getSingleResult() != null) {
             if (em.createQuery(requete).getSingleResult() != null) {
                return (BigDecimal) em.createQuery(requete).getSingleResult();
            } else {
                return BigDecimal.ZERO;
            }
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getMontantTotalEnRetardDh(Utilisateur connectedUser) {

        Date currentDate = new Date();
        if (SessionUtil.getConnectedUser() != null) {

            String requete = "select sum(f.montantDu) from Facture f where f.devise = 'Dhs' and FUNC('DATEDIFF','" + DateUtil.getSqlDate(currentDate)
                    + "' , f.dateEcheance)>0 and f.etat = 'Non payée' and f.utilisateur.societe.id = " + connectedUser.getSociete().getId();

             if (em.createQuery(requete).getSingleResult() != null) {
                return (BigDecimal) em.createQuery(requete).getSingleResult();
            } else {
                return BigDecimal.ZERO;
            }
        } else {
            return BigDecimal.ZERO;
        }
    }

    //*************************** dollar
    public BigDecimal getMontantTotalPayeDollar(Utilisateur connectedUser) {
        if (SessionUtil.getConnectedUser() != null) {

            String requete = "select sum(f.montantPaye) from Facture f where f.devise = '$' and f.etat = 'payée' and f.utilisateur.societe.id = " + connectedUser.getSociete().getId();
            if (em.createQuery(requete).getSingleResult() != null) {
                return (BigDecimal) em.createQuery(requete).getSingleResult();
            } else {
                return BigDecimal.ZERO;
            }
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getMontantTotalNonPayeDollar(Utilisateur connectedUser) {
        if (SessionUtil.getConnectedUser() != null) {

            String requete = "select sum(f.montantDu) from Facture f where f.devise = '$' and f.etat = 'Non payée'  and f.utilisateur.societe.id = " + connectedUser.getSociete().getId();
             if (em.createQuery(requete).getSingleResult() != null) {
                return (BigDecimal) em.createQuery(requete).getSingleResult();
            } else {
                return BigDecimal.ZERO;
            }
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getMontantTotalEnRetardDollar(Utilisateur connectedUser) {
        if (SessionUtil.getConnectedUser() != null) {

            Date currentDate = new Date();

            String requete = "select sum(f.montantDu) from Facture f where f.devise = '$' and FUNC('DATEDIFF','" + DateUtil.getSqlDate(currentDate)
                    + "' , f.dateEcheance)>0 and f.etat = 'Non payée' and f.utilisateur.societe.id = " + connectedUser.getSociete().getId();

            if (em.createQuery(requete).getSingleResult() != null) {
                return (BigDecimal) em.createQuery(requete).getSingleResult();
            } else {
                return BigDecimal.ZERO;
            }
        } else {
            return BigDecimal.ZERO;
        }
    }

    //***********************************Euro
    public BigDecimal getMontantTotalPayeEuro(Utilisateur connectedUser) {
        if (SessionUtil.getConnectedUser() != null) {

            String requete = "select sum(f.montantPaye) from Facture f where f.devise = '€' and f.etat = 'payée' and f.utilisateur.societe.id = " + connectedUser.getSociete().getId();
            if (em.createQuery(requete).getSingleResult() != null) {
                return (BigDecimal) em.createQuery(requete).getSingleResult();
            } else {
                return BigDecimal.ZERO;
            }
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getMontantTotalNonPayeEuro(Utilisateur connectedUser) {
//        if (SessionUtil.getConnectedUser() != null) {
//
//            String requete = "select sum(f.montantDu) from Facture f where f.devise = '€' and f.etat = 'Non payée'  and f.utilisateur.societe.id = " + connectedUser.getSociete().getId();
//            return (BigDecimal) em.createQuery(requete).getSingleResult();
//        } else {
//            return BigDecimal.ZERO;
//        }

        if (SessionUtil.getConnectedUser() != null) {

            String requete = "select sum(f.montantDu) from Facture f where f.devise = '€' and f.etat = 'Non payée' and f.utilisateur.societe.id = " + connectedUser.getSociete().getId();
            if (em.createQuery(requete).getSingleResult() != null) {
                return (BigDecimal) em.createQuery(requete).getSingleResult();
            } else {
                return BigDecimal.ZERO;
            }
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getMontantTotalEnRetardEuro(Utilisateur connectedUser) {
        if (SessionUtil.getConnectedUser() != null) {

            Date currentDate = new Date();

            String requete = "select sum(f.montantDu) from Facture f where f.devise = '€' and FUNC('DATEDIFF','" + DateUtil.getSqlDate(currentDate)
                    + "' , f.dateEcheance)>0 and f.etat = 'Non payée' and f.utilisateur.societe.id = " + connectedUser.getSociete().getId();

             if (em.createQuery(requete).getSingleResult() != null) {
                return (BigDecimal) em.createQuery(requete).getSingleResult();
            } else {
                return BigDecimal.ZERO;
            }
        } else {
            return BigDecimal.ZERO;
        }
    }
    //*******************************************

    public long nbrFactureCrees(Utilisateur connectedUser) {
        if (SessionUtil.getConnectedUser() != null) {
            String requete = "Select count(f) from Facture f where f.utilisateur.societe.id = " + connectedUser.getSociete().getId();

            return (long) em.createQuery(requete).getSingleResult();
        } else {
            return 0;
        }
    }

    public long nbrDevis(Utilisateur connectedUser) {
        if (SessionUtil.getConnectedUser() != null) {
            String requete = "Select count(d) from Devis d where d.utilisateur.societe.id = " + connectedUser.getSociete().getId();

            return (long) em.createQuery(requete).getSingleResult();
        } else {
            return 0;
        }
    }

    public long nbrCommande(Utilisateur connectedUser) {
        if (SessionUtil.getConnectedUser() != null) {
            String requete = "Select count(c) from Commande c where c.utilisateur.societe.id = " + connectedUser.getSociete().getId();

            return (long) em.createQuery(requete).getSingleResult();
        } else {
            return 0;
        }
    }

    public long nbrLivaison(Utilisateur connectedUser) {
        if (SessionUtil.getConnectedUser() != null) {
            String requete = "Select count(l) from Livraison l where l.utilisateur.societe.id = " + connectedUser.getSociete().getId();
            return (long) em.createQuery(requete).getSingleResult();
        } else {
            return 0;
        }
    }

    public long nbrFactureNonPayee(Utilisateur connectedUser) {
        if (SessionUtil.getConnectedUser() != null) {
            String requete = "Select count(f) from Facture f where f.etat = 'Non payée' and f.utilisateur.societe.id = " + connectedUser.getSociete().getId();
            return (long) em.createQuery(requete).getSingleResult();
        } else {
            return 0;
        }
    }

    public long nbrFactureEnRetard(Utilisateur connectedUser) {
        Date currentDate = new Date();
        if (SessionUtil.getConnectedUser() != null) {
            String requete = "select count(f) from Facture f where FUNC('DATEDIFF','" + DateUtil.getSqlDate(currentDate)
                    + "' , f.dateEcheance)>0 and f.etat = 'Non payée' and f.utilisateur.societe.id = " + connectedUser.getSociete().getId();
            return (long) em.createQuery(requete).getSingleResult();
        } else {
            return 0;
        }
    }

    public long nbrFacturePayee(Utilisateur connectedUser) {
        if (SessionUtil.getConnectedUser() != null) {
            String requete = "Select count(f) from Facture f where f.etat = 'payée' and f.utilisateur.societe.id = " + connectedUser.getSociete().getId();
            return (long) em.createQuery(requete).getSingleResult();
        } else {
            return 0;
        }
    }

    public long nbrProduit(Utilisateur connectedUser) {
        if (SessionUtil.getConnectedUser() != null) {
            String requete = "Select count(p) from Produit p where p.utilisateur.societe.id = " + connectedUser.getSociete().getId();
            return (long) em.createQuery(requete).getSingleResult();
        } else {
            return 0;
        }
    }

    public long nbrClient(Utilisateur connectedUser) {
        if (SessionUtil.getConnectedUser() != null) {
            String requete = "Select count(c) from Client c where c.utilisateur.societe.id = " + connectedUser.getSociete().getId();
            return (long) em.createQuery(requete).getSingleResult();
        } else {
            return 0;
        }
    }

    public long nbrFournisseur(Utilisateur connectedUser) {
        if (SessionUtil.getConnectedUser() != null) {
            String requete = "Select count(f) from Fournisseur f where f.utilisateur.societe.id = " + connectedUser.getSociete().getId();
            return (long) em.createQuery(requete).getSingleResult();
        } else {
            return 0;
        }
    }

    public long nbrUsers(Utilisateur connectedUser) {
        if (SessionUtil.getConnectedUser() != null) {
            String requete = "Select count(u) from Utilisateur u where u.societe.id = " + connectedUser.getSociete().getId();
            return (long) em.createQuery(requete).getSingleResult();
        } else {
            return 0;
        }
    }

    public List<Facture> listFactureRecentes(Utilisateur u) {
        Date currentDate = new Date();
        String requete = "select f from Facture f where FUNC('DATEDIFF','" + DateUtil.getSqlDate(currentDate) + "' , f.dateEcheance)>30 and f.utilisateur.societe.id = " + u.getSociete().getId();
        return (List<Facture>) em.createQuery(requete).getResultList();
    }

    public List<Facture> listFactureRapport(Utilisateur u, Client client) {
        String requete = "select f from Facture f where f.utilisateur.societe.id = " + u.getSociete().getId();
        if (client != null && client.getId() != null) {
            requete += " and f.client.id = " + client.getId();

        }

        System.out.println("haaaa requete dyal les factures " + requete);
        System.out.println(em.createQuery(requete).getResultList());
        if (em.createQuery(requete).getResultList() != null) {
            return em.createQuery(requete).getResultList();
        } else {
            return new ArrayList<>();
        }
    }

    public BigDecimal getChiffreAffaireByAnnee(Utilisateur connectedUser, int annee) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.YEAR, annee + 1);
        Date dateFin = calendar.getTime();
        Calendar calendar2 = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.YEAR, annee);
        Date dateDebut = calendar2.getTime();

        String requete = "select sum(f.totalAvecRemise) from Facture f where FUNC('DATEDIFF','" + DateUtil.getSqlDate(dateDebut)
                + "' , f.dateFacture)>0 and FUNC('DATEDIFF','" + DateUtil.getSqlDate(dateFin)
                + "' , f.dateFacture)<0 and f.etat = 'payée' and f.utilisateur.societe.id = " + connectedUser.getSociete().getId();

        if (em.createQuery(requete).getSingleResult() != null) {
            System.out.println("haa CA : " + em.createQuery(requete).getSingleResult().toString());

            return (BigDecimal) em.createQuery(requete).getSingleResult();

        } else {
            System.out.println("CA rah null");

            return BigDecimal.ZERO;
        }
    }

    public List<Produit> getProduitByDevise(String devise, Utilisateur utilisateur) {
        String requete = "select p from Produit p where p.utilisateur.societe.id = " + utilisateur.getSociete().getId();
        if (!devise.equals("")) {
            if (devise.equals("Dhs")) {
                requete += " and p.prixDirham <> null";
            } else if (devise.equals("$")) {
                requete += " and p.prixDollar <> null";
            } else if (devise.equals("€")) {
                requete += " and p.prixEuro <> null"; //prix euro
            }
        } else {
            return new ArrayList<>();
        }
        System.out.println("haaa requeta dyal les produits devise " + requete);
        System.out.println("haa res " + em.createQuery(requete).getResultList());
        return em.createQuery(requete).getResultList();
    }

}
