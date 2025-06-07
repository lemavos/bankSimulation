package com.bank.lib;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import com.bank.client.Client;

public class JsonSaver {
    public static void saveClientsToJson(Map<String, Client> newClients, String filePath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Map<String, Client> allClients = new HashMap<>();

        // 1. Tenta ler o arquivo JSON existente
        try (FileReader reader = new FileReader(filePath)) {
            allClients = gson.fromJson(reader, new TypeToken<Map<String, Client>>() {}.getType());

            if (allClients == null) {
                allClients = new HashMap<>();
            }
        } catch (IOException e) {
            // Se não conseguir ler (arquivo não existe), começa com mapa vazio
            System.out.println("JSON not found or empty, starting with a new map.");
        }

        // 2. Adiciona os novos clientes ao mapa
        allClients.putAll(newClients);

        // 3. Escreve o JSON atualizado no arquivo
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(allClients, writer);
            System.out.println("Clients saved successfully");
        } catch (IOException e) {
            System.out.println("[!] Error: " + e.getMessage());
        }
    }
}

