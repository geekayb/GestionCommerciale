/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author Ayoub
 */
@Entity
public class Commande implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateCommande;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateEcheance;
    private String sujet;
    private String status;
     private String devise;
    private String conditionCommande;
    private BigDecimal sousTotal;
    private BigDecimal remise;
    private BigDecimal nouveauSousTotal;
    private BigDecimal totalTax;
    private BigDecimal montantDu;
    @ManyToOne
    private Fournisseur fournisseur;
    @OneToMany(mappedBy = "commande")
    private List<CommandeItem> commandeItems;
    
    @ManyToOne
    public Utilisateur utilisateur;

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDevise() {
         if(devise==null)devise="";
        return devise;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }

    
    public String getConditionCommande() {
        return conditionCommande;
    }

    public void setConditionCommande(String conditionCommande) {
        this.conditionCommande = conditionCommande;
    }
    
    

    public Date getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public Date getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(Date dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getSousTotal() {
        return sousTotal;
    }

    public void setSousTotal(BigDecimal sousTotal) {
        this.sousTotal = sousTotal;
    }

    public BigDecimal getRemise() {
        if(remise==null)remise=BigDecimal.ZERO;
        return remise;
    }

    public void setRemise(BigDecimal remise) {
        this.remise = remise;
    }

    public BigDecimal getNouveauSousTotal() {
        return nouveauSousTotal;
    }

    public void setNouveauSousTotal(BigDecimal nouveauSousTotal) {
        this.nouveauSousTotal = nouveauSousTotal;
    }

    public BigDecimal getMontantDu() {
        return montantDu;
    }

    public void setMontantDu(BigDecimal montantDu) {
        this.montantDu = montantDu;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public List<CommandeItem> getCommandeItems() {
        return commandeItems;
    }

    public void setCommandeItems(List<CommandeItem> commandeItems) {
        this.commandeItems = commandeItems;
    }

    public BigDecimal getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(BigDecimal totalTax) {
        this.totalTax = totalTax;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Commande)) {
            return false;
        }
        Commande other = (Commande) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.Commande[ id=" + id + " ]";
    }
    
}
