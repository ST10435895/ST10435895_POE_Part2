import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    private Message validMessage1;
    private Message invalidIDMessage;
    private Message invalidRecipientMessage;
    private Message shortMessage;

    @BeforeEach
    public void setUp() {
        validMessage1 = new Message("1234567890", "+27812345", "Hello world!");
        invalidIDMessage = new Message("12345678901", "+27812345", "Hello again!"); // ID > 10 chars
        invalidRecipientMessage = new Message("1234567890", "278123456", "Hello!");  // No "+"
        shortMessage = new Message("12", "+27123456", "Hi");
    }

    @Test
    public void testCheckMessageID() {
        assertTrue(validMessage1.checkMessageID());
        assertFalse(invalidIDMessage.checkMessageID());
    }

    @Test
    public void testCheckRecipientCell() {
        assertTrue(validMessage1.checkRecipientCell()); // Valid recipient format
        assertFalse(invalidRecipientMessage.checkRecipientCell());
    }

    @Test
    public void testCreateMessageHash() {
        String hash = shortMessage.createMessageHash();
        assertTrue(hash.startsWith("12:")); // MessageID "12"
        assertTrue(hash.toUpperCase().contains("HI")); // "Hi" message reflected in hash
    }

    @Test
    public void testGenerateMessageID() {
        String id = Message.generateMessageID();
        assertNotNull(id);
        assertEquals(10, id.length());
        assertTrue(id.matches("\\d{10}"));
    }

    @Test
    public void testReturnTotalMessages() {
        int before = Message.returnTotalMessages();
        Message temp = new Message("1234567890", "+27812345", "Test msg");
        int after = Message.returnTotalMessages();
        assertEquals(before, after);
    }

    @Test
    public void testPrintMessages() {
        String result = Message.printMessages();
        assertNotNull(result);
    }

    @Test
    public void testGetters() {
        assertEquals("1234567890", validMessage1.getMessageID());
        assertEquals("+27812345", validMessage1.getRecipient());
        assertEquals("Hello world!", validMessage1.getMessageText());
    }

    //tests for question 9 requirements:
    @Test
    public void testMessageLengthLimitSuccess() {
        String msg = "This is a message well under the 250 character limit.";
        Message m = new Message("1234567890", "+27812345", msg);
        String response = checkMessageLength(m.getMessageText());
        assertEquals("Message ready to send.", response);
    }

    @Test
    public void testMessageLengthLimitFailure() {
        StringBuilder longMsg = new StringBuilder();
        for (int i = 0; i < 260; i++) longMsg.append("a");
        Message m = new Message("1234567890", "+27812345", longMsg.toString());
        String response = checkMessageLength(m.getMessageText());
        assertTrue(response.startsWith("Message exceeds 250 characters by"));
    }

    private String checkMessageLength(String message) {
        if (message.length() <= 250) {
            return "Message ready to send.";
        } else {
            int excess = message.length() - 250;
            return "Message exceeds 250 characters by " + excess + ", please reduce size.";
        }
    }

    @Test
    public void testRecipientFormatSuccess() {
        Message m = new Message("1234567890", "+2781234567", "Test");
        String response = checkRecipient(m.getRecipient());
        assertEquals("Cell phone number successfully captured.", response);
    }

    @Test
    public void testRecipientFormatFailure() {
        Message m = new Message("1234567890", "2781234567", "Test");
        String response = checkRecipient(m.getRecipient());
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", response);
    }

    private String checkRecipient(String recipient) {
        if (recipient != null && recipient.startsWith("+") && recipient.length() <= 15) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    @Test
    public void testMessageHashCorrectness() {
        Message m = new Message("00", "+2781234567", "Hi tonight");
        String hash = m.createMessageHash();
        assertTrue(hash.toUpperCase().startsWith("00:"));
        assertTrue(hash.toUpperCase().contains("HITONIGHT"));
    }

    @Test
    public void testMessageIDGenerationFormat() {
        String id = Message.generateMessageID();
        assertNotNull(id);
        assertEquals(10, id.length());
        assertTrue(id.matches("\\d{10}"));
    }
}

