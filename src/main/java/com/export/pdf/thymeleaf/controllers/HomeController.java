package com.export.pdf.thymeleaf.controllers;

import com.export.pdf.thymeleaf.domains.User;
import com.export.pdf.thymeleaf.repo.UserRepo;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
public class HomeController {


    @Autowired
    private UserRepo userRepo;


    @Autowired
    ServletContext servletContext;

    private final TemplateEngine templateEngine;

    public HomeController(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }


    @GetMapping("/users")
    public String getUserList()
    {
         ResponseEntity.status(HttpStatus.OK).body(this.userRepo.findAll());
         return "/userInvoice";
    }


    @RequestMapping(path = "/userpdf")
    public ResponseEntity<?> getUserPdf(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /* Do Business Logic*/

       User userFirstRow = this.userRepo.findAll().get(0);


        List<User> userData = this.userRepo.findAll();

        /* Create HTML using Thymeleaf template Engine */

        WebContext context = new WebContext(request, response, servletContext);
        context.setVariable("userData", userFirstRow);
        context.setVariable("iterateData",userData);
        String userInvoice = templateEngine.process("userInvoice", context);

        /* Setup Source and target I/O streams */

        ByteArrayOutputStream target = new ByteArrayOutputStream();
//        ConverterProperties converterProperties = new ConverterProperties();
//        converterProperties.setBaseUri("http://localhost:8080");
        /* Call convert method */
        HtmlConverter.convertToPdf(userInvoice, target, null);

        /* extract output as bytes */
        byte[] bytes = target.toByteArray();


        /* Send the response as downloadable PDF */

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=user.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);

    }

}
