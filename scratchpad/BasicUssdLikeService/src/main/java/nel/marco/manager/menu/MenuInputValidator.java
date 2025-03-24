package nel.marco.manager.menu;

import com.google.gson.Gson;
import nel.marco.model.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;


//The goal of this class is to make sure each input is valid for future steps and checking that everything is valid input

@Component
public class MenuInputValidator {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    public boolean isStep1Valid(List<Session> sessions) {

        if (sessions.size() <= 0) {
            return false;
        }

        Session step1 = sessions.get(1);

        if (!step1.getUserEntry().isPresent()) {
            logger.error("isStep1Valid failed [sessions={}]", new Gson().toJson(sessions));
            return false;
        }

        switch (step1.getUserEntry().get()) {
            case "1":
            case "2":
                return true;
            default:
                // If the person types the name of the country i could add the feature to maybe do check to find it.
                return false;

        }
    }


    public boolean isStep2Valid(List<Session> sessions) {

        if (sessions.size() <= 1) {
            return false;
        }

        Session step2 = sessions.get(2);

        if (!step2.getUserEntry().isPresent()) {
            logger.error("isStep2Valid failed [sessions={}]", new Gson().toJson(sessions));
            return false;
        }

        try {
            Double.parseDouble(step2.getUserEntry().get());
        } catch (NumberFormatException e) {
            return false;
        }

        return true;

    }


    public boolean isStep3Valid(List<Session> sessions) {

        if (sessions.size() <= 2) {
            return false;
        }

        Session step3 = sessions.get(3);

        if (!step3.getUserEntry().isPresent()) {
            logger.error("isStep3Valid [sessions={}]", new Gson().toJson(sessions));
            return false;
        }

        //Below is crude way of validating the number, could always improve this if required
        //Also one should not be able to pay yourself
        return step3.getUserEntry().get().matches("[0-9]{10}") && !step3.getUserEntry().get().equalsIgnoreCase(step3.getMsisdn());
    }


    public boolean isStep4Valid(List<Session> sessions) {

        if (sessions.size() <= 3) {
            return false;
        }

        Session step4 = sessions.get(4);

        if (!step4.getUserEntry().isPresent()) {
            logger.error("isStep4Valid [sessions={}]", new Gson().toJson(sessions));
            return false;
        }

        return step4.getUserEntry().get().equalsIgnoreCase("1") || step4.getUserEntry().get().equalsIgnoreCase("ok");
    }


}
