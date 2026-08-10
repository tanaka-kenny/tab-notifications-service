package za.co.pacifish.notification_service.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import za.co.pacifish.notification_service.enumeration.Channel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class NotificationFactoryTest {

    @Test
    void getStrategy_returnsStrategyForKnownChannel() {
        NotificationStrategy emailStrategy = Mockito.mock(NotificationStrategy.class);
        when(emailStrategy.getSupportedChannel()).thenReturn(Channel.EMAIL);

        NotificationFactory factory = new NotificationFactory(List.of(emailStrategy));

        NotificationStrategy result = factory.getStrategy(Channel.EMAIL);

        assertSame(emailStrategy, result);
    }

    @Test
    void getStrategy_throwsForUnknownChannel() {
        NotificationStrategy emailStrategy = Mockito.mock(NotificationStrategy.class);
        when(emailStrategy.getSupportedChannel()).thenReturn(Channel.EMAIL);

        NotificationFactory factory = new NotificationFactory(List.of(emailStrategy));

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> factory.getStrategy(Channel.PUSH_NOTIFICATION)
        );

        assertEquals("No strategy found for channel: PUSH_NOTIFICATION", exception.getMessage());
    }
}
