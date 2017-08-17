package eu.hannesgreule.interactingBot;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Layout;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.TTCCLayout;
import org.slf4j.Logger;
import sx.blah.discord.Discord4J;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Hannes
 */
public class InteractingBot {

    public static AIDataService service;
    private static String DISCORD_TOKEN;
    private static String APIAI_TOKEN;

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.print("Missing argument: config file");
            return;
        }
        Layout layout = new TTCCLayout("[HH:mm:ss]");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String name = dateFormat.format(new Date());
        RollingFileAppender appender = new RollingFileAppender(layout, "logs/" + name + ".log");
        appender.setName("logfile");
        appender.setImmediateFlush(true);
        BasicConfigurator.configure(appender);
        Logger logger = Discord4J.LOGGER;
        logger.info("Reading from config...");
        File file = new File(args[0]);
        if (!file.exists()) {
            logger.error("Missing config file");
            return;
        }
        BufferedReader reader = new BufferedReader(new FileReader(file));
        JsonParser parser = new JsonParser();
        JsonObject object = (JsonObject) parser.parse(reader);
        if (object.has("api-ai-token")) {
            String s = object.get("api-ai-token").getAsString();
            if(s.equalsIgnoreCase("")) {
                logger.error("Please provide a api.ai token in config");
                return;
            }
            APIAI_TOKEN = object.get("api-ai-token").getAsString();
        } else {
            logger.error("Missing key: api-ai-token");
            return;
        }
        if (object.has("discord-token")) {
            String s = object.get("discord-token").getAsString();
            if(s.equalsIgnoreCase("")) {
                logger.error("Please provide a discord bot token in config");
                return;
            }
            DISCORD_TOKEN = s;
        } else {
            logger.error("Missing key: discord-token");
            return;
        }
        logger.info("initializing AI API...");
        AIConfiguration configuration = new AIConfiguration(APIAI_TOKEN);
        service = new AIDataService(configuration);
        AIRequest gameRequest = new AIRequest("game");
        String game = "Interacting Bot";
        try {
            AIResponse response = service.request(gameRequest);
            if(response.getStatus().getCode() != 200) {
                logger.warn("Refused connection with status code " + response.getStatus().getCode());
                return;
            }
            game = response.getResult().getFulfillment().getSpeech();
            logger.info("Setting 'game' to " + game);
        } catch (AIServiceException e) {
            e.printStackTrace();
        }
        logger.info("Starting Discord Client");
        DiscordBot bot = new DiscordBot(DISCORD_TOKEN, logger, game);
        bot.start();
    }
}
