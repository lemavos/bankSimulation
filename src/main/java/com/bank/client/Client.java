package com.bank.client;

import java.math.BigDecimal;

import com.bank.lib.Utils;
import com.bank.lib.EmailSender;

public class Client {
    private String id;
    private boolean status;
    private BigDecimal credit;
    private String name;
    private String email;
    private String phone;
    private transient String validateCode;

    public Client(String name, String email, String phone) {
        createClient(name, email, phone);
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

    public static Client createClientUi() {
        System.out.println("\nWelcome to the Client Creation Interface!");
        System.out.println("Please provide the following details to create a new client:");

        System.out.print("Enter name: ");
        String name = Utils.input();
        System.out.print("Enter email: ");
        String email = Utils.input();
        System.out.print("Enter phone: ");
        String phone = Utils.input();

        // Validate inputs
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            System.out.println("[!] All fields are required. Please try again.");
            return null;
        }
        if (!Utils.isValidEmail(email)) {
            System.out.println("[!] Invalid email format. Please try again.");
            return null;
        }
        if (!Utils.isValidPhone(phone)) {
            System.out.println("[!] Invalid phone number format. Please try again.");
            return null;
        }

        Client client = new Client(name, email, phone);
        client.validateCode = ValidationCodeGen.generateCode();
        EmailSender.sendValEmail(client.email, client.validateCode);

        boolean activated = client.activateClient();
        if (!activated) {
            System.out.println("[!] Client creation failed due to invalid code.");
            return null;
        }

        client.info();
        return client;
    }

    private void createClient(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.id = new IdGenerator().generateId();
        this.status = false;
        this.credit = new BigDecimal(0);
        System.out.println("\nClient created with ID: " + this.id);
    }

    private boolean activateClient() {
        System.out.print("Insert validation code: ");
        String codeInsert = Utils.input();

        if (codeInsert.isEmpty()) {
            System.out.println("\n[!] Code validation is empty");
            return false;
        }

        if (codeInsert.equals(this.validateCode)) {
            this.status = true;
            System.out.println("\nClient activated with ID: " + this.id);
            return true;
        } else {
            System.out.println("[!] Invalid validation code");
            return false;
        }
    }

    public void deactivateClient() {
        this.status = false;
        System.out.println("\nClient deactivated with ID: " + this.id);
    }

    public void deleteClient() {
        if (this.credit.compareTo(BigDecimal.ZERO) > 0) {
            System.out.println("\n[!] Client cannot be deleted. Credit must be zero.");
            return;
        }
        this.status = false;
        System.out.println("\nClient deleted with ID: " + this.id);
    }

    // Getters e Setters
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

    public boolean getStatus() {
        return status;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }
}
