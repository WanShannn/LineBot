package idv.sharon.LineBot.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.container.FlexContainer;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.model.response.IssueLinkTokenResponse;

import idv.sharon.LineBot.dataInterface.LineMessageType;

@Component
public class LineReplyMessageService {

	@Value("${line.bot.channel-token}")
	private String LINE_TOKEN;

	public void replyAPI(String replyToken, Message lineMessage) {
		ReplyMessage replyMessage = new ReplyMessage(replyToken, lineMessage);

		LineMessagingClient client = LineMessagingClient.builder(LINE_TOKEN).build();
		BotApiResponse botApiResponse;

		try {
			botApiResponse = client.replyMessage(replyMessage).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	public void Message(JSONObject event) throws JsonMappingException, JsonProcessingException {

		Message lineMessage = null;

		lineMessage = new LineMessageType().MessageType(event.getJSONObject("message").getString("type"),
				event.getJSONObject("message"));

		replyAPI(event.getString("replyToken"), lineMessage);
	}	

}
