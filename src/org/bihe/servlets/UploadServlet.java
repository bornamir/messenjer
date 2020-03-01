package org.bihe.servlets;

import org.bihe.daoimpl.MessageDoaImpl;
import org.bihe.interfaces.MessageDAO;
import org.bihe.models.Message;
import org.bihe.sevices.Constants;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "UploadServlet", urlPatterns = "/upload")
@MultipartConfig(maxFileSize = Constants.MAX_FILE_SIZE, location = Constants.SAVE_DIR)
public class UploadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        System.out.println(request.getParameterMap());
        File fileSaveDir = new File(Constants.SAVE_DIR + File.separator + Constants.SAVE_DIR_FOLDER);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
        }
        for (Part part : request.getParts()) {
            String fileName = part.getSubmittedFileName();
            if (fileName != null) {
                part.write(Constants.SAVE_DIR_FOLDER + File.separator + fileName);
            }
        }
        request.getRequestDispatcher("updateMessage").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        MessageDAO messageDAO = new MessageDoaImpl();

        String listeningUser = (String) request.getSession(false).getAttribute("username");
        String sendingUser = request.getParameter("receiver");
        String fileName = request.getParameter("file");
        List<Message> messages = messageDAO.getAllFilesBetweenTwoUsers(listeningUser, sendingUser);
        boolean valid = false;
        for (Message message : messages) {
            if (message.getMessage().equals(fileName)) {
                valid = true;
                break;
            }
        }
        if (!valid) { // the file does not belong to this chat or does not exist
            response.sendError(403);
        }

        String filePath = Constants.SAVE_DIR + File.separator +
                Constants.SAVE_DIR_FOLDER + File.separator + fileName;
        File downloadFile = new File(filePath);

        String mimeType = getServletContext().getMimeType(filePath);
        if (mimeType == null) {
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }
        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
        response.setHeader(headerKey, headerValue);

        try (ServletOutputStream outStream = response.getOutputStream();
             FileInputStream inStream = new FileInputStream(downloadFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            while ((bytesRead = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            response.sendError(500);
        }


    }
}
