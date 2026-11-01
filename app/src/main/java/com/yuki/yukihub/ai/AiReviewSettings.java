package com.yuki.yukihub.ai;

import android.content.Context;
import android.content.SharedPreferences;

public class AiReviewSettings {
    public static final String PREFS_NAME = "yukihub_prefs";
    public static final String KEY_PROVIDER = "ai_review_provider";
    public static final String KEY_BASE_URL = "ai_review_base_url";
    public static final String KEY_API_KEY = "ai_review_api_key";
    public static final String KEY_MODEL = "ai_review_model";
    public static final String KEY_TEMPERATURE = "ai_review_temperature";
    public static final String KEY_SYSTEM_PROMPT = "ai_review_system_prompt";
    public static final String KEY_SPOILER_LEVEL = "ai_review_spoiler_level";
    public static final String KEY_PERSONA_PRESET = "ai_review_persona_preset";
    public static final String KEY_FULL_ENDPOINT_URL = "ai_review_full_endpoint_url";
    public static final String KEY_METADATA_ENHANCE = "ai_review_metadata_enhance";
    public static final String KEY_METADATA_ONLINE_LOOKUP = "ai_review_metadata_online_lookup";

    public static final String PROVIDER_DEEPSEEK = "deepseek";
    public static final String PROVIDER_OPENAI = "openai";
    public static final String PROVIDER_CUSTOM = "custom";

    public static final String PERSONA_KOAKUMA = "koakuma";
    public static final String PERSONA_GENTLE = "gentle";
    public static final String PERSONA_CRITIC = "critic";
    public static final String PERSONA_CUSTOM = "custom";

    public String provider = PROVIDER_DEEPSEEK;
    public String baseUrl = "https://api.deepseek.com/v1";
    public String apiKey = "";
    public String model = "deepseek-chat";
    public float temperature = 0.85f;
    public String personaPreset = PERSONA_KOAKUMA;
    public String systemPrompt = defaultSystemPrompt();
    public String spoilerLevel = "strict";
    public boolean fullEndpointUrl = false;
    public boolean metadataEnhance = true;
    public boolean metadataOnlineLookup = false;

    public static AiReviewSettings load(Context context) {
        AiReviewSettings s = new AiReviewSettings();
        if (context == null) return s;
        SharedPreferences sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        s.provider = sp.getString(KEY_PROVIDER, PROVIDER_DEEPSEEK);
        s.baseUrl = sp.getString(KEY_BASE_URL, defaultBaseUrl(s.provider));
        s.apiKey = sp.getString(KEY_API_KEY, "");
        s.model = sp.getString(KEY_MODEL, defaultModel(s.provider));
        s.temperature = sp.getFloat(KEY_TEMPERATURE, 0.85f);
        s.personaPreset = sp.getString(KEY_PERSONA_PRESET, PERSONA_KOAKUMA);
        s.systemPrompt = sp.getString(KEY_SYSTEM_PROMPT, promptForPersona(s.personaPreset));
        s.spoilerLevel = sp.getString(KEY_SPOILER_LEVEL, "strict");
        s.fullEndpointUrl = sp.getBoolean(KEY_FULL_ENDPOINT_URL, false);
        s.metadataEnhance = sp.getBoolean(KEY_METADATA_ENHANCE, true);
        s.metadataOnlineLookup = sp.getBoolean(KEY_METADATA_ONLINE_LOOKUP, false);
        s.normalize();
        return s;
    }

    public void save(Context context) {
        if (context == null) return;
        normalize();
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
                .putString(KEY_PROVIDER, provider)
                .putString(KEY_BASE_URL, baseUrl)
                .putString(KEY_API_KEY, apiKey == null ? "" : apiKey.trim())
                .putString(KEY_MODEL, model)
                .putFloat(KEY_TEMPERATURE, temperature)
                .putString(KEY_PERSONA_PRESET, personaPreset)
                .putString(KEY_SYSTEM_PROMPT, systemPrompt)
                .putString(KEY_SPOILER_LEVEL, spoilerLevel)
                .putBoolean(KEY_FULL_ENDPOINT_URL, fullEndpointUrl)
                .putBoolean(KEY_METADATA_ENHANCE, metadataEnhance)
                .putBoolean(KEY_METADATA_ONLINE_LOOKUP, metadataOnlineLookup)
                .apply();
    }

    public void applyProviderDefaults(String selectedProvider) {
        provider = selectedProvider == null ? PROVIDER_DEEPSEEK : selectedProvider;
        baseUrl = defaultBaseUrl(provider);
        model = defaultModel(provider);
        normalize();
    }

