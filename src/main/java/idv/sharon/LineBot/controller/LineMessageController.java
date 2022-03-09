package idv.sharon.LineBot.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import idv.sharon.LineBot.service.LineBroadcastService;
import idv.sharon.LineBot.service.LinePushMessageService;
import idv.sharon.LineBot.service.LineReplyMessageService;

@RestController
public class LineMessageController {
	@Value("${line.bot.channel-secret}")
	private String LINE_SECRET;

	@Autowired
	private LineReplyMessageService lineReplyMessageService;

	@PostMapping("/messaging")
	public ResponseEntity<String> messagingAPI(@RequestHeader("X-Line-Signature") String X_Line_Signature,
			@RequestBody String requestBody) throws UnsupportedEncodingException, IOException {
		System.out.println("============messaging============");

		if (checkFromLine(requestBody, X_Line_Signature)) {
			System.out.println("驗證通過" + requestBody);

			JSONObject object = new JSONObject(requestBody);
			for (int i = 0; i < object.getJSONArray("events").length(); i++) {
				if (object.getJSONArray("events").getJSONObject(i).getString("type").equals("message")) {
					lineReplyMessageService.Message(object.getJSONArray("events").getJSONObject(i));
				}
			}

			return new ResponseEntity<String>("OK", HttpStatus.OK);
		}

		System.out.println("驗證不通過");
		return new ResponseEntity<String>("Not line platform", HttpStatus.BAD_GATEWAY);
	}

	@Autowired
	private LineBroadcastService lineBroadcastService;

	@PostMapping("/broadcast")
	public ResponseEntity<String> broadcastAPI(@RequestBody String requestBody)
			throws UnsupportedEncodingException, IOException {
		System.out.println("============broadcast============");
		System.out.println(requestBody);

		JSONObject requestBodyobject = new JSONObject(requestBody);
		if (lineBroadcastService.broadcastAPI(requestBodyobject)) {
			return new ResponseEntity<String>("OK", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Not line platform", HttpStatus.BAD_GATEWAY);
		}
	}

	@Autowired
	private LinePushMessageService linePushMessageService;

	@PostMapping("/pushmessage")
	public ResponseEntity<String> pushmessageAPI(@RequestBody String requestBody)
			throws UnsupportedEncodingException, IOException {
		System.out.println("============push message============");
		System.out.println(requestBody);

		JSONObject requestBodyobject = new JSONObject(requestBody);
		if (linePushMessageService.pushMessageAPI(requestBodyobject)) {
			return new ResponseEntity<String>("OK", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Not line platform", HttpStatus.BAD_GATEWAY);
		}

	}

//	驗證訊息來源是否為line送出
	public boolean checkFromLine(String requestBody, String X_Line_Signature) {
		try {
			SecretKeySpec key = new SecretKeySpec(LINE_SECRET.getBytes(), "HmacSHA256");
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(key);
			byte[] source = requestBody.getBytes("UTF-8");
			String signature = Base64.encodeBase64String(mac.doFinal(source));

			if (signature.equals(X_Line_Signature)) {
				return true;
			}
		} catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return false;
	}
}
