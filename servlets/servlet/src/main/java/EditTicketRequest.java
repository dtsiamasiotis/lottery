import javax.validation.constraints.NotNull;

public class EditTicketRequest {
    @NotNull
    private String ticketId;
    @NotNull
    private String numbers;

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }
}
