import entities.Draw;
import entities.Prize;
import entities.Ticket;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import services.DrawService;
import services.PrizeService;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.when;

public class DrawServiceTest {

    @Mock
    private Draw draw;

    @Mock
    private PrizeService prizeService;

    @InjectMocks
    private DrawService drawService;

    @BeforeEach
    public void init()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void checkNumbersMatchingSameAmountofNumbers()
    {
        when(draw.getWinningNumbers()).thenReturn("1,2,3,4,5,6");
        when(prizeService.findPrizeByNumbersCount(3)).thenReturn(new Prize());
        Ticket ticket = new Ticket();
        ticket.setNumbers("1,2,3,4,5,6");
        ticket = drawService.findUserEntryDrawCommonNumbers(ticket);
        assertEquals(ticket.getWinner().getWinningNumbers(),draw.getWinningNumbers());
    }

    @Test
    public void checkNumbersMatchingMoreNumbersDrawn()
    {
        when(draw.getWinningNumbers()).thenReturn("1,2,3,4,5,6,7,8,9");
        when(prizeService.findPrizeByNumbersCount(3)).thenReturn(new Prize());
        Ticket ticket = new Ticket();
        ticket.setNumbers("1,2,3,4,5,6");
        ticket = drawService.findUserEntryDrawCommonNumbers(ticket);
        assertEquals(ticket.getWinner().getWinningNumbers(),ticket.getNumbers());
    }

    @Test
    public void checkNumbersMatchingNoMatch()
    {
        when(draw.getWinningNumbers()).thenReturn("1,2,3,4,5,6,7,8,9");
        when(prizeService.findPrizeByNumbersCount(3)).thenReturn(new Prize());
        Ticket ticket = new Ticket();
        ticket.setNumbers("10,12,13,14,15,16");
        ticket = drawService.findUserEntryDrawCommonNumbers(ticket);
        assertEquals(ticket.getWinner(),null);
    }

    @Test
    public void checkNumbersMatchingLessMatchesThanLimit()
    {
        when(draw.getWinningNumbers()).thenReturn("1,2,3,4,5,6,7,8,9");
        when(prizeService.findPrizeByNumbersCount(3)).thenReturn(new Prize());
        Ticket ticket = new Ticket();
        ticket.setNumbers("3,12,13,14,15,16");
        ticket = drawService.findUserEntryDrawCommonNumbers(ticket);
        assertEquals(ticket.getWinner(),null);
    }

}
