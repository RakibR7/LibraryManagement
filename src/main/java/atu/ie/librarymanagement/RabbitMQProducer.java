package atu.ie.librarymanagement;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendBorrowBookMessage(Long userId, Long bookId) {
        String message = userId + "," + bookId;
        // This sends the message to "borrow.book.queue"
        rabbitTemplate.convertAndSend("borrow.book.queue", message);
        // Optionally log or do something else
    }

    public void sendReturnBookMessage(Long userId, Long bookId) {
        String message = userId + "," + bookId;
        rabbitTemplate.convertAndSend("return.book.queue", message);
    }

    public void sendRecommendationMessage(Long userId, Long bookId, String action) {
        // e.g., "userId,bookId,borrow" or "userId,bookId,return"
        String message = userId + "," + bookId + "," + action;
        rabbitTemplate.convertAndSend("recommendation.queue", message);
    }
}
