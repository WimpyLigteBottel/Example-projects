package nel.marco.model;

import java.util.Optional;

public class Session {


    /*
    A session identifier assigned by the WASP,
    it will be consistent for a user across multiple interactions.*
    */

    private final String sessionId;

    //This represents a cell phone number and can be considered unique per user.
    private final String msisdn;


    /*
    This represents the user’s text/number entry from their cellphone. It can be left absent or
    blank for the initial interaction.

    Why  use optional ?
    Makes determining the if its empty easier and indicates clearly that it can be empty
    */
    private Optional<String> userEntry = Optional.empty();

    public Session(String sessionId, String msisdn) {
        this.sessionId = sessionId;
        this.msisdn = msisdn;
    }

    public Session(String sessionId, String msisdn, String userEntry) {
        this.sessionId = sessionId;
        this.msisdn = msisdn;
        this.userEntry = Optional.ofNullable(userEntry);
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public Optional<String> getUserEntry() {
        return userEntry;
    }

    public void setUserEntry(String userEntry) {
        //Optional caters for null input
        this.userEntry = Optional.ofNullable(userEntry);
    }
}
