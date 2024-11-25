package atu.ie.librarymanagement;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // Corrected: Non-static method to send a recommendation message
    public void sendRecommendationMessage(Long userId, Long bookId, String action) {
        String message = userId + "," + bookId + "," + action;
        rabbitTemplate.convertAndSend("recommendation.queue", message);
    }


    public boolean borrowBook(Long userId, Long bookId) {
        String message = userId + "," + bookId; // Simple comma-separated message
        rabbitTemplate.convertAndSend("borrow.book.queue", message);
        sendRecommendationMessage(userId, bookId, "borrow");
        return true;
    }

    public boolean returnBook(Long userId, Long bookId) {
        String message = userId + "," + bookId;
        rabbitTemplate.convertAndSend("return.book.queue", message);
        sendRecommendationMessage(userId, bookId, "return");
        return true;
    }
}

