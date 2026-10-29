package br.gov.es.pmo.indicator_parser.service;

import br.gov.es.pmo.indicator_interface.model.IIndicatorProvider;
import br.gov.es.pmo.indicator_interface.model.ChallengeDto;
import br.gov.es.pmo.indicator_interface.model.StrategicIndicatorDto;
import br.gov.es.pmo.indicator_interface.model.IndicatorDto;
import br.gov.es.pmo.indicator_interface.model.StrategicIndicatorDetailDto;
import br.gov.es.pmo.indicator_parser.model.PentahoQueryResponse;
import br.gov.es.pmo.indicator_parser.properties.PentahoIndicatorProperties;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.HtmlUtils;

@Component
public class PentahoIndicatorProvider implements IIndicatorProvider {

    private static final String CDA_ENDPOINT = "/pentaho/plugin/cda/api/doQuery";

    private final PentahoIndicatorProperties properties;
    private final Environment environment;
    private final WebClient webClient;

    public PentahoIndicatorProvider(PentahoIndicatorProperties properties, Environment environment) {
        this.properties = properties;
        this.environment = environment;
        this.webClient = WebClient.builder().baseUrl(properties.getBaseUrl()).build();
    }

    @Override
    public List<IndicatorDto> getIndicators() {
        return mapIndicators(query(
            properties.getPath(), properties.getDataAccessId(), "indicadores"));
    }

    static List<IndicatorDto> mapIndicators(PentahoQueryResponse response) {
        int orderIndex = columnIndex(response, "ordem", 0);
        int descriptionIndex = columnIndex(response, "descricao", 1);
        int nameIndex = columnIndex(response, "nome", 2);
        return response.getResultset().stream()
            .map(row -> new IndicatorDto(
                integer(row, orderIndex),
                string(row, descriptionIndex),
                string(row, nameIndex)))
            .collect(Collectors.toList());
    }

    @Override
    public List<ChallengeDto> getChallenges() {
        return mapChallenges(query(
            properties.getChallengesPath(), properties.getChallengesDataAccessId(), "desafios"));
    }

    static List<ChallengeDto> mapChallenges(PentahoQueryResponse response) {
        int managementId = columnIndex(response, "gestaoId", 0);
        int management = columnIndex(response, "gestao", 1);
        int groupId = columnIndex(response, "grupoId", 2);
        int groupType = columnIndex(response, "grupoTipo", 3);
        int group = columnIndex(response, "grupo", 4);
        int subgroupId = columnIndex(response, "subgrupoId", 5);
        int subgroupType = columnIndex(response, "subgrupoTipo", 6);
        int subgroup = columnIndex(response, "subgrupo", 7);
        int challengeId = columnIndex(response, "desafioId", 8);
        int challenge = columnIndex(response, "desafio", 9);

        return response.getResultset().stream()
            .map(row -> new ChallengeDto(
                integer(row, managementId),
                string(row, management),
                integer(row, groupId),
                string(row, groupType),
                string(row, group),
                integer(row, subgroupId),
                string(row, subgroupType),
                string(row, subgroup),
                integer(row, challengeId),
                string(row, challenge)))
            .collect(Collectors.toList());
    }

    @Override
    public List<StrategicIndicatorDto> getStrategicIndicators() {
        return mapStrategicIndicators(query(
            properties.getStrategicIndicatorsPath(),
            properties.getStrategicIndicatorsDataAccessId(),
            "indicadores estratégicos"));
    }

    @Override
    public List<StrategicIndicatorDetailDto> getStrategicIndicatorDetails(List<Integer> indicatorIds) {
        if (indicatorIds == null || indicatorIds.isEmpty()) {
            return Collections.emptyList();
        }
        final String ids = indicatorIds.stream()
            .filter(id -> id != null)
            .distinct()
            .map(String::valueOf)
            .collect(Collectors.joining(","));
        if (!StringUtils.hasText(ids)) {
            return Collections.emptyList();
        }

        return mapStrategicIndicatorDetails(query(
            properties.getStrategicIndicatorDetailsPath(),
            properties.getStrategicIndicatorDetailsDataAccessId(),
            "detalhes dos indicadores estratégicos",
            "paramp_indicadorIds",
            ids));
    }

    static List<StrategicIndicatorDetailDto> mapStrategicIndicatorDetails(PentahoQueryResponse response) {
        int indicatorId = columnIndex(response, "indicadorId", 0);
        int indicator = columnIndex(response, "indicador", 1);
        int source = columnIndex(response, "fonte", 2);
        int unitOfMeasure = columnIndex(response, "unidadeMedida", 3);
        int referenceYear = columnIndex(response, "anoBase", 4);
        int referenceValue = columnIndex(response, "valorBase", 5);
        int goalYear = columnIndex(response, "anoMeta", 6);
        int goalValue = columnIndex(response, "valorMeta", 7);
        int polarity = columnIndex(response, "polaridade", 8);

        return response.getResultset().stream()
            .map(row -> new StrategicIndicatorDetailDto(
                nullableInteger(row, indicatorId),
                string(row, indicator),
                string(row, source),
                string(row, unitOfMeasure),
                nullableInteger(row, referenceYear),
                decimal(row, referenceValue),
                nullableInteger(row, goalYear),
                decimal(row, goalValue),
                string(row, polarity)))
            .collect(Collectors.toList());
    }

