/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controler.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;

/**
 *
 * @author Chaimaa-abd
 */
public class ServerConfig {
     private static String vmParam = "commune.files.path";//chemin dont laquelle on va creer le dosqsier globale qui aura pour bute de contenir la totalitees des dossier d un abonnee
     private static List<Item> taxePaths = new ArrayList();
      static {
        filesPath(taxePaths,"libelles_dossiers");
    }
       private static String getContextParameter(String paramInWeb) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        String myConstantValue = ctx.getExternalContext().getInitParameter(paramInWeb);
        return myConstantValue;
    }

   private static void filesPath(List<Item> items,String nameVariable) {
        String filesName = getContextParameter(nameVariable);
        String paths[] = filesName.split(",");
        for (int i = 0; i < paths.length; i++) {
            String path = paths[i];
            String[] oneFileConfig = path.split("=");
            items.add(new Item(oneFileConfig[0], oneFileConfig[1]));
        }
    }
   private static String findFileByPath(List<Item> items,String path) {
        for (int i = 0; i < items.size(); i++) {
            Item sessionItem = items.get(i);
            if (sessionItem.getKey().equalsIgnoreCase(path)) {
                return sessionItem.getObject().toString();
            }
        }
        return null;
    }
    private static int createFile(File file) {
        if (!file.exists()) {
            if (file.mkdir()) {
                return 1; //file.getName() + " Directory is created!";
            }
            return -1;//"Failed to create " + file.getName() + " directory!";
        }
        return -2; //file.getName() + " Directory already existe!";
    }
//     private static String getCommuneFilePath(Commune commune, boolean write) {
//        String rootPath = "";
//        if (write) {
//           
//            rootPath = System.getProperty(vmParam);
//            System.out.println(rootPath);
//            
//        } else {
//            rootPath = "\\communeFiles";
//        }
//        return rootPath + "\\commune-" + commune.getId();
//    }
//     private static void createCommuneFiles(Commune commune){
//          for (Item taxePath : taxePaths) {
//              createFile(new File(getCommuneFilePath(commune, true)+ "\\" + taxePath.getObject().toString()));
//         }
//       
//   }
//private static String getChangementFilePath(Commune commune, boolean write){
//      return getCommuneFilePath( commune,  write) + "\\Changement" ;
//  }
//private static String getChaumagePath(Commune commune, boolean write){
//      return getCommuneFilePath( commune,  write) + "\\Chaumage" ;
//  }
//private static String getEntrePath(Commune commune, boolean write){
//      return getCommuneFilePath( commune,  write) + "\\Entree" ;
//  }
//private static String getSortiePath(Commune commune, boolean write){
//      return getCommuneFilePath( commune,  write) + "\\Sortie" ;
//  }

//   public static void createCommuneFile(Commune commune){
//    createFile(new File(getCommuneFilePath(commune,true)));
//    createCommuneFiles(commune);
//}
}
