package com.yuki.yukihub.ai;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AiReviewResult {
    public String title = "AI Week Review";
    public String subtitle = "This week's Onii-chan was also manipulated by the game.";
    public String scoreName = "Addiction index";
    public int score = 0;
    public String roast = "";
    public final List<String> highlights = new ArrayList<>();
    public final List<GameComment> topGamesComment = new ArrayList<>();
    public final List<String> advice = new ArrayList<>();
    public String oneLine = "";
    public String rawText = "";
    public boolean parsedJson = true;

    public static class GameComment {
        public String game = "";
        public String comment = "";
    }

    public static AiReviewResult fromContent(String content) {
        AiReviewResult r = new AiReviewResult();
        r.rawText = content == null ? "" : content.trim();
        try {
            JSONObject o = new JSONObject(extractJsonObject(r.rawText));
            r.title = safe(o.optString("title", r.title), r.title);
            r.subtitle = safe(o.optString("subtitle", r.subtitle), r.subtitle);
            r.scoreName = safe(o.optString("scoreName", o.optString("score_name", r.scoreName)), r.scoreName);
            r.score = Math.max(0, Math.min(100, o.optInt("score", 0)));
            r.roast = safe(o.optString("roast", ""), "");
            r.oneLine = safe(o.optString("oneLine", o.optString("one_line", "")), "");
            readStringArray(o.optJSONArray("highlights"), r.highlights, 3);
            readStringArray(o.optJSONArray("advice"), r.advice, 3);
            JSONArray games = o.optJSONArray("topGamesComment");
            if (games == null) games = o.optJSONArray("top_games_comment");
            if (games != null) {
                for (int i = 0; i < games.length() && r.topGamesComment.size() < 5; i++) {
                    JSONObject item = games.optJSONObject(i);
                    if (item == null) continue;
                    GameComment gc = new GameComment();
                    gc.game = safe(item.optString("game", ""), "");
                    gc.comment = safe(item.optString("comment", ""), "");
                    if (!gc.game.isEmpty() || !gc.comment.isEmpty()) r.topGamesComment.add(gc);
                }
            }
            if (r.roast.isEmpty() && !r.rawText.isEmpty()) r.roast = r.rawText;
            r.sanitizeVisualNovelSemantics();
        } catch (Throwable t) {
            r.parsedJson = false;
            r.title = "AI Week Review";
            r.subtitle = "The model returned the free text and showed it to Onii-chan first.";
            r.scoreName = "Critical rating index";
            r.score = 66;
            r.roast = r.rawText;
            r.oneLine = "Next time, you can ask the model to strictly output JSON, and the format of miscellaneous fish also needs to be adjusted.";
            r.sanitizeVisualNovelSemantics();
        }
        return r;
    }

    public JSONObject toJson() throws org.json.JSONException {
        JSONObject o = new JSONObject();
        o.put("title", title);
        o.put("subtitle", subtitle);
        o.put("scoreName", scoreName);
        o.put("score", score);
        o.put("roast", roast);
        JSONArray hs = new JSONArray();
        for (String h : highlights) hs.put(h);
        o.put("highlights", hs);
        JSONArray games = new JSONArray();
        for (GameComment gc : topGamesComment) {
            JSONObject item = new JSONObject();
            item.put("game", gc.game);
            item.put("comment", gc.comment);
            games.put(item);
        }
        o.put("topGamesComment", games);
        JSONArray adv = new JSONArray();
        for (String a : advice) adv.put(a);
        o.put("advice", adv);
        o.put("oneLine", oneLine);
        o.put("rawText", rawText);
        o.put("parsedJson", parsedJson);
        return o;
    }

    public static AiReviewResult fromJson(JSONObject o) {
        AiReviewResult r = new AiReviewResult();
        if (o == null) return r;
        r.title = safe(o.optString("title", r.title), r.title);
        r.subtitle = safe(o.optString("subtitle", r.subtitle), r.subtitle);
        r.scoreName = safe(o.optString("scoreName", r.scoreName), r.scoreName);
        r.score = Math.max(0, Math.min(100, o.optInt("score", r.score)));
        r.roast = safe(o.optString("roast", r.roast), r.roast);
        r.oneLine = safe(o.optString("oneLine", r.oneLine), r.oneLine);
        r.rawText = safe(o.optString("rawText", r.rawText), r.rawText);
        r.parsedJson = o.optBoolean("parsedJson", r.parsedJson);
        readStringArray(o.optJSONArray("highlights"), r.highlights, 3);
        readStringArray(o.optJSONArray("advice"), r.advice, 3);
        JSONArray games = o.optJSONArray("topGamesComment");
        if (games != null) {
            for (int i = 0; i < games.length() && r.topGamesComment.size() < 5; i++) {
                JSONObject item = games.optJSONObject(i);
                if (item == null) continue;
                GameComment gc = new GameComment();
                gc.game = safe(item.optString("game", ""), "");
                gc.comment = safe(item.optString("comment", ""), "");
                if (!gc.game.isEmpty() || !gc.comment.isEmpty()) r.topGamesComment.add(gc);
            }
        }
        r.sanitizeVisualNovelSemantics();
        return r;
    }

    private void sanitizeVisualNovelSemantics() {
        roast = sanitizeText(roast);
        oneLine = sanitizeText(oneLine);
        for (int i = 0; i < highlights.size(); i++) highlights.set(i, sanitizeText(highlights.get(i)));
        for (int i = 0; i < advice.size(); i++) advice.set(i, sanitizeText(advice.get(i)));
        for (GameComment gc : topGamesComment) {
            if (gc != null) gc.comment = sanitizeText(gc.comment);
        }
    }

    private static String sanitizeText(String text) {
        if (text == null) return "";
        String s = text;
        s = s.replace("Playing a completed game repeatedly", "Revisit completed works");
        s = s.replace("Replay a completed game", "Revisit completed works");
        s = s.replace("Complete the game again", "Reminiscing about the works you have played");
        s = s.replace("Restraint has been completed the game", "Compile your thoughts on completed works");
        s = s.replace("Clear game nymphomaniac", "Aftertaste after clearing the level");
        s = s.replace("Waste of time", "Immersed in it for a bit long");
        return s;
    }

    public String toShareText() {
        StringBuilder sb = new StringBuilder();
        sb.append(title).append("\n").append(subtitle).append("\n");
        if (scoreName != null && !scoreName.isEmpty()) sb.append(scoreName).append("：").append(score).append("/100\n");
        if (roast != null && !roast.isEmpty()) sb.append("\n").append(roast).append("\n");
        if (!highlights.isEmpty()) {
            sb.append("Highlights:");
            for (String h : highlights) sb.append("- ").append(h).append("\n");
        }
        if (!advice.isEmpty()) {
            sb.append("Next week’s prescription:");
            for (String a : advice) sb.append("- ").append(a).append("\n");
        }
        if (oneLine != null && !oneLine.isEmpty()) sb.append("\n").append(oneLine);
        return sb.toString().trim();
    }

    private static void readStringArray(JSONArray arr, List<String> out, int max) {
        if (arr == null || out == null) return;
        for (int i = 0; i < arr.length() && out.size() < max; i++) {
            String s = arr.optString(i, "");
            if (s != null && !s.trim().isEmpty()) out.add(s.trim());
        }
    }

    private static String extractJsonObject(String text) {
        if (text == null) return "{}";
        String s = text.trim();
        if (s.startsWith("```")) {
            int first = s.indexOf('{');
            int last = s.lastIndexOf('}');
            if (first >= 0 && last > first) return s.substring(first, last + 1);
        }
        int first = s.indexOf('{');
        int last = s.lastIndexOf('}');
        if (first >= 0 && last > first) return s.substring(first, last + 1);
        return s;
    }

    private static String safe(String value, String fallback) {
        if (value == null) return fallback == null ? "" : fallback;
        String s = value.trim();
        return s.isEmpty() ? (fallback == null ? "" : fallback) : s;
    }
}