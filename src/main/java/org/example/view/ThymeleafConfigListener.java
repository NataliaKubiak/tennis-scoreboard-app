package org.example.view;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.log4j.Log4j2;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebApplication;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

@Log4j2
@WebListener
public class ThymeleafConfigListener implements ServletContextListener {

    public static final String TEMPLATE_ENGINE_ATTR = "TemplateEngineInstance";

    private ITemplateEngine templateEngine;
    private JakartaServletWebApplication application;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("Initializing Thymeleaf configuration...");

        this.application = JakartaServletWebApplication.buildApplication(sce.getServletContext());
        this.templateEngine = templateEngine(this.application);

        sce.getServletContext().setAttribute(TEMPLATE_ENGINE_ATTR, templateEngine);
        log.info("TemplateEngine instance set in ServletContext with attribute name: {}", TEMPLATE_ENGINE_ATTR);
    }

    private WebApplicationTemplateResolver getTemplateResolver(IWebApplication application) {
        log.info("Creating TemplateResolver for application: {}", application);
            WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(application);

            templateResolver.setTemplateMode(TemplateMode.HTML);
            templateResolver.setPrefix("/WEB-INF/templates/");
            templateResolver.setSuffix(".html");
            templateResolver.setCacheTTLMs(Long.valueOf(3600000L));
            templateResolver.setCacheable(false);

        log.info("TemplateResolver configured with prefix '/WEB-INF/templates/' and suffix '.html'");
        return templateResolver;
    }

    private ITemplateEngine templateEngine(IWebApplication application) {
        log.info("Creating TemplateEngine instance...");

        TemplateEngine templateEngine = new TemplateEngine();

        WebApplicationTemplateResolver templateResolver = getTemplateResolver(application);
        templateEngine.setTemplateResolver(templateResolver);

        log.info("TemplateEngine instance created with resolver: {}", templateResolver);
        return templateEngine;
    }
}
