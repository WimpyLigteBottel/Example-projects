package nel.marco.controller;

import nel.marco.manager.SessionManager;
import nel.marco.manager.menu.MenuManager;
import nel.marco.model.Request;
import nel.marco.model.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class UssdRestControllerTest {


    UssdRestController ussdRestController;

    @Mock
    SessionManager sessionManager;

    @Mock
    MenuManager menuManager;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ussdRestController = new UssdRestController(sessionManager, menuManager);
    }

    @Test
    public void ussdRequest_initialEntry_expectMethodsToBeCalled() {

        ussdRestController.ussdRequest(new Request("asd", "asd", ""));


        verify(sessionManager, times(1)).createSession(anyString(), anyString());
        verify(menuManager, times(1)).handleStep(anyString());
    }

    @Test
    public void ussdRequest_secondSession_expectMethodsToBeCalled() {

        List<Session> sessions = new ArrayList<>();
        sessions.add(new Session("asd", "asd"));
        when(sessionManager.getSessionInfo(anyString())).thenReturn(sessions);


        ussdRestController.ussdRequest(new Request("asd", "asd", "1"));

        verify(sessionManager, times(1)).addSession(anyString(), anyString(), anyString());
        verify(menuManager, times(1)).handleStep(anyString());
    }


    @Test
    public void requestSessionId() {
        assertThat(ussdRestController.requestSessionId()).isNotEmpty();
    }
}