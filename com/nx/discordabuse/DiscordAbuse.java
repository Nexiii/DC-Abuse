package com.nx.discordabuse;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;

import com.nx.discordabuse.listeners.MessageReceiveListener;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class DiscordAbuse {

	public static void main(String[] args) {
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date());
		  try {
			  File temp = new File("temp/");
			  if(!temp.exists()) {
				  temp.mkdir();
			  }
			  String token = readFile("cfg/token.txt", StandardCharsets.UTF_8);
			  JDABuilder.createLight(token.trim(), EnumSet.of(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT))
		      	.addEventListeners(new MessageReceiveListener())
		      	.setStatus(OnlineStatus.DO_NOT_DISTURB)
		      	.build();
			  System.out.println(timeStamp + "[DC/Abuse] Bot started");
			  temp.deleteOnExit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

}
