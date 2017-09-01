# InteractingBot
**Please note that this project is in development and there might occure errors and problems.**

This bot works with api.ai to answer to messages on your Discord Server.
If you're interested in this project, please help to improve it by contribute to the code or create issues.

## Installation and usage

At the moment, you have to compile this project for yourself or ask me for the compiled .jar file.
The best way to start the bot, you should use a start script. An example script could looks like this:
```batch
java -jar InteractingBot.jar config.json
```
In this case, the bot tries to find the file `config.json` in the same directory. In this config, you have to set your tokens for Discord and API.AI.
An example config file:
```json
{
  "discord-token": "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567",
  "api-ai-token": "abcdefghijklmnopqrstuvwxyz12345678"
}
```
The bot will automatically create a log folder and save logs there
