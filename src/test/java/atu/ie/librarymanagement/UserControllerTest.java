package atu.ie.librarymanagement;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
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
    void testBorrowBook_success() throws Exception {
        // Simulate the userService returning true when user 2 borrows book 2
        when(userService.borrowBook(2L, 2L)).thenReturn(true);

        // Perform a POST request: /api/users/2/borrow?bookId=2
        mockMvc.perform(post("/api/users/2/borrow")
                        .param("bookId", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                // Expect HTTP 200 (OK)
                .andExpect(status().isOk())
                // Expect the response body
                .andExpect(content().string("Book borrowed successfully. Message sent to RabbitMQ."));

        // Optionally, verify that the mocked service was called
        Mockito.verify(userService).borrowBook(2L, 2L);
    }

    @Test
    void testBorrowBook_failure() throws Exception {
        // Simulate the userService returning false (invalid user/book, etc.)
        when(userService.borrowBook(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(post("/api/users/2/borrow")
                        .param("bookId", "999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Book is not available or user not found."));
    }

    @Test
    void testReturnBook_success() throws Exception {
        // Simulate a successful return
        when(userService.returnBook(2L, 2L)).thenReturn(true);

        mockMvc.perform(post("/api/users/2/return")
                        .param("bookId", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Book returned successfully. Message sent to RabbitMQ."));

        Mockito.verify(userService).returnBook(2L, 2L);
    }

    @Test
    void testReturnBook_failure() throws Exception {
        // Return fails
        when(userService.returnBook(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(post("/api/users/2/return")
                        .param("bookId", "999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Book return failed."));
    }
}
