package br.gov.es.pmo.indicator_parser.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PentahoQueryResponse {

    private List<Column> metadata;
    private List<List<Object>> resultset;

    public List<Column> getMetadata() {
        return metadata == null ? Collections.emptyList() : metadata;
    }

    public void setMetadata(List<Column> metadata) {
        this.metadata = metadata;
    }

    public List<List<Object>> getResultset() {
        return resultset == null ? Collections.emptyList() : resultset;
    }

    public void setResultset(List<List<Object>> resultset) {
        this.resultset = resultset;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Column {
        private String colName;
        private Integer colIndex;

        public String getColName() { return colName; }
        public void setColName(String colName) { this.colName = colName; }
        public Integer getColIndex() { return colIndex; }
        public void setColIndex(Integer colIndex) { this.colIndex = colIndex; }
    }
}