    static List<StrategicIndicatorDto> mapStrategicIndicators(PentahoQueryResponse response) {
        int managementId = columnIndex(response, "gestaoId", 0);
        int management = columnIndex(response, "gestao", 1);
        int groupId = columnIndex(response, "grupoId", 2);
        int groupType = columnIndex(response, "grupoTipo", 3);
        int group = columnIndex(response, "grupo", 4);
        int subgroupId = columnIndex(response, "subgrupoId", 5);
        int subgroupType = columnIndex(response, "subgrupoTipo", 6);
        int subgroup = columnIndex(response, "subgrupo", 7);
        int indicatorId = columnIndex(response, "indicadorId", 8);
        int indicator = columnIndex(response, "indicador", 9);

        return response.getResultset().stream()
            .map(row -> new StrategicIndicatorDto(
                integer(row, managementId),
                string(row, management),
                integer(row, groupId),
                string(row, groupType),
                string(row, group),
                integer(row, subgroupId),
                string(row, subgroupType),
                string(row, subgroup),
                integer(row, indicatorId),
                string(row, indicator)))
            .collect(Collectors.toList());
    }

    private PentahoQueryResponse query(String path, String dataAccessId, String label) {
        return query(path, dataAccessId, label, null, null);
    }

    private PentahoQueryResponse query(
        String path,
        String dataAccessId,
        String label,
        String parameterName,
        String parameterValue
    ) {
        validateCredentials();
        PentahoQueryResponse response = webClient.get()
            .uri(builder -> {
                builder.path(CDA_ENDPOINT)
                    .queryParam("path", path)
                    .queryParam("dataAccessId", dataAccessId);
                if (StringUtils.hasText(parameterName) && parameterValue != null) {
                    builder.queryParam(parameterName, parameterValue);
                }
                return builder.build();
            })
            .headers(this::setBasicAuthIfConfigured)
            .retrieve()
            .bodyToMono(PentahoQueryResponse.class)
            .block();

        if (response == null) {
            throw new IllegalStateException("Resposta vazia do Pentaho para " + label);
        }
        return response;
    }

    private static int columnIndex(PentahoQueryResponse response, String name, int fallback) {
        return response.getMetadata().stream()
            .filter(column -> name.equalsIgnoreCase(column.getColName()))
            .map(PentahoQueryResponse.Column::getColIndex)
            .filter(index -> index != null && index >= 0)
            .findFirst()
            .orElse(fallback);
    }

    private static Integer integer(List<Object> row, int index) {
        Object value = cell(row, index);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return Integer.valueOf(String.valueOf(value));
    }

    private static Integer nullableInteger(List<Object> row, int index) {
        Object value = cell(row, index);
        if (value == null || !StringUtils.hasText(String.valueOf(value))) {
            return null;
        }
        return value instanceof Number
            ? ((Number) value).intValue()
            : Integer.valueOf(String.valueOf(value));
    }

    private static BigDecimal decimal(List<Object> row, int index) {
        Object value = cell(row, index);
        if (value == null || !StringUtils.hasText(String.valueOf(value))) {
            return null;
        }
        return value instanceof BigDecimal
            ? (BigDecimal) value
            : new BigDecimal(String.valueOf(value));
    }

    private static String string(List<Object> row, int index) {
        Object value = cell(row, index);
        return value == null ? null : repairText(HtmlUtils.htmlUnescape(String.valueOf(value)));
    }

    private static Object cell(List<Object> row, int index) {
        if (row == null || index >= row.size()) {
            throw new IllegalStateException("Linha incompleta no retorno do Pentaho");
        }
        return row.get(index);
    }

    // Some CDA responses contain UTF-8 bytes previously interpreted as ISO-8859-1.
    private static String repairText(String value) {
        value = value.replace("Â ", " ");
        if (!value.contains("Ã") && !value.contains("Â")) {
            return value.replace('\u00A0', ' ');
        }
        try {
            return StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT)
                .decode(ByteBuffer.wrap(value.getBytes(StandardCharsets.ISO_8859_1)))
                .toString().replace('\u00A0', ' ');
        } catch (CharacterCodingException ignored) {
            return value.replace('\u00A0', ' ');
        }
    }

    private void validateCredentials() {
        if (StringUtils.hasText(userId()) != StringUtils.hasText(password())) {
            throw new IllegalStateException("Configure usuário e senha do Pentaho juntos");
        }
    }

    private void setBasicAuthIfConfigured(HttpHeaders headers) {
        if (StringUtils.hasText(userId())) {
            headers.setBasicAuth(userId(), password());
        }
    }

    private String userId() {
        return StringUtils.hasText(properties.getUserId())
            ? properties.getUserId()
            : environment.getProperty("pentahoBI.userId");
    }

    private String password() {
        return StringUtils.hasText(properties.getPassword())
            ? properties.getPassword()
            : environment.getProperty("pentahoBI.password");
    }
}
