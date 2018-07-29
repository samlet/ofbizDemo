package com.sagas.ofbizdemo.events;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.StaticDataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;
public class GraphQLManager {
    // static variable graphQLManager of type Singleton
    private static GraphQLManager graphQLManager = null;

    public GraphQL builder;

    // private constructor restricted to this class itself
    private GraphQLManager()
    {
        String schema = "type Query{hello: String}";

        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);

        RuntimeWiring runtimeWiring = newRuntimeWiring()
                .type("Query", builder ->
                        builder.dataFetcher("hello", new StaticDataFetcher("world")))
                .build();

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);

        builder = GraphQL.newGraphQL(graphQLSchema).build();
    }

    // static method to create instance of Singleton class
    public static GraphQLManager getInstance()
    {
        if (graphQLManager == null)
            graphQLManager = new GraphQLManager();

        return graphQLManager;
    }

    public ExecutionResult execute(String queryString){
        ExecutionResult executionResult = builder.execute(queryString); // "{hello}"
        // System.out.println(executionResult.getData().toString()); // Prints: {hello=world}
        return executionResult;
    }
}
