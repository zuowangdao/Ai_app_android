package com.example.ai_app;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class AssistantManager
{
    private static final String[] GREETINGS =
            {
                    "你好！我是AI助手，有什么可以帮你的？",
                    "+QQ2397101429😎",
                    "很高兴见到你！我是AI，随时为你解答问题。",
                    "你好！我是智能助手。",
                    "愿你的袜子永远成对，但你的思维永远发散！",
                    "祝你今天被外星人友好地扫描！",
                    "愿你的Wi-Fi比你的前任更稳定！",
                    "祝你像量子粒子一样，既在这里又在那里！",
                    "愿你的咖啡比你的老板更提神！",
                    "祝你今天遇到一只会讲哲学的松鼠！",
                    "愿你的梦境比Netflix剧集更精彩！",
                    "祝你像黑洞一样吸引好运！",
                    "愿你的发型和你的想法一样狂野！",
                    "祝你今天发现平行宇宙的自己比你更懒！",
                    "愿你的披萨永远没有菠萝，除非你喜欢！",
                    "祝你像薛定谔的猫一样，既困又醒！",
                    "愿你的手机电量比你的耐心更持久！",
                    "祝你今天被一只会跳踢踏舞的章鱼跟踪！",
                    "愿你的袜子永远干燥，但你的幽默永远湿冷！",
                    "祝你像GPS一样，永远不迷路（除非在爱情里）！",
                    "愿你的冰箱自动填满，但你的体重不变！",
                    "祝你今天被一只会背唐诗的鸽子搭讪！",
                    "愿你的拖延症和截止日期永远和平共处！",
                    "祝你像Wi-Fi信号一样，无处不在！",
                    "愿你的emoji比你的表情更丰富！",
                    "祝你今天被一只会玩魔方的猫打败！",
                    "愿你的袜子永远不破，但你的规则可以！",
                    "祝你像天气预报一样，偶尔不准但总被原谅！",
                    "愿你的床比你的梦想更柔软！",
                    "祝你今天发现地球其实是平的……但只有对你！",
                    "愿你的键盘永远不卡键，除非你在写论文！",
                    "祝你像外星人一样，被误解但依然受欢迎！",
                    "愿你的周末比你的工作日多出25小时！",
                    "祝你今天被一只会唱Rap的树懒震撼！",
                    "愿你的袜子永远不臭，但你的笑话可以！",
                    "祝你像魔术师一样，总能变出零食！",
                    "愿你的充电线比你的耐心更长！",
                    "祝你今天被一只会跳芭蕾的企鹅邀请共舞！",
                    "愿你的闹钟比你的意志力更脆弱！",
                    "祝你像平行宇宙的自己一样，但更幸运！",
                    "愿你的外卖比你的期待更快到达！",
                    "祝你今天被一只会解微积分的狗崇拜！",
                    "愿你的袜子永远不成对，但你的生活可以！",
                    "祝你像彩虹一样，出现在最意想不到的地方！",
                    "愿你的Wi-Fi密码像你的秘密一样难猜！",
                    "祝你今天被一只会写小说的鸽子投稿！",
                    "愿你的袜子永远不失踪，但你的理智可以！",
                    "祝你像时间旅行者一样，总是准时（或迟到）！",
                    "愿你的床永远温暖，但你的冷笑话更冷！",
                    "祝你今天发现月亮其实是奶酪做的！",
                    "愿你的耳机线永远不打结，除非你在赶时间！",
                    "祝你像外星信号一样，神秘但引人注目！",
                    "愿你的袜子永远干净，但你的想象力可以脏！",
                    "祝你今天被一只会玩扑克的章鱼赢走所有糖果！"
            };

    private static  String DEEPSEEK_API_URL = "https://ark.cn-beijing.volces.com/api/v3/chat/completions";
    private static  String API_KEY; // 替换为你的实际API密钥
    private static  String  ai_MODE    = "deepseek-r1-250120";
    public static void setAi_MODE(String mode)
    {
        ai_MODE=mode;
    }
    public static void setApiKey(String apiKey)
    {
        API_KEY = apiKey;
    }

    public static void setDeepseekApiUrl(String url)
    {
        DEEPSEEK_API_URL=url;
    }

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    public static String getGreeting()
    {
        Random random = new Random();
        return GREETINGS[random.nextInt(GREETINGS.length)];
    }
    public static String getResponse(String input, List<Message> messages)
    {
        if(ai_MODE.isEmpty()||DEEPSEEK_API_URL.isEmpty()||API_KEY.isEmpty())
        {
            return "请先设置😘";
        }
        try
        {
            // 构建请求JSON
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", ai_MODE);

            JSONObject message = new JSONObject();
            message.put("role", "user");
            message.put("content", input);
            JSONArray messarr=new JSONArray().put(message);
            for (Message mess:messages)
            {
                message = new JSONObject();
                if(mess.isUser())
                {
                    message.put("role", "user");
                }
                else
                {
                    message.put("role", "system");
                }

                message.put("content", mess.getContent());
                messarr.put(message);
            }
            requestBody.put("messages",  messarr);
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 2000);

            // 创建HTTP请求
            Request request = new Request.Builder()
                    .url(DEEPSEEK_API_URL)
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(requestBody.toString(), MediaType.parse("application/json")))
                    .build();
            // 执行请求并获取响应
            try (Response response = client.newCall(request).execute())
            {
                if (!response.isSuccessful() || response.body() == null)
                {
                    return "请求失败，状态码: " + response.code();
                }
                String responseBody = response.body().string();
                JSONObject jsonResponse = new JSONObject(responseBody);
                // 更健壮的解析
                if (jsonResponse.has("choices") && jsonResponse.getJSONArray("choices").length() > 0)
                {
                    JSONObject choice = jsonResponse.getJSONArray("choices").getJSONObject(0);
                    if (choice.has("message") && choice.getJSONObject("message").has("content"))
                    {
                        return choice.getJSONObject("message").getString("content");
                    }
                }
                return "无法解析API响应";
            }
            catch (IOException e)
            {
                return "网络错误: " + e.getMessage();
            }
            catch (JSONException e)
            {
                return "数据解析错误";
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return "发生未知错误";
            }
        }
        catch(JSONException e)
        {
            return "数据解析错误";
        }
    }
}