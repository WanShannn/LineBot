package idv.sharon.LineBot.dataInterface;

import java.net.URI;

import org.json.JSONObject;

import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.LocationMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.VideoMessage;

public class LineMessageType {

	public LineMessageType() {}

	public Message MessageType(String type, JSONObject event) {

		switch (type) {
		
		case "text":
			String text = (String) event.get("text");

			TextMessage textMessage = new TextMessage(text);
			return textMessage;
		case "sticker":
			String packageId = (String) event.get("packageId");
			String stickerId = (String) event.get("stickerId");

			StickerMessage stickerMessage = new StickerMessage(packageId, stickerId);
			return stickerMessage;
		case "image":
			URI originalContentUrl = URI.create(event.get("originalContentUrl").toString());
			URI previewImageUrl = URI.create(event.get("previewImageUrl").toString());					

			ImageMessage imageMessage = new ImageMessage(originalContentUrl, previewImageUrl);
			return imageMessage;
		case "video":
			URI originalContentUrl1 = URI.create(event.get("originalContentUrl").toString());				
			URI previewImageUrl1 = URI.create(event.get("previewImageUrl").toString());		

			VideoMessage videoMessage = new VideoMessage(originalContentUrl1, previewImageUrl1);
			return videoMessage;
		case "location":
			String title = (String) event.get("title");
			String address = (String) event.get("address");
			Double latitude = Double.valueOf(event.get("latitude").toString()) ;
			Double longitude = Double.valueOf(event.get("longitude").toString());

			LocationMessage locationMessage = new LocationMessage(title, address, latitude, longitude);
			return locationMessage;

		}
		return null;
	}
}
