package com.solidwall.tartib.services;


  import com.solidwall.tartib.dto.exportation.AdminEvaluationScoresToExportDto;
import com.solidwall.tartib.dto.exportation.ProjectToExportDto;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.stereotype.Service;
  import org.thymeleaf.TemplateEngine;
  import org.thymeleaf.context.Context;
  import org.xhtmlrenderer.pdf.ITextRenderer;

  import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

  @Service
  public class PdfGenerationService {

      @Autowired
      private TemplateEngine templateEngine;

      public byte[] generateProjectPdf(ProjectToExportDto projectData) throws Exception {
          // Create Thymeleaf context with data
          Context context = new Context();
          context.setVariable("project", projectData);

          // Process HTML template
          String htmlContent = templateEngine.process("project-export", context);

          // Convert HTML to PDF using Flying Saucer + iText
          ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
          ITextRenderer renderer = new ITextRenderer();
          renderer.setDocumentFromString(htmlContent);
          renderer.layout();
          renderer.createPDF(outputStream);

          return outputStream.toByteArray();
      }
      
      public byte[] generateAdminEvaluationPdf(AdminEvaluationScoresToExportDto evaluationData) throws Exception {
          // Create Thymeleaf context with evaluation data
          Context context = new Context();
          context.setVariable("evaluation", evaluationData);
          
          // Add generation date
          String formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
          evaluationData.setGenerationDate(formattedDate);

          // Process HTML template
          String htmlContent = templateEngine.process("project-evaluation-export", context);

          // Convert HTML to PDF using Flying Saucer + iText
          ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
          ITextRenderer renderer = new ITextRenderer();
          renderer.setDocumentFromString(htmlContent);
          renderer.layout();
          renderer.createPDF(outputStream);

          return outputStream.toByteArray();
      }
  }