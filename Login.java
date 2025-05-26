import javax.swing.*;

public class Login {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String phoneNumber;

    public Login(String firstName, String lastName, String username, String password, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public boolean checkUserName() {
        return username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity() {
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasNumber = password.matches(".*\\d.*");
        boolean hasSpecialChar = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");
        boolean isLongEnough = password.length() >= 8;
        return hasUppercase && hasNumber && hasSpecialChar && isLongEnough;
    }

    public boolean checkCellPhoneNumber() {
        String regex = "\\+27\\d{9}";
        return phoneNumber.matches(regex);
    }

    public String registerUser() {
        if (!checkUserName()) {
            return "Username is not correctly formatted. It must contain an underscore and be no more than 5 characters.";
        }
        if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted. It must contain at least 8 characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber()) {
            return "Cell phone number incorrectly formatted. It must start with +27 and have 9 digits after.";
        }
        return "Registration successful.";
    }

    public boolean loginUser(String enteredUsername, String enteredPassword, String enteredPhoneNumber) {
        return this.username.equals(enteredUsername) &&
                this.password.equals(enteredPassword) &&
                this.phoneNumber.equals(enteredPhoneNumber);
    }

    public static void main(String[] args) {
        // Registration inputs
        String firstName = JOptionPane.showInputDialog("Enter your first name:");
        String lastName = JOptionPane.showInputDialog("Enter your last name:");

        String username;
        while (true) {
            username = JOptionPane.showInputDialog("Enter username (must contain '_' and be <= 5 characters):");
            Login tempUser = new Login(firstName, lastName, username, "", "");
            if (tempUser.checkUserName()) break;
            else JOptionPane.showMessageDialog(null, "Username must contain an underscore and be max 5 characters.");
        }

        String password;
        while (true) {
            password = JOptionPane.showInputDialog("Enter password (8+ chars, uppercase, number, special char):");
            Login tempUser = new Login(firstName, lastName, username, password, "");
            if (tempUser.checkPasswordComplexity()) break;
            else JOptionPane.showMessageDialog(null, "Password must be at least 8 characters, contain uppercase, number and special character.");
        }

        String phoneNumber;
        while (true) {
            phoneNumber = JOptionPane.showInputDialog("Enter phone number (e.g. +27838968976):");
            Login tempUser = new Login(firstName, lastName, username, password, phoneNumber);
            if (tempUser.checkCellPhoneNumber()) break;
            else JOptionPane.showMessageDialog(null, "Phone number must start with +27 and have 9 digits after.");
        }

        Login user = new Login(firstName, lastName, username, password, phoneNumber);
        String registrationMessage = user.registerUser();
        JOptionPane.showMessageDialog(null, registrationMessage);

        if (registrationMessage.equals("Registration successful.")) {
            int loginAttempts = 0;
            boolean loggedIn = false;

            while (loginAttempts < 3 && !loggedIn) {
                String loginUsername = JOptionPane.showInputDialog("Login - Enter username:");
                String loginPassword = JOptionPane.showInputDialog("Login - Enter password:");
                String loginPhone = JOptionPane.showInputDialog("Login - Enter phone number:");

                if (user.loginUser(loginUsername, loginPassword, loginPhone)) {
                    JOptionPane.showMessageDialog(null, "Welcome " + user.firstName + " " + user.lastName + "! It is great to see you again.");
                    loggedIn = true;
                } else {
                    loginAttempts++;
                    if (loginAttempts < 3)
                        JOptionPane.showMessageDialog(null, "Incorrect credentials. " + (3 - loginAttempts) + " attempt(s) left.");
                }
            }

            if (!loggedIn) {
                int option = JOptionPane.showConfirmDialog(null, "Too many failed attempts. Restart registration?", "Login Failed", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    main(null);
                } else {
                    JOptionPane.showMessageDialog(null, "Exiting. Goodbye!");
                    System.exit(0);
                }
            }

            if (loggedIn) {
                JOptionPane.showMessageDialog(null, "Welcome to QuickChat.");
                int messagesToSend = 0;

                while (messagesToSend <= 0) {
                    try {
                        String input = JOptionPane.showInputDialog("How many messages would you like to send?");
                        messagesToSend = Integer.parseInt(input);
                        if (messagesToSend <= 0)
                            JOptionPane.showMessageDialog(null, "Please enter a positive number.");
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Invalid input. Please enter a number.");
                    }
                }

                int messagesSentCount = 0;

                while (true) {
                    String menu = "Select an option:\n1) Send Messages\n2) Show recently sent messages \n3) Quit";
                    String option = JOptionPane.showInputDialog(menu);

                    if (option == null) break;

                    switch (option) {
                        case "1":
                            for (int i = 0; i < messagesToSend; i++) {
                                String recipient;
                                while (true) {
                                    recipient = JOptionPane.showInputDialog("Enter recipient cell number (max: 10 characters, starts with '+'):");
                                    if (recipient == null) break;
                                    if (recipient.length() <= 10 && recipient.startsWith("+")) break;
                                    else JOptionPane.showMessageDialog(null, "Recipient number invalid. Must be max 10 chars and start with '+'.");
                                }
                                if (recipient == null) break;

                                String messageText;
                                while (true) {
                                    messageText = JOptionPane.showInputDialog("Enter message (max 50 characters):");
                                    if (messageText == null) break;
                                    if (messageText.length() <= 50) break;
                                    else JOptionPane.showMessageDialog(null, "Please enter a message of less than 50 characters.");
                                }
                                if (messageText == null) break;

                                String messageID = Message.generateMessageID();

                                Message msg = new Message(messageID, recipient, messageText);

                                if (!msg.checkMessageID()) {
                                    JOptionPane.showMessageDialog(null, "Invalid message ID generated. Try again.");
                                    i--;
                                    continue;
                                }
                                if (!msg.checkRecipientCell()) {
                                    JOptionPane.showMessageDialog(null, "Recipient cell number invalid.");
                                    i--;
                                    continue;
                                }

                                String userChoice = msg.sentMessage();

                                if (userChoice.equals("Message sent")) {
                                    messagesSentCount++;
                                    String details = "Message ID: " + msg.getMessageID() + "\n"
                                            + "Message Hash: " + msg.createMessageHash() + "\n"
                                            + "Recipient: " + msg.getRecipient() + "\n"
                                            + "Message: " + msg.getMessageText();
                                    JOptionPane.showMessageDialog(null, details);
                                } else if (userChoice.equals("Message stored")) {
                                    JOptionPane.showMessageDialog(null, "Message stored for later.");
                                } else if (userChoice.equals("Message disregarded")) {
                                    JOptionPane.showMessageDialog(null, "Message disregarded.");
                                } else {
                                    JOptionPane.showMessageDialog(null, "No valid option selected, message disregarded.");
                                }
                            }
                            JOptionPane.showMessageDialog(null, "Total messages sent: " + messagesSentCount);
                            break;

                        case "2":
                            JOptionPane.showMessageDialog(null, "Coming Soon.");
                            break;

                        case "3":
                            JOptionPane.showMessageDialog(null, "Goodbye!");
                            System.exit(0);
                            break;

                        default:
                            JOptionPane.showMessageDialog(null, "Invalid option. Please enter 1, 2, or 3.");
                    }
                }
            }
        }
    }
}
