package me.zp4rker.discord.microbot.cmd;

import me.zp4rker.discord.core.command.ICommand;
import me.zp4rker.discord.core.command.RegisterCommand;
import me.zp4rker.discord.core.yaml.file.Yaml;
import me.zp4rker.discord.microbot.util.MessageUtil;
import me.zp4rker.discord.microbot.util.YamlUtil;
import me.zp4rker.discord.microbot.util.ColorUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.text.DecimalFormat;

/**
 * @author ZP4RKER
 */
public class CurrencyCommand implements ICommand {

    @RegisterCommand(aliases = "currency",
                    description = "Converts specified value to different currencies.",
                    usage = "{prefix}currency <AMOUNT> <CURRENCY-FROM> <CURRENCY-TO>")
    public void onCommand(Message message, String[] args) {
        if (args.length != 3) {
            MessageUtil.selfDestruct(MessageUtil.sendError("Invalid arguments!", "Wrong arguments!\n\nUsage: `.currency <AMOUNT> " +
                    "<CURRENCY-FROM> <CURRENCY-TO>`", message), 6000);
            return;
        }

        try {
            double amount = Double.parseDouble(args[0]);

            String from = args[1].toUpperCase();
            String to = args[2].toUpperCase();

            Yaml yaml = new Yaml();
            yaml.loadFromString(YamlUtil.fromUrl("http://api.fixer.io/latest?symbols=" + from + "," + to));

            double fromRate = yaml.getDouble("rates." + from, from.equals("EUR") ? 1 : 0);
            double toRate = yaml.getDouble("rates." + to, to.equals("EUR") ? 1 : 0);

            if (fromRate == 0 || toRate == 0) {
                String reply = fromRate == 0 ? (toRate == 0 ? "Both currencies are invalid!" : from + " is an" +
                        " invalid currency!") : to + " is an invalid currency!";
                MessageUtil.selfDestruct(MessageUtil.sendError("Invalid arguments!", reply, message), 6000);
                return;
            }

            double conv = toRate / fromRate;

            DecimalFormat df = new DecimalFormat("#.##");
            double convAmount = Double.parseDouble(df.format(conv * amount));

            message.getChannel().sendMessage(new EmbedBuilder().setTitle(amount + " " + from + " = " + convAmount + " " + to)
                    .setColor(ColorUtil.randomColour()).build()).complete();
        } catch (Exception e) {
            e.printStackTrace();

            if (e instanceof NumberFormatException)
                MessageUtil.selfDestruct(MessageUtil.sendError("Invalid arguments!", args[0] + " is not a number!", message), 6000);
        }
    }

}