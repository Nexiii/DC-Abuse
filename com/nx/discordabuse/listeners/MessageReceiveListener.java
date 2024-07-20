package com.nx.discordabuse.listeners;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
		if (args.length == 3) {
			if (args[0].equals(">upload")) {
				event.getMessage().delete().queue();
				if (args[1] != null) {
					if (args[2] != null) {
						try {
							File file = new File(args[1] + "/" + args[2]);
							if (file.exists()) {
								EmbedBuilder idle = new EmbedBuilder();
								idle.setColor(Color.BLUE);
								idle.setTitle("Working");
								idle.setDescription("Working on " + args[1] + "/" + args[2]);
								event.getChannel().sendMessageEmbeds(idle.build())
										.queue(message -> message.delete().queueAfter(2, TimeUnit.SECONDS));
								String encoded = encodeFileToBase64(file);
								String converted = toHex(encoded);
								byte[] convertedBytes = converted.getBytes();
								File encodedFile = new File("temp//encoded-" + randm + "-" + args[2]);
								try (FileOutputStream fos = new FileOutputStream(encodedFile)) {
									fos.write(convertedBytes);
									fos.flush();
								}
								FileUpload convertedFile = FileUpload.fromData(encodedFile);
								EmbedBuilder complete = new EmbedBuilder();
								complete.setColor(Color.GREEN);
								complete.setTitle("Complete");
								complete.setDescription("File Encoded and Uploaded");
								Path path = Paths.get("temp//encoded-" + randm + "-" + args[2]);
								int bytes = Files.readAllBytes(path).length;
								System.out.println(timeStamp + "[DC/Abuse] ID: " + randm + " | Bytes: " + bytes);
								if (bytes < 26214400) {
									event.getChannel().sendMessageEmbeds(complete.build())
											.queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
									event.getChannel().sendMessage(Integer.toString(randm)).addFiles(convertedFile).queue();
								} else {
									error.setDescription("Size too Big");
									event.getChannel().sendMessageEmbeds(error.build())
											.queue(message -> message.delete().queueAfter(2, TimeUnit.SECONDS));
								}
								File oldTemp = new File("temp/");
								for (File tempFile : oldTemp.listFiles()) {
									if (!tempFile.isDirectory()) {
										tempFile.delete();
									}
								}
								System.out.println(timeStamp + "[DC/Abuse] Temp Cleaned");
							} else {
								error.setDescription("File doesn't exists");
								event.getChannel().sendMessageEmbeds(error.build())
										.queue(message -> message.delete().queueAfter(2, TimeUnit.SECONDS));
							}
						} catch (ArrayIndexOutOfBoundsException e) {
							event.getMessage().delete().queue();
							error.setDescription("Error using Command");
							event.getChannel().sendMessageEmbeds(error.build())
									.queue(message -> message.delete().queueAfter(2, TimeUnit.SECONDS));
						} catch (FileNotFoundException e) {
						} catch (IOException e) {
						}
					} else {
						event.getMessage().delete().queue();
						error.setDescription("Error using Command");
						event.getChannel().sendMessageEmbeds(error.build())
								.queue(message -> message.delete().queueAfter(2, TimeUnit.SECONDS));
					}
				} else {
					event.getMessage().delete().queue();
					error.setDescription("Error using Command");
					event.getChannel().sendMessageEmbeds(error.build())
							.queue(message -> message.delete().queueAfter(2, TimeUnit.SECONDS));
				}
			} else {
				event.getMessage().delete().queue();
				error.setDescription("Error using Command");
				event.getChannel().sendMessageEmbeds(error.build())
						.queue(message -> message.delete().queueAfter(2, TimeUnit.SECONDS));
			}
		}
		if (args.length == 1) {
			if (args[0].equals(">help")) {
				event.getMessage().delete().queue();
				EmbedBuilder complete = new EmbedBuilder();
				complete.setColor(Color.MAGENTA);
				complete.setTitle("Help | Commands");
				complete.addField("Commands:",">upload <file path> <file name> | To upload the encoded file to Discord", false);
				complete.addField("", ">download <uploadID> | To download the decoded file from Discord", false);
				complete.setFooter("v" + DiscordAbuse.version);
				event.getChannel().sendMessageEmbeds(complete.build()).queue();
			} else {
				int oldRandm = randm;
				if (event.getMessage().getContentRaw().contains(Integer.toString(oldRandm))) {
					if (event.getMessage().getAuthor().getId().equals(DiscordAbuse.botID)) {
						String messageID = event.getMessage().getId();
						byte[] messageText = messageID.getBytes();
						File of = new File("files//" + oldRandm + ".txt");
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
			}
		} else if (args.length == 2) {
			if (args[0].equals(">download")) {
				event.getMessage().delete().queue();
				if (args[1] != null) {
					File fileFolder = new File("files/");
					for (File dwFile : fileFolder.listFiles()) {
						if (!dwFile.isDirectory()) {
							if (dwFile.getName().equals(args[1] + ".txt")) {
								try {
									String messageID = readFile(dwFile.getPath(), StandardCharsets.UTF_8);
									event.getChannel().retrieveMessageById(messageID).queue((message) -> {
										System.out.println(message.getAttachments().get(0).getUrl());
										try {
											saveUrl("temp//downloaded-" + message.getAttachments().get(0).getFileName(),message.getAttachments().get(0).getUrl());
											byte[] hexBytes;
											try {
												hexBytes = HexFormat.of().parseHex(readFile("temp//downloaded-"+ message.getAttachments().get(0).getFileName(),StandardCharsets.UTF_8));
												byte[] basetoString = Base64.getDecoder().decode(hexBytes);
												File decodedFile = new File("temp//decoded-"
														+ message.getAttachments().get(0).getFileName());
												System.out.println(basetoString.toString());
												try (FileOutputStream fos = new FileOutputStream(decodedFile)) {
													fos.write(basetoString);
													fos.flush();
												} catch (FileNotFoundException e) {} catch (IOException e) {}
												FileUpload convertedFile = FileUpload.fromData(decodedFile);
												event.getChannel().sendFiles(convertedFile).queue();
											} catch (IOException e) {
											}
										} catch (IOException e) {
										}
									});
									File oldTemp = new File("temp/");
									for (File tempFile : oldTemp.listFiles()) {
										if (!tempFile.isDirectory()) {
											tempFile.delete();
										}
									}
									System.out.println(timeStamp + "[DC/Abuse] Temp Cleaned");
								} catch (IOException e) {
								}
							} else {
								event.getMessage().delete().queue();
								error.setDescription("File not found");
								event.getChannel().sendMessageEmbeds(error.build())
										.queue(message -> message.delete().queueAfter(2, TimeUnit.SECONDS));
							}
						} else {
							event.getMessage().delete().queue();
							error.setDescription("File not found");
							event.getChannel().sendMessageEmbeds(error.build())
									.queue(message -> message.delete().queueAfter(2, TimeUnit.SECONDS));
						}
					}
				} else {
					event.getMessage().delete().queue();
					error.setDescription("Error using Command");
					event.getChannel().sendMessageEmbeds(error.build())
							.queue(message -> message.delete().queueAfter(2, TimeUnit.SECONDS));
				}
			} else {
				event.getMessage().delete().queue();
				error.setDescription("Error using Command");
				event.getChannel().sendMessageEmbeds(error.build())
						.queue(message -> message.delete().queueAfter(2, TimeUnit.SECONDS));
			}
		} else {
			event.getMessage().delete().queue();
			error.setDescription("Error using Command");
			event.getChannel().sendMessageEmbeds(error.build())
					.queue(message -> message.delete().queueAfter(2, TimeUnit.SECONDS));
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

	public String toHex(String value) {
		return HexFormat.of().formatHex(value.getBytes());
	}

	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public void saveUrl(final String filename, final String urlString) throws MalformedURLException, IOException {
		BufferedInputStream in = null;
		FileOutputStream fout = null;
		try {
			in = new BufferedInputStream(new URL(urlString).openStream());
			fout = new FileOutputStream(filename);

			final byte data[] = new byte[1024];
			int count;
			while ((count = in.read(data, 0, 1024)) != -1) {
				fout.write(data, 0, count);
			}
		} finally {
			if (in != null) {
				in.close();
			}
			if (fout != null) {
				fout.close();
			}
		}
	}
}