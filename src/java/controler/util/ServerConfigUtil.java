/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controler.util;

import bean.Societe;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Chaimaa-abd
 */
public class ServerConfigUtil {

    private static String vmParam = "irisi.projet.upload.path";//chemin dont laquelle on va creer le dosqsier globale qui aura pour bute de contenir la totalitees des dossier d un abonnee
 //   private static List<Item> taxePaths = new ArrayList();

//    static {
//        filesPath(taxePaths, "uploads");
//    }
//
//    private static String getContextParameter(String paramInWeb) {
//        FacesContext ctx = FacesContext.getCurrentInstance();
//        String myConstantValue = ctx.getExternalContext().getInitParameter(paramInWeb);
//        return myConstantValue;
//    }
//
//    private static void filesPath(List<Item> items, String nameVariable) {
//        String filesName = nameVariable;
//        String paths[] = filesName.split(",");
//        for (int i = 0; i < paths.length; i++) {
//            String path = paths[i];
//            String[] oneFileConfig = path.split("=");
//            items.add(new Item(oneFileConfig[0], oneFileConfig[1]));
//        }
//    }
//
//    private static String findFileByPath(List<Item> items, String path) {
//        for (int i = 0; i < items.size(); i++) {
//            Item sessionItem = items.get(i);
//            if (sessionItem.getKey().equalsIgnoreCase(path)) {
//                return sessionItem.getObject().toString();
//            }
//        }
//        return null;
//    }

    private static int createFile(File file) {
        if (!file.exists()) {
            if (file.mkdir()) {
                return 1; //file.getName() + " Directory is created!";
            }
            return -1;//"Failed to create " + file.getName() + " directory!";
        }
        return -2; //file.getName() + " Directory already existe!";
    }

    private static String getSocieteFilePath(Societe societe, boolean write) {
        String rootPath = "";
        if (write) {

            rootPath = System.getProperty(vmParam);
            System.out.println("ha path:::." + rootPath);

        } else {
            rootPath = "/societe";
        }
        return  rootPath;

    }

//    private static void createCommuneFiles(Societe societe) {
//        for (Item taxePath : taxePaths) {
//            createFile(new File(getSocieteFilePath(societe, true) + "\\" + taxePath.getObject().toString()));
//        }
//
//    }

    public static String getChangementFilePath(Societe societe, boolean write) {
        return getSocieteFilePath(societe, write) + "/Changement";
    }

    public static String getChaumagePath(Societe commune, boolean write) {
        return getSocieteFilePath(commune, write) + "/Chaumage";
    }

    public static String getEntrePath(Societe societe, boolean write) {
        return getSocieteFilePath(societe, write);
    }

    public static String getSortiePath(Societe commune, boolean write) {
        return getSocieteFilePath(commune, write) + "/Sortie";
    }
    public static String getInfoPath(Societe commune, boolean write) {
        return getSocieteFilePath(commune, write) + "/info";
    }

//    public static void createCommuneFile(Societe commune) {
//        createFile(new File(getSocieteFilePath(commune, true)));
//        createCommuneFiles(commune);
//    }

    public static void upload(UploadedFile uploadedFile, String destinationPath, String nameOfUploadeFile) {
        try {
            String fileSavePath = destinationPath + "/" + nameOfUploadeFile;
            System.out.println("ha file save path :::" + fileSavePath);
            InputStream fileContent = null;
            fileContent = uploadedFile.getInputstream();
            Files.copy(fileContent, new File(fileSavePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            JsfUtil.addErrorMessage(e, "Erreur Upload image");
            System.out.println("No update !!!!");
            e.printStackTrace();
        }
    }

}
