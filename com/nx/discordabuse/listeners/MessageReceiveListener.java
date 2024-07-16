package com.nx.discordabuse.listeners;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HexFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.FileUpload;

public class MessageReceiveListener extends ListenerAdapter {
	  @Override
	  public void onMessageReceived(MessageReceivedEvent event) throws ArrayIndexOutOfBoundsException {
		  String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date());
		  File oldTemp = new File("temp/");
		  String[] args = event.getMessage().getContentRaw().split(" ");
		  EmbedBuilder error = new EmbedBuilder();
		  error.setColor(Color.RED);
		  error.setTitle("Error");
	    	if(args[0].equals(">upload")) {
	    		event.getMessage().delete().queue();
	    		EmbedBuilder idle = new EmbedBuilder();
	    		idle.setColor(Color.BLUE);
	    		idle.setTitle("Working");
	    		idle.setDescription("Working on " + args[1]+"/"+args[2]);
	    		event.getChannel().sendMessageEmbeds(idle.build()).queue( message -> message.delete().queueAfter(2, TimeUnit.SECONDS) );
	    		Random rand = new Random();
				int randm = rand.nextInt(1000000, 99999999);
	    		try {
	    			if(args[0] != null) {
		    			if(args[1] != null) {
		    				if(args[2] != null) {
		    					File file = new File(args[1]+ "/" + args[2]);
				    			if(file.exists()) {
				    				String encoded = encodeFileToBase64(file);
				    				String converted = toHex(encoded);
				    				byte[] btDataFile = converted.getBytes();
				    				File of = new File("temp//encoded-"+ randm+ "-" +args[2]);
				    				try (FileOutputStream osf = new FileOutputStream(of)) {
										osf.write(btDataFile);
										osf.flush();
									}
				    				if(of.getAbsoluteFile().getTotalSpace() > 25000000) {
					    				FileUpload convertedFile = FileUpload.fromData(of);
					    				EmbedBuilder complete = new EmbedBuilder();
					    				complete.setColor(Color.GREEN);
					    				complete.setTitle("Complete | ID: " + randm);
					    				complete.setDescription("File Encoded and Uploaded");
					    				complete.setFooter(Integer.toString(randm));
					    				event.getChannel().sendMessageEmbeds(complete.build()).queue();
					    				event.getChannel().sendFiles(convertedFile).queue();
					    				of.delete();
				    				} else {
				    					  error.setDescription("Size too Big");
				    					  event.getChannel().sendMessageEmbeds(error.build()).queue( message -> message.delete().queueAfter(2, TimeUnit.SECONDS) );
				    				}
				    				for(File tempFile: oldTemp.listFiles()) {
				    				    if (!tempFile.isDirectory()) {
				    				    	tempFile.delete();
				    						System.out.println(timeStamp + "[DC/Abuse] Temp Cleaned");
				    				    }
				    				}
				    			}
				    		}		    				
		    			} else {
		    				error.setDescription("File doesnt Exists");
		    				event.getChannel().sendMessageEmbeds(error.build()).queue( message -> message.delete().queueAfter(2, TimeUnit.SECONDS) );
		    			}
		    		}
				} catch (ArrayIndexOutOfBoundsException e) {
					error.setDescription("Error using Command");
					event.getChannel().sendMessageEmbeds(error.build()).queue( message -> message.delete().queueAfter(2, TimeUnit.SECONDS) );
				} catch (FileNotFoundException e) {
				} catch (IOException e) {
				}
	    	} else if (args[0].equals(">help")) {
	    		EmbedBuilder complete = new EmbedBuilder();
				complete.setColor(Color.MAGENTA);
				complete.setTitle("Help");
				complete.setDescription(">upload <file path> <file name> | To upload the encoded file to Discord");
				event.getChannel().sendMessageEmbeds(complete.build()).queue();
			}
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