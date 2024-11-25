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

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get")
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
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
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
        boolean success = userService.borrowBook(userId, bookId);
        return success ? ResponseEntity.ok("Book borrowed successfully.") : ResponseEntity.badRequest().body("Book is not available or user not found.");
    }

    @PostMapping("/{userId}/return")
    public ResponseEntity<String> returnBook(@PathVariable Long userId, @RequestParam Long bookId) {
        boolean success = userService.returnBook(userId, bookId);
        return success ? ResponseEntity.ok("Book returned successfully.") : ResponseEntity.badRequest().body("Book return failed.");
    }


    @GetMapping("/{userId}/recommendations")
    public ResponseEntity<ResponseEntity<List<Book>>> getRecommendations(@PathVariable Long userId) {
        UserController recommendationService = null;
        ResponseEntity<List<Book>> recommendedBooks = recommendationService.getRecommendations(userId).getBody();
        return ResponseEntity.ok(recommendedBooks);
    }
}
