package eu.hannesgreule.interactingBot;

import ai.api.AIServiceException;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageEvent;
import sx.blah.discord.handle.obj.IMessage;

/**
 * @author Hannes
 */
public class MessageHandler {

    private IMessage message;

    public MessageHandler(MessageEvent event) {
        message = event.getMessage();
    }

    public AIResponse getResponse() {
        AIRequest request = new AIRequest(message.getContent());
        try {
            return InteractingBot.service.request(request);
        } catch (AIServiceException e) {
            e.printStackTrace();
        }
        return null;
    }

}
