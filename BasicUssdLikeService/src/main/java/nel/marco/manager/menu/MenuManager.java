package nel.marco.manager.menu;

import nel.marco.manager.PaymentManager;
import nel.marco.manager.SessionManager;
import nel.marco.model.Response;
import nel.marco.model.Session;
import nel.marco.type.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
public class MenuManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    private final SessionManager sessionManager;
    private final MenuInputValidator menuInputValidator;
    private final PaymentManager paymentManager;

    public MenuManager(SessionManager sessionManager, MenuInputValidator menuInputValidator, PaymentManager paymentManager) {
        this.sessionManager = sessionManager;
        this.menuInputValidator = menuInputValidator;
        this.paymentManager = paymentManager;
    }


    public Response handleStep(String sessionId) {

        int stepNumber = determineCurrentStep(sessionId);
        Session session = sessionManager.getLatestSession(sessionId);

        String message;
        String additionalOptions;

        switch (stepNumber) {
            case 1:
                //Templating could be used here
                message = "Welcome! Where would you like to send your money today!\n %s";
                additionalOptions = "1. Kenya \n 2.Malawi";
                message = String.format(message, additionalOptions);

                return new Response(session.getSessionId(), message);


            case 2:
                updateCountryOption(session);

                message = "How much money(in Rands) would like to send to %s?";
                message = String.format(message, session.getUserEntry().get()); // optional should always be checked if its present or not. This should be check in validation step

                return new Response(session.getSessionId(), message);

            case 3:

                message = "Please enter the person cellphone number?\n Example: 0123456789";
                message = String.format(message, session.getUserEntry().get()); // optional should always be checked if its present or not. This should be check in validation step

                return new Response(session.getSessionId(), message);


            case 4:

                Optional<String> country = sessionManager.getSessionInfo(session.getSessionId()).get(1).getUserEntry();
                Optional<String> moneyAmount = sessionManager.getSessionInfo(session.getSessionId()).get(2).getUserEntry();

                if (!country.isPresent() || !moneyAmount.isPresent()) {
                    throw new RuntimeException("Unable to find correct values in step 3");
                }

                BigDecimal randAmount = BigDecimal.valueOf(Double.parseDouble(moneyAmount.get()));
                Country countrySelected = Country.parseValue(country.get());


                BigDecimal actualAmount = Country.convertAmount(randAmount, countrySelected);


                message = "The person your sending to will receive: %s %s. \n 1. Ok";
                message = String.format(message, actualAmount.toPlainString(), countrySelected.getCurrency());

                return new Response(session.getSessionId(), message);


            case 5:

                message = "Thank you for using XYZ company!";

                try {
                    paymentManager.payClient(sessionManager.getSessionInfo(session.getSessionId()));
                } catch (Exception e) {
                    message = "Your payment failed, please follow up with our agents (0123456789)";
                } finally {
                    sessionManager.clearSession(session.getSessionId());
                }

                return new Response(session.getSessionId(), message);

            default:
                throw new RuntimeException(String.format("Invalid step number [stepNumber=%d;sessionId=%s]", stepNumber, session.getSessionId()));
        }

    }

    private int determineCurrentStep(String sessionId) {

        List<Session> sessions = sessionManager.getSessionInfo(sessionId);

        if (sessions.isEmpty()) {
            return 1;
        }

        switch (sessions.size()) {
            case 1:
                return 1;
            case 2:
                if (menuInputValidator.isStep1Valid(sessions)) {
                    return 2;
                }
                sessionManager.removeSession(sessionId, 1);
                return 1;
            case 3:
                if (menuInputValidator.isStep2Valid(sessions)) {
                    return 3;
                }

                sessionManager.removeSession(sessionId, 2);
                return 2;
            case 4:
                if (menuInputValidator.isStep3Valid(sessions)) {
                    return 4;
                }

                sessionManager.removeSession(sessionId, 3);
                return 3;
            case 5:
                if (menuInputValidator.isStep4Valid(sessions)) {
                    return 5;
                }
                sessionManager.removeSession(sessionId, 4);
                return 4;
            default:
                logger.error("determineCurrentStep failed enter the default case [sessionId={}]", sessionId);
                throw new RuntimeException("This should not have happened");
        }

    }

    /*
        Updates the country option the user has chosen
        Example:

        1 == Kenya
        2 == Malawi

        Ideally I would implement better way of converting the values selected but tried to keep it simple
     */
    private void updateCountryOption(Session session) {


        if (!session.getUserEntry().isPresent()) {
            return;
        }

        switch (session.getUserEntry().get()) {
            case "1":
                session.setUserEntry(Country.values()[0].toString());
                break;
            case "2":
                session.setUserEntry(Country.values()[1].toString());
                break;
            default:
                //Additional feature can be implemented in case the user types out the country and not use the number provided
                session.setUserEntry(Country.parseValue(session.getUserEntry().get()).toString());
                break;
        }
    }


}
