package com.example.backup.service.impl.pk;

import com.example.backup.consumer.WebSocketServer;
import com.example.backup.service.pk.StartGameService;
import org.springframework.stereotype.Service;

/**
 * @author zzx
 * @date 2023/8/24 0:15
 */
@Service
public class StartGameServiceImpl implements StartGameService {
    @Override
    public String startGame(Integer aId,Integer aBotId, Integer bId,Integer bBotId) {
        System.out.println("start game:" + aId + " " + bId);
        WebSocketServer.startGame(aId,aBotId,bId,bBotId);
        return "start game success";
    }
}
