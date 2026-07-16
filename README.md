# Neo Boss Tracker

A Discord bot for tracking the **Imperial Tree** boss respawn in **Blade & Soul NEO**.

The bot helps players monitor the boss respawn window, keep track of the last kill time, 
and receive automatic Discord notifications when a new spawn tick begins.

## Features

- Slash commands for boss kill registration
- Manual kill time input
- Automatic status message updates
- Respawn timer calculation
- Spawn tick notifications (`@everyone`)
- Support for multiple game channels

## Technologies

- Java 17
- Gradle
- JDA (Java Discord API)

## Configuration

Common application settings are stored in `config/config.properties`.

Sensitive information, such as the Discord bot token, is loaded from environment variables.

```properties
timezone=Europe/Kiev
update.interval.seconds=30
```

Required environment variable:

```text
DISCORD_TOKEN=your_discord_bot_token
```

## Project Structure

```
src/main/java/dev/andrii/bosstracker
├── config
├── discord
├── factory
├── model
├── service
├── storage
└── util
```

## Future Plans

- Support for multiple bosses
- Persistent storage
- Historical statistics