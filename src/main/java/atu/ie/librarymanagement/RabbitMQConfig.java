package atu.ie.librarymanagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConfig.class);

    // Queue names
    public static final String BORROW_BOOK_QUEUE = "borrow.book.queue";
    public static final String RETURN_BOOK_QUEUE = "return.book.queue";
    public static final String RECOMMENDATION_QUEUE = "recommendation.queue";

    @Bean
    public Queue borrowBookQueue() {
        logger.info("Declaring queue: {}", BORROW_BOOK_QUEUE);
        return new Queue(BORROW_BOOK_QUEUE, true);
    }

    @Bean
    public Queue returnBookQueue() {
        logger.info("Declaring queue: {}", RETURN_BOOK_QUEUE);
        return new Queue(RETURN_BOOK_QUEUE, true);
    }

    @Bean
    public Queue recommendationQueue() {
        logger.info("Declaring queue: {}", RECOMMENDATION_QUEUE);
        return new Queue(RECOMMENDATION_QUEUE, true);
    }
}
