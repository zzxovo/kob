package org.example.botrunningsystem.service.impl.util;

import org.example.botrunningsystem.util.BotInterface;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

/**
 * @author zzx
 * @date 2023/8/25 2:38
 */
@Component
public class Consumer extends Thread{
    private Bot bot;
    private static RestTemplate restTemplate;
    private final static String receiveBotMoveUrl = "http://127.0.0.1:3000/pk/receive/bot/move/";
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        Consumer.restTemplate = restTemplate;
    }
    public void startTimeout(long timeout,Bot bot)  {
        this.bot = bot;
        this.start();
        try {
            this.join(timeout); //最多等待timeout秒，不管run函数如何，在timeout秒之后就不会执行run函数的内容
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            this.interrupt();
        }

    }

    private String addUid(String code,String uid){ //在code中的Bot类名后添加uid
        int k = code.indexOf(" implements org.example.botrunningsystem.util.BotInterface");
        return code.substring(0, k) + uid + code.substring(k);
    }

    @Override
    public void run() {
        UUID uuid = UUID.randomUUID();
        String uid = uuid.toString().substring(0,8);

        BotInterface botInterface = Reflect.compile(
                "org.example.botrunningsystem.util.Bot" + uid,
                addUid(bot.getBotCode(), uid)
        ).create().get();

        Integer direction = botInterface.nextMove(bot.getInput());

        System.out.println("move-direction:" + bot.getUserId() + " " + direction);
        MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
        data.add("user_id",bot.getUserId().toString());
        data.add("direction",direction.toString());
        restTemplate.postForObject(receiveBotMoveUrl,data, String.class);

    }
}