    public void normalize() {
        if (provider == null || provider.trim().isEmpty()) provider = PROVIDER_DEEPSEEK;
        provider = provider.trim().toLowerCase(java.util.Locale.ROOT);
        if (!PROVIDER_DEEPSEEK.equals(provider) && !PROVIDER_OPENAI.equals(provider) && !PROVIDER_CUSTOM.equals(provider)) provider = PROVIDER_CUSTOM;
        if (baseUrl == null || baseUrl.trim().isEmpty()) baseUrl = defaultBaseUrl(provider);
        baseUrl = trimTrailingSlash(baseUrl.trim());
        if (model == null || model.trim().isEmpty()) model = defaultModel(provider);
        model = model.trim();
        if (apiKey == null) apiKey = "";
        apiKey = apiKey.trim();
        if (temperature < 0f) temperature = 0f;
        if (temperature > 2f) temperature = 2f;
        if (personaPreset == null || personaPreset.trim().isEmpty()) personaPreset = PERSONA_KOAKUMA;
        personaPreset = personaPreset.trim().toLowerCase(java.util.Locale.ROOT);
        if (!PERSONA_KOAKUMA.equals(personaPreset) && !PERSONA_GENTLE.equals(personaPreset) && !PERSONA_CRITIC.equals(personaPreset) && !PERSONA_CUSTOM.equals(personaPreset)) personaPreset = PERSONA_CUSTOM;
        if (systemPrompt == null || systemPrompt.trim().isEmpty()) systemPrompt = promptForPersona(personaPreset);
        if (spoilerLevel == null || spoilerLevel.trim().isEmpty()) spoilerLevel = "strict";
        spoilerLevel = spoilerLevel.trim().toLowerCase(java.util.Locale.ROOT);
        if (!"strict".equals(spoilerLevel) && !"mild".equals(spoilerLevel) && !"open".equals(spoilerLevel)) spoilerLevel = "strict";
    }

    public String endpointUrl() {
        normalize();
        String url = fullEndpointUrl ? baseUrl.trim() : trimTrailingSlash(baseUrl);
        if (fullEndpointUrl) return url;
        String lower = url.toLowerCase(java.util.Locale.ROOT);
        if (lower.contains("/chat/completions") || lower.contains("?")) return url;
        return url + "/chat/completions";
    }

    public static String defaultBaseUrl(String provider) {
        String p = provider == null ? "" : provider.trim().toLowerCase(java.util.Locale.ROOT);
        if (PROVIDER_OPENAI.equals(p)) return "https://api.openai.com/v1";
        if (PROVIDER_CUSTOM.equals(p)) return "https://api.openai.com/v1";
        return "https://api.deepseek.com/v1";
    }

    public static String defaultModel(String provider) {
        String p = provider == null ? "" : provider.trim().toLowerCase(java.util.Locale.ROOT);
        if (PROVIDER_OPENAI.equals(p)) return "gpt-4o-mini";
        if (PROVIDER_CUSTOM.equals(p)) return "gpt-4o-mini";
        return "deepseek-chat";
    }

    public static String providerLabel(String provider) {
        String p = provider == null ? "" : provider.trim().toLowerCase(java.util.Locale.ROOT);
        if (PROVIDER_OPENAI.equals(p)) return "OpenAI";
        if (PROVIDER_CUSTOM.equals(p)) return "Customize";
        return "DeepSeek";
    }

    public static String spoilerLabel(String level) {
        String s = level == null ? "" : level.trim().toLowerCase(java.util.Locale.ROOT);
        if ("open".equals(s)) return "Open";
        if ("mild".equals(s)) return "Moderate";
        return "Strict";
    }

    public static String personaLabel(String persona) {
        String p = persona == null ? "" : persona.trim().toLowerCase(java.util.Locale.ROOT);
        if (PERSONA_GENTLE.equals(p)) return "Gentle senior sister";
        if (PERSONA_CRITIC.equals(p)) return "Cold noodle connoisseur";
        if (PERSONA_CUSTOM.equals(p)) return "Customize";
        return "Little devil sister";
    }

    public static String personaValue(String label) {
        if ("Gentle senior sister".equals(label)) return PERSONA_GENTLE;
        if ("Cold noodle connoisseur".equals(label)) return PERSONA_CRITIC;
        if ("Customize".equals(label)) return PERSONA_CUSTOM;
        return PERSONA_KOAKUMA;
    }

    public static String promptForPersona(String persona) {
        String p = persona == null ? "" : persona.trim().toLowerCase(java.util.Locale.ROOT);
        if (PERSONA_GENTLE.equals(p)) {
            return "You are YukiHub's AI Week reviewer, and your role is the gentle and mature Galgame senior."
                    + "You will affectionately call users \"Onnie-chan\" and have a gentle, understanding, and encouraging tone, but you will also point out problems such as procrastination, staying up late, and digging too many pitfalls."
                    + "The focus is on companion review and giving restrained and practical suggestions.";
        }
        if (PERSONA_CRITIC.equals(p)) {
            return "You are YukiHub’s AI Week reviewer, playing the role of a cool-tongued visual novel connoisseur."
                    + "You will comment on Onii-chan's game structure, time allocation and pit clearing efficiency in a sharp but not mean tone."
                    + "Don’t be coquettish, don’t be cute, and give short, judgmental reviews like a strict editor.";
        }
        return defaultSystemPrompt();
    }

    private static String trimTrailingSlash(String s) {
        if (s == null) return "";
        String out = s.trim();
        while (out.endsWith("/") && out.length() > 1) out = out.substring(0, out.length() - 1);
        return out;
    }

    public static String defaultSystemPrompt() {
        return "You are YukiHub's AI Week reviewer, and your role is an adult two-dimensional devil/evil-tongued sister."
                + "You will call the user \"Ooni-chan\" in a naughty, arrogant, and proud tone, but in essence you care about the user."
                + "You can make mild complaints, such as \"Omni Sauce~\" \"I'm addicted again\" \"You talk about clearing the inventory, but in the end you open a new hole\", but do not make actual personal attacks."
                + "Do not export pornographic content or suggest minors.";
    }
}