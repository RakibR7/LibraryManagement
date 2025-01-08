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
        return ResponseEntity.ok(userService.registerUser(user));
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User updatedUser) {
        User user = userService.updateUser(id, updatedUser);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id) ? ResponseEntity.ok("User deleted successfully.")
                : ResponseEntity.notFound().build();
    }
    @PostMapping("/{userId}/borrow")
    public ResponseEntity<String> borrowBook(@PathVariable Long userId, @RequestParam Long bookId) {
        rabbitMQProducer.sendBorrowBookMessage(userId, bookId);
        return userService.borrowBook(userId, bookId)
                ? ResponseEntity.ok("Book borrowed successfully. Message sent to RabbitMQ.")
                : ResponseEntity.badRequest().body("Book is not available or user not found.");
    }
    @PostMapping("/{userId}/return")
    public ResponseEntity<String> returnBook(@PathVariable Long userId, @RequestParam Long bookId) {
        rabbitMQProducer.sendReturnBookMessage(userId, bookId);
        return userService.returnBook(userId, bookId)
                ? ResponseEntity.ok("Book returned successfully. Message sent to RabbitMQ.")
                : ResponseEntity.badRequest().body("Book return failed.");
    }
    @GetMapping("/{userId}/recommendations")
    public ResponseEntity<List<Book>> getRecommendations(@PathVariable Long userId) {
        return ResponseEntity.ok(List.of());
    }
}
