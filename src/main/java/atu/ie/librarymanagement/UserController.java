package atu.ie.librarymanagement;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final RabbitMQProducer rabbitMQProducer;

    // Combined constructor for dependency injection
    public UserController(UserService userService, RabbitMQProducer rabbitMQProducer) {
        this.userService = userService;
        this.rabbitMQProducer = rabbitMQProducer;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        User createdUser = userService.registerUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User updatedUser) {
        User user = userService.updateUser(id, updatedUser);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.ok("User deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{userId}/borrow")
    public ResponseEntity<String> borrowBook(@PathVariable Long userId, @RequestParam Long bookId) {
        // Send a RabbitMQ message for borrowing a book
        rabbitMQProducer.sendBorrowBookMessage(userId, bookId);
        boolean success = userService.borrowBook(userId, bookId);
        return success ? ResponseEntity.ok("Book borrowed successfully. Message sent to RabbitMQ.") : ResponseEntity.badRequest().body("Book is not available or user not found.");
    }

    @PostMapping("/{userId}/return")
    public ResponseEntity<String> returnBook(@PathVariable Long userId, @RequestParam Long bookId) {
        // Send a RabbitMQ message for returning a book
        rabbitMQProducer.sendReturnBookMessage(userId, bookId);
        boolean success = userService.returnBook(userId, bookId);
        return success ? ResponseEntity.ok("Book returned successfully. Message sent to RabbitMQ.") : ResponseEntity.badRequest().body("Book return failed.");
    }

    @GetMapping("/{userId}/recommendations")
    public ResponseEntity<List<Book>> getRecommendations(@PathVariable Long userId) {
        // Logic to fetch recommendations (to be implemented in service or injected service)
        // Dummy response for now
        return ResponseEntity.ok(List.of()); // Return an empty list or actual recommendation logic
    }
}
