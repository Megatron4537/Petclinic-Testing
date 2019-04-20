package org.springframework.samples.petclinic.features;

import org.apache.logging.log4j.message.ObjectArrayMessage;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.web.servlet.ModelAndView;
import java.io.IOException;
import java.util.Properties;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FeatureToggle {

    private VisitRepository visits;

    // features disabled by default
    private static boolean featureA = false;
    private static boolean featureB = false;

    private static final Logger logger = LogManager.getLogger("FeatureToggle");

    public FeatureToggle(VisitRepository visits){

        this.visits = visits;

        try {
            // if toggle was already set, dont check config file for enabled features
                //load a properties file from class path, inside static method
                Properties prop = new Properties();
                prop.load(FeatureToggle.class.getClassLoader().getResourceAsStream("application.properties"));

//                if (prop.getProperty("featureB").equals("true")) this.setFeatureB(true);
//
//                if (prop.getProperty("featureA").equals("true"))this.setFeatureA(true);


        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Overloaded constructor
     */
    public FeatureToggle(ModelAndView mav){

        try {
            // if toggle was already set, dont check config file for enabled features

//                //load a properties file from class path, inside static method
                Properties prop = new Properties();
                prop.load(FeatureToggle.class.getClassLoader().getResourceAsStream("application.properties"));
//
//                if (prop.getProperty("featureA").equals("true")) {
//                    this.setFeatureA(true);
//                }
//                if (prop.getProperty("featureB").equals("true")) {
//                    this.setFeatureA(false);
//                }

            boolean feature = ThreadLocalRandom.current().nextBoolean();
            this.setFeatureB(!feature);
            this.setFeatureA(feature);

            mav.addObject("featureA", featureA);
            mav.addObject("featureB", featureB);

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

    public static void setFeatureA(boolean on){
        featureA = on;
    }

    public static void setFeatureB(boolean on) {
        featureB = on;
    }

    public String visitFeatureA(Map<String,Object> model, int visitId){

        if(this.isFeatureAEnabled()){
            logger.info(String.format("A, %d",visitId));
            Visit visit = this.visits.findById(visitId);
            this.visits.delete(visit);
            return "";
        } else return "error";
    }
    
    public String visitFeatureB(Map<String,Object> model, int visitId){
    
        if(this.isFeatureBEnabled()){
            logger.info(String.format("B, %d",visitId));

            Visit visit = this.visits.findById(visitId);
            model.put("visit", visit);  
            return "pets/createOrUpdateVisitForm";
            
        } else return "error";
    }
}
