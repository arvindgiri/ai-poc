package com.arv.adk.tools;

import com.arv.framework.adk.interfaces.tool.ITool;
import com.arv.framework.adk.interfaces.tool.IToolInput;
import com.arv.framework.adk.interfaces.tool.IToolMetadata;
import com.arv.framework.adk.interfaces.tool.IToolResult;
import com.arv.framework.adk.interfaces.enums.ToolCategory;
import com.arv.framework.adk.interfaces.enums.ReturnType;
import com.arv.framework.adk.tools.ToolMetadata;
import com.arv.framework.adk.tools.Parameter;
import com.arv.framework.adk.tools.ToolResult;
import lombok.extern.slf4j.Slf4j;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tool for searching Wikipedia articles and extracting information.
 * Can search for topics and extract specific data like population figures.
 */
@Slf4j
public class WikipediaSearchTool implements ITool {

  private static final String WIKIPEDIA_API_URL = "https://en.wikipedia.org/api/rest_v1/page/summary/";
  private final OkHttpClient httpClient;
  private final ObjectMapper objectMapper;

  public WikipediaSearchTool() {
    this.httpClient = new OkHttpClient();
    this.objectMapper = new ObjectMapper();
  }

  @Override
  public String getId() {
    return "wikipedia_search";
  }

  @Override
  public String getName() {
    return "Wikipedia Search";
  }

  @Override
  public String getDescription() {
    return "Search Wikipedia for information about countries, cities, or topics. Can extract population data and other facts.";
  }

  @Override
  public IToolMetadata getMetadata() {
    ToolMetadata meta = new ToolMetadata(getId(), getName(), getDescription());
    meta.setCategory(ToolCategory.UTILITY);
    meta.setReturnType(ReturnType.STRING);
    meta.setVersion("1.0.0");
    
    // Add query parameter
    Parameter queryParam = new Parameter(
        "query", 
        "string", 
        "Search term or topic (e.g., 'India', 'United States', 'Tokyo')", 
        true
    );
    meta.addParameter(queryParam);
    
    return meta;
  }

  @Override
  public IToolResult execute(IToolInput input) {
    log.info("Executing Wikipedia search tool");
    
    try {
      // Get the search query parameter
      String query = (String) input.getParameter("query");
      if (query == null || query.trim().isEmpty()) {
        return ToolResult.failure("Query parameter is required");
      }

      log.debug("Searching Wikipedia for: {}", query);
      
      // Clean and format the query for Wikipedia API
      String cleanQuery = query.trim().replace(" ", "_");
      String url = WIKIPEDIA_API_URL + cleanQuery;
      
      Request request = new Request.Builder()
          .url(url)
          .addHeader("User-Agent", "ConnectADK/1.0 (Educational Purpose)")
          .build();

      try (Response response = httpClient.newCall(request).execute()) {
        if (!response.isSuccessful()) {
          log.warn("Wikipedia API returned status: {}", response.code());
          return ToolResult.failure("Wikipedia search failed: " + response.code());
        }

        String responseBody = response.body().string();
        JsonNode jsonResponse = objectMapper.readTree(responseBody);
        
        // Extract key information
        String title = jsonResponse.has("title") ? jsonResponse.get("title").asText() : "Unknown";
        String extract = jsonResponse.has("extract") ? jsonResponse.get("extract").asText() : "";
        
        log.debug("Found Wikipedia article: {}", title);
        
        // Try to extract population if mentioned in the extract
        String populationInfo = extractPopulationInfo(extract);
        
        StringBuilder result = new StringBuilder();
        result.append("Wikipedia Summary for ").append(title).append(":\n\n");
        result.append(extract);
        
        if (!populationInfo.isEmpty()) {
          result.append("\n\nPopulation Information: ").append(populationInfo);
        }
        
        return ToolResult.success(result.toString());
        
      }
      
    } catch (IOException e) {
      log.error("Error calling Wikipedia API", e);
        return ToolResult.failure("Failed to search Wikipedia: " + e.getMessage());
    } catch (Exception e) {
      log.error("Unexpected error in Wikipedia search", e);
        return ToolResult.failure("Unexpected error: " + e.getMessage());
    }
  }

  /**
   * Extract population information from Wikipedia text using regex patterns.
   */
  private String extractPopulationInfo(String text) {
    // Common patterns for population in Wikipedia articles
    Pattern[] patterns = {
        Pattern.compile("population of ([\\d,\\.\\s]+(?:million|billion|thousand))", Pattern.CASE_INSENSITIVE),
        Pattern.compile("([\\d,\\.\\s]+(?:million|billion|thousand)) people", Pattern.CASE_INSENSITIVE),
        Pattern.compile("home to ([\\d,\\.\\s]+(?:million|billion|thousand))", Pattern.CASE_INSENSITIVE),
        Pattern.compile("([\\d,\\.\\s]+(?:million|billion|thousand)) inhabitants", Pattern.CASE_INSENSITIVE),
        Pattern.compile("population.*?([\\d,\\.]+(?:\\s*million|\\s*billion))", Pattern.CASE_INSENSITIVE)
    };

    for (Pattern pattern : patterns) {
      Matcher matcher = pattern.matcher(text);
      if (matcher.find()) {
        String populationText = matcher.group(1);
        log.debug("Extracted population info: {}", populationText);
        return populationText.trim();
      }
    }

    return "";
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public void setEnabled(boolean enabled) {
    // Implementation for enable/disable functionality
  }

  @Override
  public boolean validateInput(IToolInput input) {
    return input != null && input.getParameter("query") != null;
  }
}
