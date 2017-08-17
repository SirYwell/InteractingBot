package eu.hannesgreule.interactingBot;

import ai.api.model.AIResponse;
import org.slf4j.Logger;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

/**
 * @author Hannes
 */
public class Listeners {

    private static long lastSent = 0L;
    private Logger logger;
    private String game;

    public Listeners(Logger logger, String game) {
        this.logger = logger;
        this.game = game;
    }

    @EventSubscriber
    public void onMessage(MessageReceivedEvent event) {
        if (lastSent > System.currentTimeMillis()) {
            return;
        }
        if (!event.getMessage().getMentions().contains(event.getClient().getOurUser())) {
            logger.info("Trying to get answer");
            MessageHandler handler = new MessageHandler(event);
            AIResponse response = handler.getResponse();
            if (response.getStatus().getCode() == 200) {
                lastSent = System.currentTimeMillis() + 7500;
                if (response.getResult().getFulfillment().getSpeech().equals("")) {
                    return;
                }
                logger.info("Got an answer");
                event.getChannel().setTypingStatus(true);
                String speech = response.getResult().getFulfillment().getSpeech();
                int milliseconds = speech.length() * 150;
                logger.debug("Speech length: " + milliseconds);
                logger.debug("Now: " + System.currentTimeMillis());
                try {
                    Thread.sleep(milliseconds);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                logger.debug("Now: " + System.currentTimeMillis());
                logger.info("Sending answer");
                event.getChannel().sendMessage(speech);
                event.getChannel().setTypingStatus(false);
                return;
            }
            logger.warn("Refused connection with status code " + response.getStatus().getCode());
        }
    }

    @EventSubscriber
    public void onReady(ReadyEvent event) {
        event.getClient().changePlayingText(game);
    }
}
