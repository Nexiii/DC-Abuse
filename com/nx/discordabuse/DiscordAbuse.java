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

	public static final String version = "0.1.2_A";
	public static String botID;
	
	public static void main(String[] args) {
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date());
		
		  try {
			  File tempFolder = new File("temp/");
			  if(!tempFolder.exists()) {
				  System.out.println(timeStamp+"[DC/Abuse] temp/ folder created");
				  tempFolder.mkdir();
			  }
			  File filesFolder = new File("files/");
			  if(!filesFolder.exists()) {
				  System.out.println(timeStamp+"[DC/Abuse] files/ folder created");
				  filesFolder.mkdir();
			  }
			  File idFile = new File("cfg/id.txt");
			  if(idFile.exists()) {
				  if(!readFile(idFile.getPath(), StandardCharsets.UTF_8).isBlank()) {
					  botID = readFile(idFile.getPath(), StandardCharsets.UTF_8);
					  File tokenFile = new File("cfg/token.txt");
					  if(tokenFile.exists()) {
						  String token = readFile(tokenFile.getPath(), StandardCharsets.UTF_8);  
						  JDABuilder.createLight(token.trim(), EnumSet.of(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT))
					      	.addEventListeners(new MessageReceiveListener())
					      	.setStatus(OnlineStatus.DO_NOT_DISTURB)
					      	.build();
						  System.out.println(timeStamp + "[DC/Abuse] Version: "+version);
						  System.out.println(timeStamp + "[DC/Abuse] Bot started");
					  } else {
						  System.err.println(timeStamp+"[DC/Abuse] Token file not found!, Stopping Bot");
						  System.exit(0);
					  }
				  } else {
					  System.err.println(timeStamp+"[DC/Abuse] ID file is Empty, Stopping Bot");
					  System.exit(0);
				  }
				  
			  } else {
				  System.err.println(timeStamp+"[DC/Abuse] ID file not found!, Stopping Bot");
				  System.exit(0);
			  }
		} catch (IOException e) {
			System.err.println(timeStamp+"[DC/Abuse] Error while creating folders");
			System.exit(0);
		}
	}
	
	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

}
