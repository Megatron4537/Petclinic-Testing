package org.springframework.samples.petclinic.features;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.web.servlet.ModelAndView;
import java.io.IOException;
import java.util.Properties;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FeatureToggle {

    private VisitRepository visits;

    // features disabled by default
    private boolean featureA = false;
    private boolean featureB = false;

    private static final Logger logger = LogManager.getLogger("FEATURE TOGGLE");

    public FeatureToggle(VisitRepository visits){

        this.visits = visits;

        Properties prop = new Properties();
        try {
            //load a properties file from class path, inside static method
            prop.load(FeatureToggle.class.getClassLoader().getResourceAsStream("application.properties"));

          if(prop.getProperty("featureB").equals("true")) this.featureB = true;

          if(prop.getProperty("featureA").equals("true")) this.featureA = true;

        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Overloaded constructor
     */
    public FeatureToggle(ModelAndView mav){

        Properties prop = new Properties();
        try {
            //load a properties file from class path, inside static method
            prop.load(FeatureToggle.class.getClassLoader().getResourceAsStream("application.properties"));

            if(prop.getProperty("featureA").equals("true")) {
                this.featureA = true;
                mav.addObject("featureA",true);
            }
            if(prop.getProperty("featureB").equals("true")) {
                this.featureB = true;
                mav.addObject("featureB",true);
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean isFeatureAEnabled(){
        return this.featureA;
    }

    public boolean isFeatureBEnabled(){
        return this.featureB;
    }

    public String visitFeature(Map<String,Object> model, int visitId){

        if(this.isFeatureAEnabled()){
            logger.info("Feature A",model,visitId);

            // this.visits.cancel(visitId)
            return "redirect:/owners";
        }
        else if(this.isFeatureBEnabled()){
            logger.info("Feature B",model,visitId);

            Visit visit = this.visits.findById(visitId);
            model.put("visit", visit);
            return "pets/createOrUpdateVisitForm";
        }
        else return "error";
    }
}
