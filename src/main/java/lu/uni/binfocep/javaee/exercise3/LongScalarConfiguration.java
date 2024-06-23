package lu.uni.binfocep.javaee.exercise3;

import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;



@Configuration
public class LongScalarConfiguration {

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder.scalar(longScalar());
    }

    private GraphQLScalarType longScalar() {
        return GraphQLScalarType.newScalar()
                .name("Long")
                .description("A custom scalar that handles Long values")
                .coercing(new Coercing<Long, Long>() {
                    @Override
                    public Long serialize(Object dataFetcherResult) throws CoercingSerializeException {
                        if (dataFetcherResult instanceof Long) {
                            return (Long) dataFetcherResult;
                        }
                        throw new CoercingSerializeException("Expected a Long object.");
                    }

                    @Override
                    public Long parseValue(Object input) throws CoercingParseValueException {
                        if (input instanceof Long) {
                            return (Long) input;
                        }
                        throw new CoercingParseValueException("Expected a Long object.");
                    }

                    @Override
                    public Long parseLiteral(Object input) throws CoercingParseLiteralException {
                        if (input instanceof Long) {
                            return (Long) input;
                        }
                        throw new CoercingParseLiteralException("Expected a Long object.");
                    }
                })
                .build();
    }
}
