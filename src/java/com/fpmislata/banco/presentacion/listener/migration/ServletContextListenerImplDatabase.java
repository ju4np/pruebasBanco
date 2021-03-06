/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpmislata.banco.presentacion.listener.migration;

import com.fpmislata.banco.dominio.SucursalBancaria;
import com.fpmislata.banco.persistencia.SucursalBancariaDAO;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author DAW-2
 */
public class ServletContextListenerImplDatabase implements ServletContextListener {


    InitialContext initialContext;
    DataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            //Ahora mismo esto no se usa
            //Codigo para que funcione el autowired
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContextEvent.getServletContext());
            AutowireCapableBeanFactory autowireCapableBeanFactory = webApplicationContext.getAutowireCapableBeanFactory();
            autowireCapableBeanFactory.autowireBean(this);
            
            //Ejecutamos los script de sql
            initialContext = new InitialContext();
            this.dataSource = (DataSource) initialContext.lookup("java:comp/env/jdbc/cycybank");
            initialContext.close();
            
            Flyway flyway = new Flyway();
            flyway.setDataSource(dataSource);
            flyway.setLocations("com.fpmislata.banco.persistencia.migration");
            flyway.setEncoding("utf-8");
            flyway.migrate();
            
            
            System.out.println("*/*/*/*/*/*/*/*/*/ServletContextListener started ");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("*/*/*/*/*/*/*/*/*/ServletContextListener destroyed");
    }

}
