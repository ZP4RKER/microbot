package me.zp4rker.discord.microbot.cmd;

import me.zp4rker.discord.core.command.ICommand;
import me.zp4rker.discord.core.command.RegisterCommand;
import me.zp4rker.discord.core.logger.ZLogger;
import me.zp4rker.discord.core.yaml.file.Yaml;
import me.zp4rker.discord.microbot.util.MessageUtil;
import me.zp4rker.discord.microbot.util.YamlUtil;
import net.dv8tion.jda.core.entities.Message;

import java.util.LinkedHashMap;

/**
 * @author ZP4RKER
 */
public class DogCommand implements ICommand {

    @RegisterCommand(aliases = "dog",
                    description = "Sends a random picture of a dog.",
                    usage = "{prefix}dog")
    public void onCommand(Message message) {
        MessageUtil.bypassDeleteLogs(message);

        try{
            Yaml yaml = new Yaml();
            yaml.loadFromString(YamlUtil.fromUrl("https://api.thedogapi.co.uk/v2/dog.php").replaceAll("\\\\([\"/])", "$1"));

            String url = ((LinkedHashMap) yaml.getList("data").get(0)).get("url").toString();
            MessageUtil.selfDestruct(message.getChannel().sendMessage(url).complete(), 20000);

        } catch (Exception e) {
            e.printStackTrace();
            ZLogger.warn("Could not get data!");
        }
    }

}