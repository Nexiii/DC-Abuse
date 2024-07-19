package com.nx.discordabuse.listeners;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HexFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.nx.discordabuse.DiscordAbuse;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.FileUpload;

public class MessageReceiveListener extends ListenerAdapter {
	Random rand = new Random();
	int randm = rand.nextInt(1000000, 99999999);
	  @Override
	  public void onMessageReceived(MessageReceivedEvent event) throws IndexOutOfBoundsException {
		  String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date());
		  String[] args = event.getMessage().getContentRaw().split(" ");
		  EmbedBuilder error = new EmbedBuilder();
		  error.setColor(Color.RED);
		  error.setTitle("Error");
	    	if(args.length == 3) {
				if(args[0].equals(">upload")) {
		    		event.getMessage().delete().queue();
    				if(args[1] != null) {
	    				if(args[2] != null) {
	    					try {
	    						File file = new File(args[1]+ "/" + args[2]);
				    			if(file.exists()) {
						    		EmbedBuilder idle = new EmbedBuilder();
						    		idle.setColor(Color.BLUE);
						    		idle.setTitle("Working");
						    		idle.setDescription("Working on " + args[1]+"/"+args[2]);
				    				event.getChannel().sendMessageEmbeds(idle.build()).queue( message -> message.delete().queueAfter(2, TimeUnit.SECONDS) );
				    				String encoded = encodeFileToBase64(file);
				    				String converted = toHex(encoded);
				    				byte[] convertedBytes = converted.getBytes();
				    				File encodedFile = new File("temp//encoded-"+ randm+ "-" +args[2]);
				    				try (FileOutputStream fos = new FileOutputStream(encodedFile)) {
				    					fos.write(convertedBytes);
				    					fos.flush();
									}
				    				FileUpload convertedFile = FileUpload.fromData(encodedFile);
				    				EmbedBuilder complete = new EmbedBuilder();
				    				complete.setColor(Color.GREEN);
				    				complete.setTitle("Complete");
				    				complete.setDescription("File Encoded and Uploaded");
				    				Path path = Paths.get("temp//encoded-"+ randm+ "-" +args[2]);
									int bytes = Files.readAllBytes(path).length;
									System.out.println(timeStamp+"[DC/Abuse] ID: "+randm+" | Bytes: "+bytes);
				    				if(bytes < 26214400) {
				    					event.getChannel().sendMessageEmbeds(complete.build()).queue( message -> message.delete().queueAfter(5, TimeUnit.SECONDS) );
				    					event.getChannel().sendMessage(Integer.toString(randm)).addFiles(convertedFile).queue();
				    				} else {
				    					  error.setDescription("Size too Big");
				    					  event.getChannel().sendMessageEmbeds(error.build()).queue( message -> message.delete().queueAfter(2, TimeUnit.SECONDS) );
				    				}
				    				File oldTemp = new File("temp/");
				    				for(File tempFile: oldTemp.listFiles()) {
				    				    if (!tempFile.isDirectory()) {
				    				    	tempFile.delete();
				    				    }
				    				}
				    				System.out.println(timeStamp + "[DC/Abuse] Temp Cleaned");
				    			} else {
				    				error.setDescription("File doesn't exists");
				    				event.getChannel().sendMessageEmbeds(error.build()).queue( message -> message.delete().queueAfter(2, TimeUnit.SECONDS) );
				    			}
	    					} catch (ArrayIndexOutOfBoundsException e) {
	    						event.getMessage().delete().queue();
	    						error.setDescription("Error using Command");
	    						event.getChannel().sendMessageEmbeds(error.build()).queue( message -> message.delete().queueAfter(2, TimeUnit.SECONDS) );
	    					} catch (FileNotFoundException e) {} catch (IOException e) {}
			    		}	else {
			    			event.getMessage().delete().queue();
			    			error.setDescription("Error using Command");
		    				event.getChannel().sendMessageEmbeds(error.build()).queue( message -> message.delete().queueAfter(2, TimeUnit.SECONDS) );
		    			}	    				
	    			} else {
	    				event.getMessage().delete().queue();
	    				error.setDescription("Error using Command");
	    				event.getChannel().sendMessageEmbeds(error.build()).queue( message -> message.delete().queueAfter(2, TimeUnit.SECONDS) );
	    			}
    			} else {
    				event.getMessage().delete().queue();
    				error.setDescription("Error using Command");
    				event.getChannel().sendMessageEmbeds(error.build()).queue( message -> message.delete().queueAfter(2, TimeUnit.SECONDS) );
    			}
	    	}
	    	if (args.length == 1) {
	    		if(args[0].equals(">help")) {
	    			event.getMessage().delete().queue();
		    		EmbedBuilder complete = new EmbedBuilder();
					complete.setColor(Color.MAGENTA);
					complete.setTitle("Help | Commands");
					complete.setDescription(">upload <file path> <file name> | To upload the encoded file to Discord");
					complete.setFooter("v"+DiscordAbuse.version);
					event.getChannel().sendMessageEmbeds(complete.build()).queue();
	    		} else {
	    			int oldRandm = randm;
		    		if(event.getMessage().getContentRaw().contains(Integer.toString(oldRandm)) && event.getAuthor().getId() == DiscordAbuse.botID) {
		    			String fileName = event.getMessage().getAttachments().get(0).getFileName();
		    			String messageID = event.getMessage().getId();
		    			byte[] messageText = messageID.getBytes();
	    				File of = new File("files//"+fileName+".txt");
	    				try (FileOutputStream osf = new FileOutputStream(of)) {
							osf.write(messageText);
							osf.flush();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
		    		}
	    		}
			}else if(args.length == 2) {
				if(args[0].equals(">download")) {
					if(args[1] != null) {
						event.getChannel().retrieveMessageById(DiscordAbuse.botID);
					}
				}
			} else {}
	  }
	  	private static String encodeFileToBase64(File file) {
		    try {
		        byte[] fileContent = Files.readAllBytes(file.toPath());
		        return Base64.getEncoder().encodeToString(fileContent);
		    } catch (IOException e) {
		        throw new IllegalStateException("could not read file " + file, e);
		    }
	  	}

		private static byte[] decodeBase64toBytes(String encodedText) {
	  		return Base64.getDecoder().decode(encodedText);
	  	}

		private static void byteToFile(byte[] bytes, String fileName) {
	  		File file = new File(fileName);
	  		try {
				file.createNewFile();
				try (FileOutputStream osf = new FileOutputStream(file)) {
					osf.write(bytes);
					osf.flush();
				}
			} catch (IOException e) {
				System.err.println("Error");
			}	  		
	  	}
	  	
	  	public String toHex(String value) {
	  	    return HexFormat.of().formatHex(value.getBytes());
	  	}
	  	
	  	public static byte[] hexStringToByteArray(String hex) {
	  	    int l = hex.length();
	  	    byte[] data = new byte[l / 2];
	  	    for (int i = 0; i < l; i += 2) {
	  	        data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
	  	                + Character.digit(hex.charAt(i + 1), 16));
	  	    }
	  	    return data;
	  	}
}