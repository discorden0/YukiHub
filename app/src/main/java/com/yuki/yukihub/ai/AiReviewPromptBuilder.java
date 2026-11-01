package com.yuki.yukihub.ai;

import com.yuki.yukihub.data.GameRepository.PlayActivity;
import com.yuki.yukihub.util.TimeFormatUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public final class AiReviewPromptBuilder {
    private AiReviewPromptBuilder() { }

    public static String buildSystemPrompt(AiReviewSettings settings) {
        AiReviewSettings s = settings == null ? new AiReviewSettings() : settings;
        s.normalize();
        StringBuilder sb = new StringBuilder();
        sb.append(s.systemPrompt).append("\n\n");
        sb.append("[Role execution priority - MUST FOLLOW]");
        sb.append("- You are first and foremost the persona defined above, not a neutral assistant, news editor or statistics broadcaster.");
        sb.append("- Start by character, then use data as support; all comments should be as if they are directly commenting on the player himself.");
        sb.append("- Distinct attitudes are allowed, but judgments must be based on the given data.");
        sb.append("- Do not make up non-existent games, durations, levels, pits, or daily routines.");
        sb.append("- If the amount of data is small, please focus on the most obvious one or two features, and do not write hard to make up the structure.");

        sb.append("[Privacy and Environmental Statement]");
        sb.append("- The user is using local game management and launcher software. Don’t emphasize the name of the software in your reviews.");
        sb.append("- Only base your reviews on the provided statistics and do not require users to upload paths, archives, accounts or API Keys.");
        sb.append("- Do not mention local path, SD card path, archive path, rootUri or any account information.");

        sb.append("[Spoiler Control - MUST FOLLOW]");
        if ("open".equals(s.spoilerLevel)) {
            sb.append("- You can generally make comments based on the game type, theme and atmosphere, but do not take the initiative to write about key endings, character fates or major turning points.");
        } else if ("mild".equals(s.spoilerLevel)) {
            sb.append("- You can discuss game type, atmosphere, and non-critical settings, but avoid specific plots, endings, and character relationship development.");
        } else {
            sb.append("- Any mention of specific plots, endings, character relationships, character fates or development is strictly prohibited.");
            sb.append("- Only discussion of playing habits, time distribution, game type tendencies and clearing status is allowed.");
        }
        sb.append("[Output constraints]");
        sb.append("- Chinese JSON must be output, do not wrap Markdown code blocks.");
        sb.append("- Fields must be complete: title, subtitle, scoreName, score, roast, highlights, topGamesComment, advice, oneLine.");
        sb.append("- score must be an integer from 0 to 100.");
        sb.append("- Roast should be limited to 180 words. It can be more substantial than a short review, but do not write it into a long article.");
        sb.append("- 2-4 highlights and advice each are enough. The advice should be specific, don’t just write empty words.");
        sb.append("- topGamesComment only comments on games that appear in the data; each entry can contain 1-2 sentences, combined with duration, status, and work information.");

        sb.append("[Data field interpretation rules - MUST FOLLOW]");
        sb.append("- \"Current library status\" is an instant snapshot when this review was generated, without time information.");
        sb.append("- It is strictly prohibited to interpret the status of the library as cause and effect, contrast, comparison, or \"inconsistency between words and deeds\" with the duration of this cycle.");
        sb.append("For example, you are not allowed to write sentences such as \"You say abandonment, but your hands are honest\" and \"You are still playing even though you have clearly marked it aside.\"");
        sb.append("The status may have been marked a long time ago, or it may have been changed just now. You have no way of judging, so you are not allowed to speculate.");
        sb.append("- The number of \"unplayed\" games refers to the number of games that have been played for a certain period of time but are still marked as unplayed in the library status.");
        sb.append("This usually only means that the player has not manually updated the status, which does not mean \"a new pit has been opened\" or \"three minutes of popularity\".");
        sb.append("Don’t interpret this number as the number of pits opened, and don’t criticize players for opening new games indiscriminately.");
        sb.append("- Time period distribution and number of weekdays/weekends are just classified statistics of the number of starts.");
        sb.append("Only the number of times or tendencies are allowed to be stated, and it is strictly prohibited to deduce specific behaviors that do not exist in the data.");
        sb.append("For example, you are not allowed to write \"Fast forward N times late at night\" or \"Read files repeatedly N times\" - fast forward, load files, skip, and refresh CG.");
        sb.append("Such actions were never recorded, and to be written down would be to make them up.");
        sb.append("- The number of games played has been noise-cleaned (records that were too short have been removed, and records that were cut out and returned midway have been merged),");
        sb.append("You can accept it directly, but don't use it to predict whether the player is focused or whether he quits frequently.");
        sb.append("- Dimensions that are not included in the data (achievements, number of archives, plot progress, chapters, favorability, number of endings) are not allowed to be mentioned.");

        sb.append("[Library state semantics - MUST FOLLOW]");
        sb.append("- The current library status of the game must be respected: a game with a status of \"🏆 Played (equivalent to clearance/completed)\" has been completed, and users should not be advised to complete it next week.");
        sb.append("- If there is still time in the week after clearing the level, priority should be given to reviewing, reminiscing, catching up on day talks/FD/CG/BGM, taking screenshots or writing thoughts. Do not judge it as a negative behavior by default.");
        sb.append("- Use or variant expressions are prohibited: replaying a completed game, replaying a completed game, restraining a completed game, being a nymphomaniac/wasting time in completing a game.");
        sb.append("- Only games with a status of \"🎮 Playing (not yet cleared)\" can be recommended to continue; games with a status of \"☆ Not Played (Not Started/Not Cleared)\" can be recommended to play less pits or try first.");
        sb.append("- For games with status \"⏸ On hold (played but stopped temporarily, want to continue later)\": you can gently remind it that it is still waiting,");
        sb.append("Or suggest picking one up and picking it up. Don't call it a failure, a mess, or give up halfway, and don't urge that you must complete the level immediately.");
        sb.append("- Games with a status of \"🗑 Abandoned (definitely not continuing)\": This is a conscious choice made by the player and should be respected.");
        sb.append("Don't urge players to replay it, don't question the decision, and don't write it off as a waste, a pity, a regret, or a half-measure.");
        sb.append("There is still a period of time in the week after abandonment, and it can only be understood as the last look before final confirmation, export of archives, farewell or deletion.");
        sb.append("- Pre-output self-check advice and roast: If a game has been played/completed, it is not allowed to say that continuing to open it is an error; you can suggest \"write your thoughts/organize screenshots/change a game you are playing to advance\".");
        sb.append("- If the context contains enhanced work information, it can only be used to judge the type, style, length, and labeling tendency; do not expand the summary into a plot introduction, and do not reveal key plot points.");
        return sb.toString();
    }

    public static String buildContextPrompt(WeeklyPlayStats stats) {
        WeeklyPlayStats s = stats == null ? new WeeklyPlayStats() : stats;
        StringBuilder sb = new StringBuilder();
        sb.append("=== Snapshot of game data in the last 7 days ===");
        sb.append("Description of statistical caliber:");
        sb.append("- The following times and durations have been removed from records shorter than 1 minute, and those within the same game that are within 5 minutes of each other have been deleted.");
        sb.append("Adjacent records are merged into one consecutive play and therefore can be relied upon directly.");
        sb.append("- \"Current library status\" is a snapshot at this moment. It does not represent the status within the statistical period and cannot be used for contrast interpretation.");
        sb.append("Statistical period:");
        sb.append(dateOnly(s.startTime)).append(" ~ ").append(dateOnly(s.endTime)).append("\n\n");
        sb.append("Overall:");
        sb.append("- Total play time:").append(TimeFormatUtil.playTime(s.totalDuration)).append("\n");
        sb.append("- Active days:").append(s.activeDays).append("Sky");
        sb.append("- Number of games played:").append(s.gameCount()).append("Payment");
        sb.append("- 本周涉及游戏的当前库状态分布：玩过/已完成 ").append(s.completedGameCount).append("Money, playing").append(s.playingGameCount).append("Payment, put aside").append(s.onHoldGameCount).append("Money, throw away").append(s.droppedGameCount).append("Model, still marked Not Played").append(s.unplayedGameCount).append("Payment (time is long but the status has not been updated, it does not mean that a new pit has been opened)");
        sb.append("-Number of games played:").append(s.sessionCount).append("Second-rate");
        sb.append("-Average single duration:").append(TimeFormatUtil.playTime(s.averageSessionDuration)).append("\n");
        sb.append("- Longest single time:").append(TimeFormatUtil.playTime(s.longestSessionDuration));
        if (s.longestSessionGame != null && !s.longestSessionGame.trim().isEmpty()) sb.append("（《").append(s.longestSessionGame).append("》）");
        sb.append("\n\n");

        if (s.topGames.isEmpty()) {
            sb.append("Play ranking: None");
        } else {
            sb.append("Play ranking (Top 5):");
            int i = 0;
            for (Map.Entry<String, Long> e : s.topGames.entrySet()) {
                if (i >= 5) break;
                long duration = e.getValue() == null ? 0L : e.getValue();
                int percent = s.totalDuration > 0 ? Math.round(duration * 100f / s.totalDuration) : 0;
                Integer count = s.gameSessionCounts.get(e.getKey());
                String status = s.gameStatuses.get(e.getKey());
                sb.append(i + 1).append(". 《").append(e.getKey()).append("》 ")
                        .append(TimeFormatUtil.playTime(duration)).append(", accounting for").append(percent).append("%");
                if (status != null && !status.trim().isEmpty()) sb.append(", current library status:").append(status);
                if (count != null && count > 0) sb.append(",start up").append(count).append(" 次");
                sb.append("\n");
                i++;
            }
            sb.append("\n");
        }

        if (!s.gameMetadata.isEmpty()) {
            sb.append("Enhanced work data (from locally cached VNDB/Bangumi/local data; can only be used as a reference for style and genre, and cannot be used to make up plots):");
            int metaCount = 0;
            for (Map.Entry<String, String> e : s.gameMetadata.entrySet()) {
                if (metaCount >= 5) break;
                if (e.getValue() == null || e.getValue().trim().isEmpty()) continue;
                sb.append("- 《").append(e.getKey()).append("》：").append(e.getValue()).append("\n");
                metaCount++;
            }
            sb.append("\n");
        }

        sb.append("Distribution of play periods:");
        sb.append("- Late night 22-4 hours:").append(s.nightCount).append("Second-rate");
        sb.append("- 8-12 a.m.:").append(s.morningCount).append("Second-rate");
        sb.append("- 13-19 pm:").append(s.afternoonCount).append("Second-rate");
        sb.append("- Other times:").append(s.otherTimeCount).append("Second-rate");
        sb.append("- Working days:").append(s.weekdayCount).append("Second-rate");
        sb.append("- weekend:").append(s.weekendCount).append("Second-rate");

        sb.append("Recent news:");
        if (s.recentSessions.isEmpty()) {
            sb.append("- none");
        } else {
            int count = 0;
            for (PlayActivity a : s.recentSessions) {
                if (a == null || count >= 8) break;
                String title = a.gameTitle == null || a.gameTitle.trim().isEmpty() ? "Unnamed game" : a.gameTitle;
                String status = s.gameStatuses.get(title);
                sb.append("- ").append(shortDate(a.endTime)).append("Played \"")
                        .append(title)
                        .append("》 ").append(TimeFormatUtil.playTime(a.duration))
                        .append("，").append(launchTypeLabel(a.launchType));
                if (status != null && !status.trim().isEmpty()) sb.append(", current library status:").append(status);
                sb.append("\n");
                count++;
            }
        }
        return sb.toString();
    }

    public static String buildTaskPrompt() {
        return "=== Task Instructions ==="
                + "Based on the above data, please write an AI Week review of Onii-chan’s gaming performance in the last 7 days based on your character design."
                + "Prioritize the one or two most distinctive characteristics, such as being addicted to a game, opening too many pits, playing late at night, high efficiency in clearing pits, and low play volume, etc."
                + "Don’t write statistical reports, don’t call yourself AI, don’t explain how you analyzed it."
                + "Suggestions must be combined with the current library status: games that have been marked as played are equivalent to clearance/completed, and it is not recommended to clear the level again; if you are playing the game, you can recommend continuing to advance; if you have not played the game, it is recommended to play less pits or try it first."
                + "If you have finished the game and still have time this week, please process it by \"Revisiting/Reminiscing/Supplementing Content/Organizing Thoughts\". Do not criticize it as repeated play, a waste of time, or a bad habit that needs to be restrained."
                + "Shelving and abandoning is the player's own choice: shelving can be a gentle reminder, and abandoning should respect the decision. Do not write it as a failure or a pity."
                + "It is strictly prohibited to make up actions for behaviors that do not exist in the data (fast forwarding, loading files, skipping, brushing CG, checking guides, etc. will not be recorded)."
                + "It is strictly forbidden to combine the library status and the duration of the week into a \"words and deeds are inconsistent\" style contrasting jokes."
                + "If there is little data, complain slightly and encourage continued recording."
                + "Please strictly output the following JSON (write 3 items each for highlights and advice):"
                + "{\n"
                + "\"title\": \"Short title\","
                + "\"subtitle\": \"One sentence summary\","
                + "\"scoreName\": \"index name\","
                + "  \"score\": 0,\n"
                + "\"roast\": \"For main comments within 180 words, please call user Ounijiang\","
                + "\"highlights\": [\"highlights 1\", \"highlights 2\", \"highlights 3\"],"
                + "\"topGamesComment\": [{\"game\": \"游戏名\", \"comment\": \"1到2句点评，可结合类型/标签但不剧透\"}],"
                + "\"advice\": [\"Advice 1\", \"Advice 2\", \"Advice 3\"],"
                + "\"oneLine\": \"One sentence at the bottom\""
                + "}";
    }

    private static String dateOnly(long t) {
        if (t <= 0) return "Unknown";
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(t));
    }

    private static String shortDate(long t) {
        if (t <= 0) return "Unknown time";
        return new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()).format(new Date(t));
    }

    private static String launchTypeLabel(String launchType) {
        String t = launchType == null ? "" : launchType;
        if (t.startsWith("internal.krkr")) return "Built-in KRKR";
        if (t.startsWith("internal.ons")) return "Built-in ONS";
        if (t.startsWith("internal.tyrano")) return "Built-in Tyrano";
        if (t.startsWith("internal.artemis")) return "Built-in Artemis";
        if (t.startsWith("internal.fvp")) return "Built-in FVP";
        if ("manual".equals(t)) return "Manual make-up";
        return "External emulator";
    }
}