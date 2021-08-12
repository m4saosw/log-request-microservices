package br.com.massao.logrequest.infrastructure.config;

import br.com.massao.logrequest.batch.LogRequestFieldSetMapper;
import br.com.massao.logrequest.application.dto.LogRequestForm;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
    @Autowired
    JobRepository jobRepository;

    /**
     * Defines a job launcher that runs in async way
     * @return
     * @throws Exception
     */
    @Bean(name = "myJobLauncher")
    public JobLauncher simpleJobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    /**
     * Job to run
     * @param jobBuilderFactory
     * @param stepBuilderFactory
     * @param reader
     * @param processor
     * @param writer
     * @return
     */
    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory,
                   StepBuilderFactory stepBuilderFactory,
                   ItemReader<LogRequestForm> reader,
                   ItemWriter<LogRequestForm> writer) {

        Step step = stepBuilderFactory.get("step1")
                .<LogRequestForm, LogRequestForm>chunk(100)
                .reader(reader)
                .writer(writer)
                .build();

        return jobBuilderFactory.get("importLogRequestJob")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    /**
     * File input reader
     *
     * @param pathFile
     * @return
     */
    @Bean
    @StepScope
    public FlatFileItemReader<LogRequestForm> itemReader(@Value("#{jobParameters[pathFile]}") String pathFile) {

        return new FlatFileItemReaderBuilder()
                .resource((new FileSystemResource(pathFile)))
                .name("reader")
                .lineMapper(lineMapper())
                .build();

    }

    /**
     * Attributes row mapping
     *
     * @return
     */
    @Bean
    public LineMapper<LogRequestForm> lineMapper() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter("|");
        tokenizer.setStrict(false);
        tokenizer.setNames(new String[]{"data", "ip", "request", "status", "userAgent"});

        DefaultLineMapper<LogRequestForm> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new LogRequestFieldSetMapper());

        return lineMapper;
    }

}
