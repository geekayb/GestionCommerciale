/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package charts;

/**
 *
 * @author Ayoub
 */
import bean.Utilisateur;
import controler.util.SessionUtil;
import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.primefaces.event.ItemSelectEvent;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.MeterGaugeChartModel;
import org.primefaces.model.chart.PieChartModel;
import service.FactureFacade;

@ManagedBean
public class ChartView implements Serializable {

    private BarChartModel barModel;
    private BarChartModel barModel2;
    private BarChartModel barModel3;
    private PieChartModel pieModel1;
    private MeterGaugeChartModel meterGaugeModel1;
    private MeterGaugeChartModel meterGaugeModel2;
    private MeterGaugeChartModel meterGaugeModel3;
    private LineChartModel lineModel2;
    Utilisateur u = SessionUtil.getConnectedUser();

    @EJB
    FactureFacade factureFacade;

    @PostConstruct
    public void init() {
        
        createBarModels();
        createBarModels2();
        createBarModels3();
        createPieModels();
        
        //  createLineModels();
    
    }

    public LineChartModel getLineModel2() {
        return lineModel2;
    }
//      private void createLineModels() {
//      
//        
//        
//       
//         
//        lineModel2 = initCategoryModel();
//        lineModel2.setTitle("Category Chart");
//        lineModel2.setLegendPosition("e");
//        lineModel2.setShowPointLabels(true);
//        lineModel2.getAxes().put(AxisType.X, new CategoryAxis("Années"));
//        Axis yAxis = lineModel2.getAxis(AxisType.Y);
//        yAxis.setLabel("Chiffre d'affaire");
//        yAxis.setMin(0);
//        
//    }

//       private LineChartModel initCategoryModel() {
//        LineChartModel model = new LineChartModel();
//        Date date = new Date(); // your date
//        Calendar cal = Calendar.getInstance();
//         cal.setTime(date);
//        int year = cal.get(Calendar.YEAR);
//        BigDecimal year1 = factureFacade.getChiffreAffaireByAnnee(SessionUtil.getConnectedUser(), year-4);
//        BigDecimal year2 = factureFacade.getChiffreAffaireByAnnee(SessionUtil.getConnectedUser(), year-3);
//        BigDecimal year3 = factureFacade.getChiffreAffaireByAnnee(SessionUtil.getConnectedUser(), year-2);
//        BigDecimal year4 = factureFacade.getChiffreAffaireByAnnee(SessionUtil.getConnectedUser(), year-1);
//        BigDecimal year5 = factureFacade.getChiffreAffaireByAnnee(SessionUtil.getConnectedUser(), year);
//
//           System.out.println("year1 "+ year1);
//           System.out.println("year2 "+ year2);
//           System.out.println("year3 "+ year3);
//           System.out.println("year4 "+ year4);
//           System.out.println("year5 "+ year5);
//        ChartSeries boys = new ChartSeries();
//        boys.setLabel("Les ventes");
//        boys.set(year - 4 + "", year1.intValue());
//        boys.set(year - 3 + "", year2.intValue());
//        boys.set(year - 2 + "",year3.intValue());
//        boys.set(year - 1 + "", year4.intValue() );
//        boys.set(year + "",year5.intValue());
//       
//
//        model.addSeries(boys);
//
//        return model;
//    }
    public MeterGaugeChartModel getMeterGaugeModel1() {
        return meterGaugeModel1;
    }

    public MeterGaugeChartModel getMeterGaugeModel2() {
        return meterGaugeModel2;
    }

    public MeterGaugeChartModel getMeterGaugeModel3() {
        return meterGaugeModel3;
    }
    

    //compteur
    private MeterGaugeChartModel initMeterGaugeModel() {
        List<Number> intervals = new ArrayList<Number>() {
            {
                if(u!=null)
                add(factureFacade.getMontantTotalPayeDh(u));
                add(factureFacade.getMontantTotalPayeDh(u).add(factureFacade.getMontantTotalNonPayeDh(u)));
               
                
            }
        };

        return new MeterGaugeChartModel(factureFacade.getMontantTotalPayeDh(u), intervals);
    }
    private MeterGaugeChartModel initMeterGaugeModel2() {
        List<Number> intervals = new ArrayList<Number>() {
            {
                if(u!=null)
                add(factureFacade.getMontantTotalNonPayeDh(u));
               
                
            }
        };

        return new MeterGaugeChartModel(factureFacade.getMontantTotalNonPayeDh(u), intervals);
    }
    private MeterGaugeChartModel initMeterGaugeModel3() {
        List<Number> intervals = new ArrayList<Number>() {
            {
                if(u!=null)
                add(factureFacade.getMontantTotalEnRetardDh(u));
                
               
                
            }
        };

        return new MeterGaugeChartModel(factureFacade.getMontantTotalEnRetardDh(u), intervals);
    }

    

    public BarChartModel getBarModel() {
        return barModel;
    }

    public BarChartModel getBarModel2() {
        return barModel2;
    }

    public void setBarModel2(BarChartModel barModel2) {
        this.barModel2 = barModel2;
    }
    
    public BarChartModel getBarModel3() {
        return barModel3;
    }

    public void setBarModel3(BarChartModel barModel3) {
        this.barModel3 = barModel3;
    }

   
    public PieChartModel getPieModel1() {

        return pieModel1;
    }

    private void createBarModels() {
        createBarModel();
    }

    private void createBarModels2() {
        createBarModel2();
    }
    private void createBarModels3() {
        createBarModel3();
    }

