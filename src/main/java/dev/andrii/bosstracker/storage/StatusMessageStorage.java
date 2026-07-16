package dev.andrii.bosstracker.storage;

import net.dv8tion.jda.api.entities.Message;

public class StatusMessageStorage {

    private volatile Message message;

    public void setMessage(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}