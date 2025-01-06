package atu.ie.librarymanagement;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private RabbitMQProducer rabbitMQProducer;

    @Test
    void testGetUserById_found() throws Exception {
        User dummy = new User();
        dummy.setId(2L);
        dummy.setName("Alice");
        dummy.setEmail("alice@example.com");
        dummy.setPassword("password123");

        when(userService.getUserById(2L)).thenReturn(dummy);

        mockMvc.perform(get("/api/users/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void testGetUserById_notFound() throws Exception {
        when(userService.getUserById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testBorrowBook_success() throws Exception {
        when(userService.borrowBook(2L, 2L)).thenReturn(true);

        mockMvc.perform(post("/api/users/2/borrow")
                        .param("bookId", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Book borrowed successfully. Message sent to RabbitMQ."));

        verify(userService).borrowBook(2L, 2L);
        verify(rabbitMQProducer).sendBorrowBookMessage(2L, 2L);
    }

    @Test
    void testBorrowBook_failure() throws Exception {
        when(userService.borrowBook(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(post("/api/users/2/borrow")
                        .param("bookId", "99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Book is not available or user not found."));
    }

    // ... other tests
}