    private BarChartModel initBarModel1() {
        BarChartModel model = new BarChartModel();
        model.setAnimate(true);
       
       

        ChartSeries facture = new ChartSeries();
        facture.setLabel("Total factures");
        facture.set("", factureFacade.nbrFactureCrees(SessionUtil.getConnectedUser()));

        ChartSeries paye = new ChartSeries();
        paye.setLabel("Factures Payées");
        paye.set("", factureFacade.nbrFacturePayee(SessionUtil.getConnectedUser()));

        ChartSeries nonPaye = new ChartSeries();
        nonPaye.setLabel("Factures non payées");
        nonPaye.set("", factureFacade.nbrFactureNonPayee(SessionUtil.getConnectedUser()));

        ChartSeries retard = new ChartSeries();
        retard.setLabel("Factures en retard");
        retard.set("", factureFacade.nbrFactureEnRetard(SessionUtil.getConnectedUser()));

        

        model.addSeries(facture);
        model.addSeries(paye);
        model.addSeries(nonPaye);
        model.addSeries(retard);

       

        return model;
    }

    private BarChartModel initBarModel2() {

        BarChartModel model2 = new BarChartModel();
        model2.setAnimate(true);

        

        ChartSeries fac = new ChartSeries();
        fac.setLabel("Factures");
        fac.set("", factureFacade.nbrFactureCrees(SessionUtil.getConnectedUser()));
        ChartSeries devis = new ChartSeries();
        devis.setLabel("Devis");
        devis.set("", factureFacade.nbrDevis(SessionUtil.getConnectedUser()));
        ChartSeries boncommande = new ChartSeries();
        boncommande.setLabel("Bon de commande");
        boncommande.set("", factureFacade.nbrCommande(SessionUtil.getConnectedUser()));
        ChartSeries bonlivraison = new ChartSeries();
        bonlivraison.setLabel("Bon de livraison");
        bonlivraison.set("", factureFacade.nbrLivaison(SessionUtil.getConnectedUser()));

        model2.addSeries(fac);
        model2.addSeries(devis);
        model2.addSeries(boncommande);
        model2.addSeries(bonlivraison);

        return model2;
    }
    
    private BarChartModel initBarModel3() {

        BarChartModel model3 = new BarChartModel();
        model3.setAnimate(true);
        ChartSeries montantPaye = new ChartSeries();
        montantPaye.setLabel("Montant payé");
        montantPaye.set("Dhs", factureFacade.getMontantTotalPayeDh(u));
        montantPaye.set("Dollar", factureFacade.getMontantTotalPayeDollar(u));
        montantPaye.set("Euro", factureFacade.getMontantTotalPayeEuro(u));
 
        ChartSeries montantNonPaye = new ChartSeries();
        montantNonPaye.setLabel("Montant non payé");
        montantNonPaye.set("Dhs", factureFacade.getMontantTotalNonPayeDh(u));
        montantNonPaye.set("Dollar", factureFacade.getMontantTotalNonPayeDollar(u));
        montantNonPaye.set("Euro", factureFacade.getMontantTotalNonPayeEuro(u));
  
        
        ChartSeries montantEnRetard = new ChartSeries();
        montantEnRetard.setLabel("Montant en retard");
        montantEnRetard.set("Dhs", factureFacade.getMontantTotalEnRetardDh(u));
        montantEnRetard.set("Dollar", factureFacade.getMontantTotalEnRetardDollar(u));
        montantEnRetard.set("Euro", factureFacade.getMontantTotalEnRetardEuro(u));
     
 
        model3.addSeries(montantPaye);
        model3.addSeries(montantNonPaye);
        model3.addSeries(montantEnRetard);
         
        return model3;
    }

    private void createBarModel() {
        barModel = initBarModel1();
        barModel2 = initBarModel2();
        barModel.setLegendPosition("ne");
        Axis yAxis = barModel.getAxis(AxisType.Y);
        Axis xAxis = barModel.getAxis(AxisType.X);
    }

    private void createBarModel2() {
        barModel2 = initBarModel2();
        barModel2.setLegendPosition("ne");
        Axis yAxise = barModel2.getAxis(AxisType.Y);
        yAxise.setLabel("Nombre");
        Axis xAxise = barModel.getAxis(AxisType.X);
        //xAxise.setLabel("Factures");

    }
    private void createBarModel3() {
        barModel3 = initBarModel3();
        barModel3.setLegendPosition("ne");
        Axis yAxise = barModel3.getAxis(AxisType.Y);
        yAxise.setLabel("Nombre");
        Axis xAxise = barModel3.getAxis(AxisType.X);
        //xAxise.setLabel("Factures");

    }

    private void createPieModels() {
        createPieModel1();
    }

    private void createPieModel1() {
        pieModel1 = new PieChartModel();

        pieModel1.set("Montant payé", factureFacade.getMontantTotalPayeDh(SessionUtil.getConnectedUser()));
        pieModel1.set("Montant non payé", factureFacade.getMontantTotalNonPayeDh(SessionUtil.getConnectedUser()));
        pieModel1.set("Montant en retard", factureFacade.getMontantTotalEnRetardDh(SessionUtil.getConnectedUser()));

        pieModel1.setLegendPosition("w");
    }

    public void itemSelect(ItemSelectEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item selected",
                "Item Index: " + event.getItemIndex() + ", Series Index:" + event.getSeriesIndex());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
