package nel.marco.model;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class Request {

    @NotNull
    private String sessionId;

    @Length(min = 10, max = 10)
    @NotNull
    private String msisdn;

    @Length(max = 100)
    private String userEntry;


    public Request() {
    }

    public Request(String sessionId, String msisdn, String userEntry) {
        this.sessionId = sessionId;
        this.msisdn = msisdn;
        this.userEntry = userEntry;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getUserEntry() {
        return userEntry;
    }

    public void setUserEntry(String userEntry) {
        this.userEntry = userEntry;
    }
}
