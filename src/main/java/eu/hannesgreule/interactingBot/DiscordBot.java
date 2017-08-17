package eu.hannesgreule.interactingBot;

import org.slf4j.Logger;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.util.DiscordException;

/**
 * @author Hannes
 */
public class DiscordBot extends Thread {

    private String token;
    private IDiscordClient client;
    private Logger logger;
    private String game;


    public DiscordBot(String token, Logger logger, String game) {
        this.token = token;
        this.logger = logger;
        this.game = game;
    }

    @Override
    public void run() {
        ClientBuilder clientBuilder = new ClientBuilder();
        clientBuilder.withToken(token);
        try {
            client = clientBuilder.login();
            EventDispatcher dispatcher = client.getDispatcher();
            dispatcher.registerListener(new Listeners(logger, game));
        } catch (DiscordException e) {
            e.printStackTrace();
        }
    }

    public IDiscordClient getClient() {
        return client;
    }
}
