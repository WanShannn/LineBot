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
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.response.BotApiResponse;

@Component
public class LinePushMessageService {

	@Value("${line.bot.channel-token}")
	private String LINE_TOKEN;

	public boolean pushMessageAPI(JSONObject event) {

		List<Message> MessageArray = new ArrayList<Message>();
		String sendTarget = "";

		for (int i = 0; i < event.getJSONArray("data").length(); i++) {
			ObjectMapper mapper = new ObjectMapper();

			if (event.getJSONArray("data").getJSONObject(i).getString("type").equals("message")) {
				try {
					MessageArray.add((Message) mapper.readValue(
							event.getJSONArray("data").getJSONObject(i).getJSONObject("message").toString(),
							Message.class));
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			if (event.getJSONArray("data").getJSONObject(i).getJSONObject("source").getString("type").equals("user")) {
				sendTarget = event.getJSONArray("data").getJSONObject(i).getJSONObject("source").getString("userId");
			}
			if (event.getJSONArray("data").getJSONObject(i).getJSONObject("source").getString("type").equals("group")) {
				sendTarget = event.getJSONArray("data").getJSONObject(i).getJSONObject("source").getString("groupId");
			}
		}

		PushMessage pushMessage = new PushMessage(sendTarget, MessageArray);

		LineMessagingClient client = LineMessagingClient.builder(LINE_TOKEN).build();
		BotApiResponse botApiResponse;

		try {
			botApiResponse = client.pushMessage(pushMessage).get();
			return true;
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return false;
		}
	}
}
