package pact;

import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.PactProviderRule;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import br.ce.wcaquino.consumer.tasks.model.Task;
import br.ce.wcaquino.consumer.tasks.service.TasksConsumer;
import org.apache.http.client.ClientProtocolException;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;

public class TaskConsumerContractTest {

    //rules do JUnit
    //isso vai criar a instância mockapi. Abaixo estamos tentando acessar a api tasks. Abaixo lado do servidor e embutido no codigotem a
    //inteligência para criar a api mockada
    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("Tasks",this);
    // PactProviderRule mockProvider = new PactProviderRule("Tasks","localhost", 8080 , this); outra forma

    //expectativa que a api mockada deve ter, vou dizer como pretendo interagir com ela e o que deve fazer quando fizer a interação
    //no final será gerado um contrato entre basic consumer e tasks (linha 13)
    @Pact(consumer = "BasicConsumer")
    // metodo que vai configurar essa espectativa
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        return builder
                .given("There is a task with id = 1") //onde informo minhas pre condições, o que preciso ter para que o teste seja executado, funcione
                .uponReceiving("Retrieve Task #1") //quando acontecer determinada ação, faça isso (quando vier um get na task de id 1
                        .path("/todo/1") //o caminho que ele vai buscar na api será este
                        .method("GET") //utilizando GET
                .willRespondWith() //quero que me responda desta forma
                        .status(200) //vai responder um status code 200
                        .body("{\"id\": 1, \"task\": \"Task from pact\", \"dueDate\": \"2020-01-01\"}") //tambem vai trazer o copro do json
                .toPact(); //builder finaiza convertendo tudo isso para um pacto
    }

    @Test
    @PactVerification //com isso sabe que precisa fazer verificação neste ponto
    public void test() throws ClientProtocolException, IOException {
        //Arrange
        TasksConsumer consumer = new TasksConsumer(mockProvider.getUrl());
        System.out.println(mockProvider.getUrl());
        //Act
        Task task = consumer.getTask(1L);//passamos o id 1 como esperado no pacto
        System.out.println(task);
        //Assert
        assertThat(task.getId(), is(1L));
        assertThat(task.getTask(), is("Task from pact"));
    }

}

