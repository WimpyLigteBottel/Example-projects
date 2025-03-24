package nel.marco.controller;


import com.google.gson.Gson;
import nel.marco.manager.SessionManager;
import nel.marco.manager.menu.MenuManager;
import nel.marco.model.Request;
import nel.marco.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
public class UssdRestController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private SessionManager sessionManager;
    private MenuManager menuManager;

    public UssdRestController(SessionManager sessionManager, MenuManager menuManager) {
        this.sessionManager = sessionManager;
        this.menuManager = menuManager;
    }

    @PostMapping("/ussd")
    public Response ussdRequest(@RequestBody @Valid Request request) {

        boolean hasNoSession = sessionManager.getSessionInfo(request.getSessionId()).isEmpty();

        if (hasNoSession) {
            sessionManager.createSession(request.getSessionId(), request.getMsisdn());
        } else {
            sessionManager.addSession(request.getSessionId(), request.getMsisdn(), request.getUserEntry());
        }

        try {
            return menuManager.handleStep(request.getSessionId());
        } catch (Exception e) {
            String sessionJson = new Gson().toJson(sessionManager.getSessionInfo(request.getSessionId()));
            logger.error("ussdRequest failed [sessionId={};sessionJson={}]", request.getSessionId(), sessionJson);

            sessionManager.clearSession(request.getSessionId());

            //This could be handle by clearing the session and displaying default error screen, i decided to keep it simple
            throw new RuntimeException("Could not handle request");
        }
    }

    @GetMapping("/requestSessionId")
    public String requestSessionId() {
        return UUID.randomUUID().toString();
    }


}
