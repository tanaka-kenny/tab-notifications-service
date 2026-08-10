package za.co.pacifish.notification_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.pacifish.notification_service.enumeration.Channel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NotificationFactory {

    private final Map<Channel, NotificationStrategy> strategies = new HashMap<>();

    @Autowired
    public NotificationFactory(List<NotificationStrategy> strategies) {
        strategies.forEach(strategy ->
            this.strategies.put(strategy.getSupportedChannel(), strategy));
    }

    public NotificationStrategy getStrategy(Channel channel) {
        NotificationStrategy strategy = strategies.get(channel);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for channel: " + channel);
        }
        return strategy;
    }
}
