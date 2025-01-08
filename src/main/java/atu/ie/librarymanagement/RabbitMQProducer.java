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
        String message = "Borrowed: User " + userId + ", Book " + bookId;
        rabbitTemplate.convertAndSend(RabbitMQConfig.BORROW_BOOK_QUEUE, message);
    }

    public void sendReturnBookMessage(Long userId, Long bookId) {
        String message = "Returned: User " + userId + ", Book " + bookId;
        rabbitTemplate.convertAndSend(RabbitMQConfig.RETURN_BOOK_QUEUE, message);
    }
}
