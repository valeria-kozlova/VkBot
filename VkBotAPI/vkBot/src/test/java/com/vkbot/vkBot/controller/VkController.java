package com.vkbot.vkBot.controller;

import com.vkbot.vkBot.service.VkApiService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class VkController
{
    @Autowired
    private VkApiService vkApiService;
    @PostMapping("/callback")
    public String handleCallback(@RequestBody String callbackPayload)
    {
        JSONObject json = null;
        try
        {
            json = new JSONObject(callbackPayload);
        }
        catch (JSONException e)
        {
            throw new RuntimeException(e);
        }
        String type = null;
        try
        {
            type = json.getString("type");
        }
        catch (JSONException e)
        {
            throw new RuntimeException(e);
        }

        // Если тип запроса - новое сообщение
        if ("message_new".equals(type))
        {
            JSONObject message = null;
            try
            {
                message = json.getJSONObject("object").getJSONObject("message");
            }
            catch (JSONException e)
            {
                throw new RuntimeException(e);
            }
            String userId = null;
            try
            {
                userId = message.getString("from_id");
            }
            catch (JSONException e)
            {
                throw new RuntimeException(e);
            }
            String text = null;
            try
            {
                text = message.getString("text");
            }
            catch (JSONException e)
            {
                throw new RuntimeException(e);
            }

            // Создаем цитату
            String quotedMessage = "> " + text; // Добавляем символы цитирования перед текстом сообщения

            // Отправляем цитату обратно пользователю
            vkApiService.sendMessage(userId, quotedMessage);
        }

        // Вернуть "ok" для подтверждения получения запроса от VK
        return "ok";
    }
}
