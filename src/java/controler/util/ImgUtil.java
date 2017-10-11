/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controler.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Bakero
 */
@WebServlet(name = "ImgUtil", urlPatterns = "/societe/*")
public class ImgUtil extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {

            // Get image file.
            String file = request.getParameter("file");
            System.out.println("Hahuwa le FILE ==>" + file);

            BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));

            // Get image contents.
            byte[] bytes = new byte[in.available()];

            in.read(bytes);
            in.close();

            // Write image contents to response.
            response.getOutputStream().write(bytes);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
}
