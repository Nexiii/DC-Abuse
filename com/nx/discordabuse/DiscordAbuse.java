package com.nx.discordabuse;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.Scanner;

import com.nx.discordabuse.listeners.MessageReceiveListener;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class DiscordAbuse {

	public static final String version = "0.1.3_A";
	public static String botID;

	public static void main(String[] args) {
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date());
		URL url;
		try {
			url = new URL("https://raw.githubusercontent.com/Nexiii/DC-Abuse/main/version.txt");
			Scanner sc = new Scanner(url.openStream());
			StringBuffer sb = new StringBuffer();
			while (sc.hasNext()) {
				sb.append(sc.next());
			}
			String result = sb.toString();
			result = result.replaceAll("<[^>]*>", "");
			try {
				File tempFolder = new File("temp/");
				if (!tempFolder.exists()) {
					System.out.println(timeStamp + "[DC/Abuse] temp/ folder created");
					tempFolder.mkdir();
				}
				File filesFolder = new File("files/");
				if (!filesFolder.exists()) {
					System.out.println(timeStamp + "[DC/Abuse] files/ folder created");
					filesFolder.mkdir();
				}
				File idFile = new File("cfg/id.txt");
				if (idFile.exists()) {
					if (!MessageReceiveListener.readFile(idFile.getPath(), StandardCharsets.UTF_8).isBlank()) {
						botID = MessageReceiveListener.readFile(idFile.getPath(), StandardCharsets.UTF_8);
						File tokenFile = new File("cfg/token.txt");
						if (tokenFile.exists()) {
							String token = MessageReceiveListener.readFile(tokenFile.getPath(), StandardCharsets.UTF_8);
							JDABuilder
									.createLight(token.trim(),EnumSet.of(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT))
									.addEventListeners(new MessageReceiveListener())
									.setStatus(OnlineStatus.DO_NOT_DISTURB).build();
							System.out.println(timeStamp + "[DC/Abuse] Version: " + version);
							if (!result.equals(version)) {
								System.out.println(
										timeStamp + "[DC/Abuse] A newer version is available (v" + result + ")");
							}
							System.out.println(timeStamp + "[DC/Abuse] Bot started");
						} else {
							System.err.println(timeStamp + "[DC/Abuse] Token file not found!, Stopping Bot");
							System.exit(0);
						}
					} else {
						System.err.println(timeStamp + "[DC/Abuse] ID file is Empty, Stopping Bot");
						System.exit(0);
					}

				} else {
					System.err.println(timeStamp + "[DC/Abuse] ID file not found!, Stopping Bot");
					System.exit(0);
				}
			} catch (IOException e) {
				System.err.println(timeStamp + "[DC/Abuse] Error while creating folders");
				System.exit(0);
			}

		} catch (IOException e) {
		}
	}

}
