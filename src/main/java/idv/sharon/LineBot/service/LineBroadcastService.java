package idv.sharon.LineBot.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.Broadcast;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.response.BotApiResponse;

@Component
public class LineBroadcastService {

	@Value("${line.bot.channel-token}")
	private String LINE_TOKEN;

	public boolean broadcastAPI(JSONObject event) {

		List<Message> MessageArray = new ArrayList<Message>();

		for (int i = 0; i < event.getJSONArray("data").length(); i++) {
			if (event.getJSONArray("data").getJSONObject(i).getString("type").equals("message")) {

				ObjectMapper mapper = new ObjectMapper();
				try {
					MessageArray.add((Message) mapper.readValue(
							event.getJSONArray("data").getJSONObject(i).getJSONObject("message").toString(),
							Message.class));
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

		Broadcast broadcast = new Broadcast(MessageArray);

		LineMessagingClient client = LineMessagingClient.builder(LINE_TOKEN).build();
		BotApiResponse botApiResponse;

		try {
			botApiResponse = client.broadcast(broadcast).get();
			return true;
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return false;
		}
	}
}
