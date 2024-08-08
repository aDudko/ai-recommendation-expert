package org.adudko.project.bootstrap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.adudko.project.config.VectorStoreProperties;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class LoadVectorStore implements CommandLineRunner {

    private final VectorStore vectorStore;
    private final VectorStoreProperties vectorStoreProperties;


    @Override
    public void run(String... args) throws Exception {
        if (vectorStore.similaritySearch("job-company").isEmpty()) {
            log.debug("Loading documents into vector store");
            vectorStoreProperties
                    .getDocumentsToLoad()
                    .forEach(document -> {
                        log.info("Loading document: " + document.getFilename());
                        TikaDocumentReader documentReader = new TikaDocumentReader(document);
                        List<Document> documents = documentReader.get();
                        TextSplitter textSplitter = new TokenTextSplitter();
                        List<Document> splitDocuments = textSplitter.apply(documents);
                        vectorStore.add(splitDocuments);
                    });
        }
        log.debug("Vector store loaded");
    }

}
