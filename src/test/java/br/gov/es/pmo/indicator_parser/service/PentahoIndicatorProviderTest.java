package br.gov.es.pmo.indicator_parser.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.gov.es.pmo.indicator_interface.model.ChallengeDto;
import br.gov.es.pmo.indicator_interface.model.IndicatorDto;
import br.gov.es.pmo.indicator_parser.model.PentahoQueryResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;

class PentahoIndicatorProviderTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void readsOdsColumnsByMetadataAndRepairsMojibake() throws Exception {
        String json = "{\"metadata\":["
            + "{\"colName\":\"nome\",\"colIndex\":0},"
            + "{\"colName\":\"ordem\",\"colIndex\":1},"
            + "{\"colName\":\"descricao\",\"colIndex\":2}],"
            + "\"resultset\":[[\"ErradicaÃ§Ã£o da pobreza\",1,"
            + "\"Erradicar a pobreza em todas as formas\"]],"
            + "\"queryInfo\":{\"totalRows\":\"1\"}}";

        List<IndicatorDto> indicators = PentahoIndicatorProvider.mapIndicators(
            mapper.readValue(json, PentahoQueryResponse.class));

        assertEquals(1, indicators.size());
        assertEquals(1, indicators.get(0).getOrder());
        assertEquals("Erradicação da pobreza", indicators.get(0).getName());
        assertEquals("Erradicar a pobreza em todas as formas", indicators.get(0).getDescription());
    }

    @Test
    void readsChallengesWithHierarchyAndKeepsRepeatedIds() throws Exception {
        String json = "{\"metadata\":["
            + "{\"colName\":\"gestaoId\",\"colIndex\":0},"
            + "{\"colName\":\"gestao\",\"colIndex\":1},"
            + "{\"colName\":\"grupoId\",\"colIndex\":2},"
            + "{\"colName\":\"grupoTipo\",\"colIndex\":3},"
            + "{\"colName\":\"grupo\",\"colIndex\":4},"
            + "{\"colName\":\"subgrupoId\",\"colIndex\":5},"
            + "{\"colName\":\"subgrupoTipo\",\"colIndex\":6},"
            + "{\"colName\":\"subgrupo\",\"colIndex\":7},"
            + "{\"colName\":\"desafioId\",\"colIndex\":8},"
            + "{\"colName\":\"desafio\",\"colIndex\":9}],"
            + "\"resultset\":["
            + "[-1,\"-\",2,\"Ãrea EstratÃ©gica\",\"Agricultura e Meio Ambiente\","
            + "-1,\"-\",\"-\",20,\"Apoiar oÂ desenvolvimento\"],"
            + "[1,\"GestÃ£o 2023-26\",743,\"Eixo\",\"Eixo 2\","
            + "14,\"Ãrea TemÃ¡tica\",\"Agricultura e Meio Ambiente\",20,"
            + "\"Apoiar o desenvolvimento\"]],"
            + "\"queryInfo\":{\"totalRows\":\"2\"}}";

        List<ChallengeDto> challenges = PentahoIndicatorProvider.mapChallenges(
            mapper.readValue(json, PentahoQueryResponse.class));

        assertEquals(2, challenges.size());
        assertEquals(-1, challenges.get(0).getManagementId());
        assertEquals("Área Estratégica", challenges.get(0).getGroupType());
        assertEquals("Apoiar o desenvolvimento", challenges.get(0).getChallenge());
        assertEquals(20, challenges.get(0).getChallengeId());
        assertEquals(20, challenges.get(1).getChallengeId());
        assertEquals(1, challenges.get(1).getManagementId());
        assertEquals("Área Temática", challenges.get(1).getSubgroupType());
    }
}
