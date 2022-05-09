import br.ce.wcaquino.consumer.tasks.model.Task;
import br.ce.wcaquino.consumer.tasks.service.TasksConsumer;
import br.ce.wcaquino.consumer.utils.RequestHelper;
import org.apache.http.client.ClientProtocolException;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.BDDMockito.then;

@RunWith(MockitoJUnitRunner.class)
public class TasksConsumerTest {

    private static final String INVALID_URL = "http://invalidURL.com";

    @InjectMocks
    private TasksConsumer consumer = new TasksConsumer(INVALID_URL);

    //mockando
    @Mock
    private RequestHelper helper;

    @Test
    public  void shoulGetAndExistingTask() throws ClientProtocolException, IOException {

        //Arrange -> configurar o que é necessário para o teste
        //ibjeto que vai retornar
        Map<String, String> expectedTask = new HashMap<String, String>();
        //colocar no map todos os valores que quero que retorne, nisso farei um put para inserir no map um conjunto de chave , valor
        expectedTask.put("id","1");
        expectedTask.put("task","TaskMocked!");
        expectedTask.put("dueDate","2000-01-01");

        //when > quando uma determinada ação ocorrer então deve fazer outra coisa. Quando o metodo helper. ocorrer com determinado parametro,
        //o parametro é o que definimos na linha 23 + as concatenações. "then" então deve no nosso caso retornal null
        Mockito
                .when(helper.get(INVALID_URL + "/todo/1"))
                .thenReturn(expectedTask); //retorna objeto moclado linha 36,38.39.40

        //Act -> Execução da tarefa que queremos testar
        Task task = consumer.getTask(1L);

        // Assert -> Realmente ocorreu como era esperado?
        Assert.assertNotNull(task);
        Assert.assertThat(task.getId(), CoreMatchers.is(1L));

    }

}
