package atu.ie.librarymanagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendBorrowBookMessage(Long userId, Long bookId) {
        String message = "User " + userId + " borrowed Book " + bookId;
        logger.info("Sending to borrow.book.queue: '{}'", message);
        rabbitTemplate.convertAndSend(RabbitMQConfig.BORROW_BOOK_QUEUE, message);
        logger.info("Message successfully sent to borrow.book.queue");
    }

    public void sendReturnBookMessage(Long userId, Long bookId) {
        String message = "User " + userId + " returned Book " + bookId;
        logger.info("Sending to return.book.queue: '{}'", message);
        rabbitTemplate.convertAndSend(RabbitMQConfig.RETURN_BOOK_QUEUE, message);
        logger.info("Message successfully sent to return.book.queue");
    }

    public void sendRecommendationMessage(Long userId, Long bookId, String action) {
        // e.g. "User 2, Book 5, Action: borrow"
        String message = "User " + userId + ", Book " + bookId + ", Action: " + action;
        logger.info("Sending to recommendation.queue: '{}'", message);
        rabbitTemplate.convertAndSend(RabbitMQConfig.RECOMMENDATION_QUEUE, message);
        logger.info("Message successfully sent to recommendation.queue");
    }
}
