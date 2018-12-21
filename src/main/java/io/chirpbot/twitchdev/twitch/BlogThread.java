package io.chirpbot.twitchdev.twitch;

import io.chirpbot.twitchdev.TwitchDev;
import io.chirpbot.twitchdev.helpers.MessageUtils;
import io.chirpbot.twitchdev.helpers.rss.Feed;
import io.chirpbot.twitchdev.helpers.rss.FeedMessage;
import io.chirpbot.twitchdev.helpers.rss.RSSParser;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IEmbed;

import java.awt.*;
import java.time.Instant;
import java.util.List;

public class BlogThread extends Thread {
	private RSSParser parser;
	private FeedMessage latestFeed;

	public BlogThread() {
		parser = new RSSParser("https://blog.twitch.tv/feed");
		latestFeed = null;
	}

	@Override
	public void run() {
		while(true) {
			Feed feed = parser.readFeed();
			if(latestFeed != null && latestFeed != feed.getEntries().get(0)) {
				latestFeed = feed.getEntries().get(0);
				updateAnnouncements();
			} else if(latestFeed == null){
				latestFeed = feed.getEntries().get(0);
				updateAnnouncements();
			}
			try {
				sleep(3600000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void updateAnnouncements() {
		//523673395221495808L - #announcements
		//524833530656587777L - #bot-test
		MessageUtils.sendMessage(TwitchDev.getBot().getGuilds().get(0), TwitchDev.getBot().getChannelByID(524833530656587777L), "New Blog Post:");
		MessageUtils.sendEmbed(TwitchDev.getBot().getGuilds().get(0), TwitchDev.getBot().getChannelByID(524833530656587777L), new EmbedObject(new IEmbed() {
			@Override
			public String getTitle() {
				return latestFeed.getTitle();
			}

			@Override
			public String getType() {
				return null;
			}

			@Override
			public String getDescription() {
				return latestFeed.getDescription();
			}

			@Override
			public String getUrl() {
				return latestFeed.getLink();
			}

			@Override
			public Instant getTimestamp() {
				return Instant.now();
			}

			@Override
			public Color getColor() {
				return null;
			}

			@Override
			public IEmbedFooter getFooter() {
				return null;
			}

			@Override
			public IEmbedImage getImage() {
				return null;
			}

			@Override
			public IEmbedImage getThumbnail() {
				return null;
			}

			@Override
			public IEmbedVideo getVideo() {
				return null;
			}

			@Override
			public IEmbedProvider getEmbedProvider() {
				return null;
			}

			@Override
			public IEmbedAuthor getAuthor() {
				return new IEmbedAuthor() {
					@Override
					public String getName() {
						return latestFeed.getAuthor();
					}

					@Override
					public String getUrl() {
						return null;
					}

					@Override
					public String getIconUrl() {
						return null;
					}
				};
			}

			@Override
			public List<IEmbedField> getEmbedFields() {
				return null;
			}
		}));
	}
}
