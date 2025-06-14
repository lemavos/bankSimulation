package com.bank.models;

import com.bank.menus.Banners;
import com.bank.services.EmailSender;
import com.bank.services.IdGen;
import com.bank.services.Utils;
import com.bank.services.ValCodeGen;
import java.math.BigDecimal;

public class Client {

    private String id;
    private boolean status;
    private BigDecimal credit;
    private String name;
    private String email;
    private String phone;
    private String password;
    private transient String validateCode;

    private Client(String name, String email, String phone, String password) {
        createClient(name, email, phone, password);
    }

    public static Client createClientUi() {
        Banners.printCreateAccountBanner();
        System.out.println("+=====================================+");

        System.out.print("|  Enter name: ");
        String name = Utils.input();
        System.out.print("|  Enter email: ");
        String email = Utils.input();
        System.out.print("|  Enter phone (optional): ");
        String phone = Utils.input();
        System.out.print("|  Enter password: ");
        String password = Utils.input();
        System.out.println("+=====================================+");

        // Validate inputs
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            System.out.println(
                "|  [!] All fields are required. Please try again."
            );
            System.out.println("+=====================================+");
            return null;
        }
        if (!Utils.isValidEmail(email)) {
            System.out.println(
                "|  [!] Invalid email format. Please try again."
            );
            System.out.println("+=====================================+");
            return null;
        }
        if (phone != null && !phone.isEmpty()) {
            if (!Utils.isValidPhone(phone)) {
                System.out.println(
                    "|  [!] Invalid phone number format. Please try again."
                );
                System.out.println("+=====================================+");
            }
        }
        if (!Utils.isValidPassword(password)) {
            System.out.println(
                "|  [!] Invalid password format. Please try again."
            );
            System.out.println("+=====================================+");
            return null;
        }

        Client client = new Client(name, email, phone, password);
        client.validateCode = ValCodeGen.generateCode();
        System.out.println("+=====================================+");
        EmailSender.sendValEmail(client.email, client.validateCode);

        boolean activated = client.activateClient();
        if (!activated) {
            System.out.println(
                "|  [!] Client creation failed due to invalid code."
            );
            System.out.println("+=====================================+");
            return null;
        }

        return client;
    }

    public void info() {
        System.out.println("\nClient Information:");
        System.out.println("  Name: " + this.name);
        System.out.println("  Email: " + this.email);
        System.out.println("  Phone: " + this.phone);
        System.out.println("  ID: " + this.id);
        System.out.println("  Status: " + this.status);
        System.out.println("  Credit: " + this.credit);
    }

    private void createClient(
        String name,
        String email,
        String phone,
        String password
    ) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.id = new IdGen().generateId();
        this.status = false;
        this.credit = new BigDecimal(0);
        this.password = password;
        System.out.println("|  Client created with ID: " + this.id);
    }

    private boolean activateClient() {
        System.err.println("Val code: " + this.validateCode); // remove after tests
        System.out.print("|  Insert validation code: ");
        String codeInsert = Utils.input();
        if (codeInsert.isEmpty()) {
            System.out.println("\n|  [!] Code validation is empty");
            System.out.println("+=====================================+");
            return false;
        }

        if (codeInsert.equals(this.validateCode)) {
            this.status = true;
            System.out.println("|  Client activated with ID: " + this.id);
            System.out.println("+=====================================+");
            return true;
        } else {
            System.out.println("|  [!] Invalid validation code");
            System.out.println("+=====================================+");
            return false;
        }
    }

    // criar uma ui pra isso
    public void deleteClient(Client client) {
        if (client.credit.compareTo(BigDecimal.ZERO) > 0) {
            System.out.println(
                "\n[!] Client cannot be deleted. Credit must be zero."
            );
            return;
        }
        client.status = false;
        System.out.println("\nClient deleted with ID: " + client.id);
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public boolean getStatus() {
        return status;
    }

    // Setters
    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }
}
