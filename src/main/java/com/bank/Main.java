package com.bank;

import com.bank.client.Client;
import com.bank.client.ClientDB;
import com.bank.lib.Utils;

public class Main {
    public static void main(String[] args) {
        Utils.clearTerminal();

        // Criar o cliente via interface
        Client client = Client.createClientUi();
        if (client == null) {
            System.out.println("[!] Cliente inválido. Encerrando.");
            return;
        }

        // Criar DB e tabela
        ClientDB db = new ClientDB();

        // Salvar o cliente no banco SQLite
        db.insertOrUpdateClient(client);

    }
}
