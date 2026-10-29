package br.gov.es.pmo.indicator_parser.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pentaho-indicator")
public class PentahoIndicatorProperties {

    private String baseUrl;
    private String userId;
    private String password;
    private String path;
    private String dataAccessId;
    private String challengesPath;
    private String challengesDataAccessId;
    private String strategicIndicatorsPath;
    private String strategicIndicatorsDataAccessId;
    private String strategicIndicatorDetailsPath;
    private String strategicIndicatorDetailsDataAccessId;

    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    public String getDataAccessId() { return dataAccessId; }
    public void setDataAccessId(String dataAccessId) { this.dataAccessId = dataAccessId; }
    public String getChallengesPath() { return challengesPath; }
    public void setChallengesPath(String challengesPath) { this.challengesPath = challengesPath; }
    public String getChallengesDataAccessId() { return challengesDataAccessId; }
    public void setChallengesDataAccessId(String challengesDataAccessId) { this.challengesDataAccessId = challengesDataAccessId; }
    public String getStrategicIndicatorsPath() { return strategicIndicatorsPath; }
    public void setStrategicIndicatorsPath(String strategicIndicatorsPath) { this.strategicIndicatorsPath = strategicIndicatorsPath; }
    public String getStrategicIndicatorsDataAccessId() { return strategicIndicatorsDataAccessId; }
    public void setStrategicIndicatorsDataAccessId(String strategicIndicatorsDataAccessId) { this.strategicIndicatorsDataAccessId = strategicIndicatorsDataAccessId; }
    public String getStrategicIndicatorDetailsPath() { return strategicIndicatorDetailsPath; }
    public void setStrategicIndicatorDetailsPath(String strategicIndicatorDetailsPath) { this.strategicIndicatorDetailsPath = strategicIndicatorDetailsPath; }
    public String getStrategicIndicatorDetailsDataAccessId() { return strategicIndicatorDetailsDataAccessId; }
    public void setStrategicIndicatorDetailsDataAccessId(String strategicIndicatorDetailsDataAccessId) { this.strategicIndicatorDetailsDataAccessId = strategicIndicatorDetailsDataAccessId; }
}
