package ticket.booking.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private String name;  // Line 5
    private String password;  // Line 6
    private String hashPassword;  // Line 7
    private List<Ticket> ticketsBooked;  // Line 8
    private String userId;  // Line 9

    // Constructor with parameters
    public User(String name, String password, String hashPassword, List<Ticket> ticketBooked, String userId) {  // Line 11
        this.name = name;
        this.password = password;
        this.hashPassword = hashPassword;
        this.ticketsBooked = ticketBooked;
        this.userId = userId;
    }

    // Default constructor for deserialization
    public User() {}  // Line 16

    // Getter methods
    public String getName() {  // Line 18
        return name;
    }

    public String getPassword() {  // Line 20
        return password;
    }

    public String getHashPassword() {  // Line 22
        return hashPassword;
    }

    public List<Ticket> getTicketBooked() {  // Line 24
        return ticketsBooked;
    }

    public void printTickets() {  // Line 26
        for (int i = 0; i < ticketsBooked.size(); i++) {
            System.out.println(ticketsBooked.get(i).getTicketInfo());
        }
    }

    public String getUserId() {  // Line 30
        return userId;
    }

    // Setter methods
    public void setName(String name) {  // Line 33
        this.name = name;
    }

    public void setPassword(String password) {  // Line 35
        this.password = password;
    }

    public void setHashPassword(String hashPassword) {  // Line 37
        this.hashPassword = hashPassword;
    }

    // Corrected setter method: parameter name should be `ticketsBooked` (lowercase `t`)
    public void setTicketBooked(List<Ticket> ticketsBooked) {  // Line 45 (Fixed)
        this.ticketsBooked = ticketsBooked;
    }

    public void setUserId(String userId) {  // Line 47
        this.userId = userId;
    }
}
