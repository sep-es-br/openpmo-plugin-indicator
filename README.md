# openpmo-plugin-indicator

Implementação de `IIndicatorProvider` para consultar os 17 Objetivos de Desenvolvimento Sustentável (ODS) e os desafios estratégicos no Pentaho BI da SEP.

O provider executa `GET /pentaho/plugin/cda/api/doQuery` para as duas consultas:

| Método | `path` | `dataAccessId` | DTO |
| --- | --- | --- | --- |
| `getIndicators()` | `/public/dashboard/pmo/plugin/pmo_ods.cda` | `pmo_ods` | `IndicatorDto` |
| `getChallenges()` | `/public/dashboard/pmo/plugin/pmo_desafios.cda` | `pmo_desafios` | `ChallengeDto` |

O retorno de desafios inclui gestão, grupo, subgrupo e desafio. Cada linha é preservada, mesmo quando um `desafioId` aparece em mais de uma gestão. Os valores `-1` e `"-"` também são mantidos. A leitura usa `metadata.colIndex`, tolera campos extras e corrige textos que chegaram com UTF-8 interpretado como ISO-8859-1.

O Spring Boot registra o provider automaticamente por `META-INF/spring.factories`. A configuração fica em `indicator-parser.properties` e pode ser sobrescrita com as propriedades abaixo:

| Propriedade | Padrão |
| --- | --- |
| `pentaho-indicator.base-url` | `https://bi.sep.es.gov.br` |
| `pentaho-indicator.path` | `/public/dashboard/pmo/plugin/pmo_ods.cda` |
| `pentaho-indicator.data-access-id` | `pmo_ods` |
| `pentaho-indicator.challenges-path` | `/public/dashboard/pmo/plugin/pmo_desafios.cda` |
| `pentaho-indicator.challenges-data-access-id` | `pmo_desafios` |
| `pentaho-indicator.user-id` | `PENTAHO_BI_USER_ID` ou `pentahoBI.userId` |
| `pentaho-indicator.password` | `PENTAHO_BI_PASSWORD` ou `pentahoBI.password` |

Se usuário e senha estiverem configurados, a requisição usa Basic Auth. Sem credenciais, consulta o endpoint como público. Configure sempre os dois valores juntos quando a instalação exigir autenticação.

## Build local

Com os dois projetos como pastas irmãs:

```powershell
.\gradlew.bat build --include-build ..\openpmo-plugin-indicator-interface
```

Depois da publicação do contrato, o plugin pode resolver `com.github.sep-es-br:openpmo-plugin-indicator-interface:1.0.0` via JitPack. O artefato desta implementação é `com.github.sep-es-br:openpmo-plugin-indicator:1.0.0`. Os repositórios Git já foram criados; para resolver a versão `1.0.0` pelo JitPack, publique essa versão nos dois projetos.
