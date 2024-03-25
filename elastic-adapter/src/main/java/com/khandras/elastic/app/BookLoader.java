package com.khandras.elastic.app;

import com.khandras.elastic.adapter.ElasticAdapter;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookLoader {
    private final ElasticAdapter elasticAdapter;

    @PostConstruct
    public void loadBook() throws IOException {
        try(DataInputStream in = new DataInputStream(BookLoader.class.getResourceAsStream("/bloh_effective_java.txt"))) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));


            var executor = Executors.newFixedThreadPool(4);

            var tasks = br.lines()
                    .parallel()
                    .map(line -> CompletableFuture.runAsync(() -> loadRaw(line), executor))
                    .toArray(CompletableFuture[]::new);

            log.info("Tasks setted for execution");
            var fin = CompletableFuture.allOf(tasks);
            log.info("end middle before get");
            fin.get(30, TimeUnit.MINUTES);
            log.info("end all tasks");
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadRaw(String raw) {
        try {
            elasticAdapter.sendMessage(raw, "Joshua Bloch", "2018", "Effective Java. Third Edition");
        } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }
}
