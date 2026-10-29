# openpmo-plugin-indicator
Implementação de `IIndicatorProvider` para consultar os 17 Objetivos de Desenvolvimento Sustentável (ODS), desafios e indicadores estratégicos no Pentaho BI da SEP.

O provider executa `GET /pentaho/plugin/cda/api/doQuery` para as duas consultas:

| Método | `path` | `dataAccessId` | DTO |
| --- | --- | --- | --- |
| `getIndicators()` | `/public/dashboard/pmo/plugin/pmo_ods.cda` | `pmo_ods` | `IndicatorDto` |
| `getChallenges()` | `/public/dashboard/pmo/plugin/pmo_desafios.cda` | `pmo_desafios` | `ChallengeDto` |
| `getStrategicIndicators()` | `/public/dashboard/pmo/indicador_estrategico/pmo_indicadores.cda` | `pmo_indicadores` | `StrategicIndicatorDto` |
| `getStrategicIndicatorDetails(ids)` | `/public/dashboard/pmo/indicador_estrategico/pmo_indicadores_detalhes.cda` | `indicator_detail` | `StrategicIndicatorDetailDto` |

O retorno de desafios inclui gestão, grupo, subgrupo e desafio. Cada linha é preservada, mesmo quando um `desafioId` aparece em mais de uma gestão. Os valores `-1` e `"-"` também são mantidos. A leitura usa `metadata.colIndex`, tolera campos extras e corrige textos que chegaram com UTF-8 interpretado como ISO-8859-1.

O retorno de indicadores estratégicos inclui gestão, grupo, subgrupo e indicador. Cada linha do BI é preservada, os valores `-1` e `"-"` são mantidos e a leitura usa `metadata.colIndex`.

Os detalhes recebem os IDs em lote pelo parâmetro CDA `param_p_indicadorIds`, em lista separada por vírgulas. O retorno preserva uma linha por indicador/ano e inclui fonte, unidade de medida, ano/valor de referência, ano/valor da meta e polaridade original. Metas do projeto não são consultadas pelo plugin.

O Spring Boot registra o provider automaticamente por `META-INF/spring.factories`. A configuração fica em `indicator-parser.properties` e pode ser sobrescrita com as propriedades abaixo:

| Propriedade | Padrão |
| --- | --- |
| `pentaho-indicator.base-url` | `https://bi.sep.es.gov.br` |
| `pentaho-indicator.path` | `/public/dashboard/pmo/plugin/pmo_ods.cda` |
| `pentaho-indicator.data-access-id` | `pmo_ods` |
| `pentaho-indicator.challenges-path` | `/public/dashboard/pmo/plugin/pmo_desafios.cda` |
| `pentaho-indicator.challenges-data-access-id` | `pmo_desafios` |
| `pentaho-indicator.strategic-indicators-path` | `/public/dashboard/pmo/indicador_estrategico/pmo_indicadores.cda` |
| `pentaho-indicator.strategic-indicators-data-access-id` | `pmo_indicadores` |
| `pentaho-indicator.strategic-indicator-details-path` | `/public/dashboard/pmo/indicador_estrategico/pmo_indicadores_detalhes.cda` |
| `pentaho-indicator.strategic-indicator-details-data-access-id` | `indicator_detail` |
| `pentaho-indicator.user-id` | `PENTAHO_BI_USER_ID` ou `pentahoBI.userId` |
| `pentaho-indicator.password` | `PENTAHO_BI_PASSWORD` ou `pentahoBI.password` |

Se usuário e senha estiverem configurados, a requisição usa Basic Auth. Sem credenciais, consulta o endpoint como público. Configure sempre os dois valores juntos quando a instalação exigir autenticação.

## Build local

Com os dois projetos como pastas irmãs:

```powershell
.\gradlew.bat build --include-build ..\openpmo-plugin-indicator-interface
```

Depois da publicação do contrato, o plugin pode resolver `com.github.sep-es-br:openpmo-plugin-indicator-interface:1.2.0` via JitPack. O artefato desta implementação é `com.github.sep-es-br:openpmo-plugin-indicator:1.2.0`. Para resolver a versão `1.2.0` pelo JitPack, publique essa versão nos dois projetos.

## Desenvolvimento local com o OpenPMO API

Para consumir os dois clones locais sem publicar no JitPack, publique primeiro a interface no Maven local. Em seguida, construa e publique localmente a implementação com `-PlocalIndicatorPlugins`. No API, use o mesmo parâmetro para resolver somente os artefatos desta interface e implementação pelo Maven local. Sem o parâmetro, o API continua usando JitPack.

```powershell
# Na raiz de openpmo-plugin-indicator-interface
.\gradlew.bat publishToMavenLocal

# Na raiz de openpmo-plugin-indicator
.\gradlew.bat -PlocalIndicatorPlugins build publishToMavenLocal

# Na raiz de openpmo-api
.\gradlew.bat -PlocalIndicatorPlugins bootRun
```
