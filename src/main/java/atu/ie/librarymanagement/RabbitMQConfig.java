package atu.ie.librarymanagement;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String BORROW_BOOK_QUEUE = "borrow.book.queue";
    public static final String RETURN_BOOK_QUEUE = "return.book.queue";

    @Bean
    public Queue borrowBookQueue() {
        // Define the borrow book queue (durable queue)
        return new Queue(BORROW_BOOK_QUEUE, true);
    }

    @Bean
    public Queue returnBookQueue() {
        // Define the return book queue (durable queue)
        return new Queue(RETURN_BOOK_QUEUE, true);
    }
}
