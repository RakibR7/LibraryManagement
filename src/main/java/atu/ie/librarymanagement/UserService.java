package atu.ie.librarymanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User registerUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(Long id, User updatedUser) {
        if (userRepository.existsById(id)) {
            updatedUser.setId(id);
            return userRepository.save(updatedUser);
        }
        return null;
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean borrowBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            // Call Book Service to check availability and mark the book as borrowed
            String bookServiceUrl = "http://localhost:8081/api/books/" + bookId + "/borrow";
            ResponseEntity<Boolean> response = restTemplate.postForEntity(bookServiceUrl, null, Boolean.class);

            if (response.getBody() != null && response.getBody()) {
                user.getBorrowedBooks().add(bookId);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    public boolean returnBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null && user.getBorrowedBooks().contains(bookId)) {
            // Call Book Service to mark the book as returned
            String bookServiceUrl = "http://localhost:8081/api/books/" + bookId + "/return";
            restTemplate.postForEntity(bookServiceUrl, null, Void.class);

            user.getBorrowedBooks().remove(bookId);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
