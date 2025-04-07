package manager;

import dev.miku.r2dbc.mysql.MySqlConnectionConfiguration;
import dev.miku.r2dbc.mysql.MySqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


public class R2dbcHelper extends HelperBase {
    private ConnectionFactory madrasConnection;


    public R2dbcHelper(ApplicationManager manager) {
        super(manager);
        setMadrasConnection();
    }

    public void setMadrasConnection() {
        if (madrasConnection == null) {
            madrasConnection = MySqlConnectionFactory.from(MySqlConnectionConfiguration.builder()
                    .host("192.168.2.11") // Хост
                    .port(3306) // Порт
                    .database("dms") // Имя базы данных
                    .username("dms") // Имя пользователя
                    .password("dms") // Пароль
                    .build());

        }
    }

    public List<Integer> checkDocsInMadras(String appNumber) {
        String sql = "SELECT COUNT(*) AS Result\n" +
                    "FROM tph035_package t35 \n" +
                    "INNER JOIN tph014_document t14 ON t14.docpckkey = t35.pckkey \n" +
                    "LEFT JOIN tph013_docctl t13 ON t14.DOCDCTKEY = t13.DCTKEY \n" +
                    "LEFT JOIN tph016_docnote t16 ON t16.DNTDOCKEY = t14.DOCKEY\n" +
                    "WHERE t35.PCKORIAPPNUMBER = ? \n" +
                    "ORDER BY pckdateformal, pckseqnumber";

        Flux<Integer> resultFlux = Flux.from(madrasConnection.create())
                .flatMap(connection ->
                        Flux.from(connection.createStatement(sql)
                                        .bind(0, appNumber)
                                        .execute())
                                .flatMap(result ->
                                        result.map((row, rowMetadata) -> {
                                            int count = row.get("Result", Integer.class);
                                            return count;
                                        }))
                                .doFinally(signalType -> connection.close()));

        Mono<List<Integer>> resultListMono = resultFlux.collectList();

        // Блокируем выполнение и получаем результат
        List<Integer> resultList = resultListMono.block();
        return resultList;
    }
}

