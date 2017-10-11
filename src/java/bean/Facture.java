/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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
public class Facture implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
        
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateFacture;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateEcheance;
    private String etat;
    private String devise;
    private String conditionFacture;
    private BigDecimal sousTotal;
    private BigDecimal totalTax;
    private BigDecimal nouveauSousTotal;
    private BigDecimal remise;
    private BigDecimal totalAvecRemise;
    private BigDecimal montantPaye;
    private BigDecimal montantDu;
   
    @ManyToOne
    private Client client;
  
    @OneToMany(mappedBy = "facture")
    private List<FactureItem> factureItems;

     @OneToMany(mappedBy = "facture")
    private List<Paiement> paiements;
 
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
    
    

    public String getConditionFacture() {
        if(conditionFacture==null)conditionFacture = "";
        return conditionFacture;
    }

    public void setConditionFacture(String conditionFacture) {
        this.conditionFacture = conditionFacture;
    }
    

    public Date getDateFacture() {
        return dateFacture;
    }

    public void setDateFacture(Date dateFacture) {
        this.dateFacture = dateFacture;
    }

    public Date getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(Date dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public BigDecimal getSousTotal() {
        return sousTotal;
    }

    public void setSousTotal(BigDecimal sousTotal) {
        this.sousTotal = sousTotal;
    }

    public BigDecimal getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(BigDecimal totalTax) {
        this.totalTax = totalTax;
    }

    public BigDecimal getNouveauSousTotal() {
        return nouveauSousTotal;
    }

    public void setNouveauSousTotal(BigDecimal nouveauSousTotal) {
        this.nouveauSousTotal = nouveauSousTotal;
    }

    public BigDecimal getRemise() {
        if(remise==null)remise=BigDecimal.ZERO;
        return remise;
    }

    public void setRemise(BigDecimal remise) {
        this.remise = remise;
    }

    public BigDecimal getTotalAvecRemise() {
        return totalAvecRemise;
    }

    public void setTotalAvecRemise(BigDecimal totalAvecRemise) {
        this.totalAvecRemise = totalAvecRemise;
    }

    public BigDecimal getMontantPaye() {
        return montantPaye;
    }

    public void setMontantPaye(BigDecimal montantPaye) {
        this.montantPaye = montantPaye;
    }

    public BigDecimal getMontantDu() {
        return montantDu;
    }

    public void setMontantDu(BigDecimal montantDu) {
        this.montantDu = montantDu;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<FactureItem> getFactureItems() {
        if(factureItems==null) factureItems = new ArrayList<>();
        return factureItems;
    }

    public void setFactureItems(List<FactureItem> factureItems) {
        this.factureItems = factureItems;
    }

    public List<Paiement> getPaiements() {
        return paiements;
    }

    public void setPaiements(List<Paiement> paiements) {
        this.paiements = paiements;
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
        if (!(object instanceof Facture)) {
            return false;
        }
        Facture other = (Facture) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.Facture[ id=" + id + " ]";
    }

    
    
    
}
