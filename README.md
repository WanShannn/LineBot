# LineBot-Java
使用JAVA建立下列 LineBot Messaging API 功能：
* Reply Messages
* Broadcast Messages
* Push Messages

## **前置作業**
至Line developers建立Channel並取得Channel secret及Channel access token。

> application.properties
   ```
    line.bot.channel-secret = 'Your Channel secret'
    line.bot.channel-token = 'Your Channel access token'
   ```
   
## **RequestBody結構**
當用戶發訊息至LineBot後，後端收到的RequestBody結構為：

```json
{
  "destination": "ID",
  "events": [
    {
      "type": "message",
      "message": {
        "type": "text",
        "id": "id",
        "text": "textmessage"
      },
      "timestamp": 1111111111111,
      "source": {
        "type": "user",
        "userId": "userId"
      },
      "replyToken": "replyToken",
      "mode": "active"
    }
  ]
}
```

* events
  > type `LineBot收到訊息的型態`  
  > message `訊息類型(Text、Sticker、Image、Video、Location)及內容`   
  > source `發送訊息的用戶類型(userId、groupId、roomId)及ID`   
  > replyToken `LineBot要Response訊息時用的token`


## **Modules**
* line-bot-api-client
* line-bot-model
* line-bot-servlet
* line-bot-spring-boot

## **Result**
* ReplyMessage
   > ![image](https://github.com/WanShannn/LineBot/blob/main/result/ReplyMessage-1.png)
   > ![image](https://github.com/WanShannn/LineBot/blob/main/result/ReplyMessage-2.png)
* Broadcast及PushMessage
   > ![image](https://github.com/WanShannn/LineBot/blob/main/result/Broadcast%E5%8F%8APushMessage-1.png)
   > ![image](https://github.com/WanShannn/LineBot/blob/main/result/Broadcast%E5%8F%8APushMessage-2.png)

## **References**
* https://www.learncodewithmike.com/2020/06/python-line-bot.html
* https://developers.line.biz/en/docs/messaging-api/
* https://github.com/line/line-bot-sdk-java
