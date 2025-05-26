import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class Message {

    private String messageID;
    private String recipient;
    private String messageText;
    private static int totalMessagesSent = 0;
    private static ArrayList<Message> sentMessages = new ArrayList<>();

    public Message(String messageID, String recipient, String messageText) {
        this.messageID = messageID;
        this.recipient = recipient;
        this.messageText = messageText;
    }

    public static void resetTotalMessages() {
    }

    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    public boolean checkRecipientCell() {
        return recipient != null && recipient.length() <= 10 && recipient.startsWith("+");
    }

    public String createMessageHash() {
        String firstTwo = messageID.length() >= 2 ? messageID.substring(0, 2) : messageID;
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;
        return (firstTwo + ":" + totalMessagesSent + "." + (firstWord + lastWord)).toUpperCase();
    }

    public String sentMessage() {
        String[] options = {"Send Message", "Disregard Message", "Store Message"};
        int choice = JOptionPane.showOptionDialog(null,
                "Choose what to do with the message:",
                "Message Options",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

        switch (choice) {
            case 0:
                totalMessagesSent++;
                sentMessages.add(this);
                return "Message sent";
            case 1:
                return "Message disregarded";
            case 2:
                return "Message stored";
            default:
                return "No option selected";
        }
    }

    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages sent yet.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append("ID: ").append(m.messageID)
                    .append(", Recipient: ").append(m.recipient)
                    .append(", Message: ").append(m.messageText).append("\n");
        }
        return sb.toString();
    }

    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    public static void storeMessages() {
        // Placeholder for JSON or file storage logic
        System.out.println("Storing messages:");
        System.out.println(printMessages());
    }

    public String getMessageID() {
        return messageID;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public static String generateMessageID() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }
}
