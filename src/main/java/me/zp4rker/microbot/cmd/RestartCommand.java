package me.zp4rker.microbot.cmd;

import me.zp4rker.core.command.ICommand;
import me.zp4rker.core.command.RegisterCommand;
import me.zp4rker.core.exception.ExceptionHandler;
import me.zp4rker.microbot.util.MessageUtil;
import net.dv8tion.jda.core.entities.Message;

/**
 * @author ZP4RKER
 */
public class RestartCommand implements ICommand {

    @RegisterCommand(aliases = "restart")
    public void onCommand(Message message) {
        if (!message.getAuthor().getId().equals("145064570237485056")) return;

        MessageUtil.bypassDeleteLogs(message);

        message.getJDA().shutdown();

        try {
            Runtime.getRuntime().exec("/home/bots/start-microbot.sh").waitFor();
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

}
