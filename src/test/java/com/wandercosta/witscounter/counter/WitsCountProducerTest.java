package com.wandercosta.witscounter.counter;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wandercosta.witscounter.connection.TCPClient;
import java.io.IOException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Class to test WitsCountProducer.
 *
 * @author Wander Costa (www.wandercosta.com)
 */
@RunWith(MockitoJUnitRunner.class)
public class WitsCountProducerTest {

    private static final String NULL_CLIENT = "TCPClient must be provided.";
    private static final String NULL_MATRIX = "WitsCountMatrix must be provided.";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private TCPClient client;

    @Mock
    private WitsMatrixCounter matrix;

    @Test
    public void shouldStartRunTwiceAndStop() throws IOException, InterruptedException {
        String data = "mocked result";
        when(client.readLine()).thenReturn(data);

        WitsCountProducer producer = new WitsCountProducer(client, matrix);
        producer.start();
        Thread.sleep(50);
        producer.stop();
        Thread.sleep(50);

        verify(client).connect();
        verify(client).disconnect();
        verify(client, atLeastOnce()).readLine();
        verify(matrix, atLeastOnce()).add(data);
    }

    @Test
    public void shouldStartAndStopTwiceAndNotAffectResult()
            throws IOException, InterruptedException {
        String data = "mocked result";
        when(client.readLine()).thenReturn(data);

        WitsCountProducer producer = new WitsCountProducer(client, matrix);
        producer.start();
        producer.start();
        Thread.sleep(50);
        producer.stop();
        producer.stop();
        Thread.sleep(50);

        verify(client).connect();
        verify(client).disconnect();
        verify(client, atLeastOnce()).readLine();
        verify(matrix, atLeastOnce()).add(data);
    }

    @Test
    public void shouldFailToReadFromSocket() throws IOException, InterruptedException {
        when(client.readLine()).thenThrow(new IOException("mocked exception"));

        WitsCountProducer producer = new WitsCountProducer(client, matrix);
        producer.start();
        producer.start();
        Thread.sleep(50);
        producer.stop();
        producer.stop();
        Thread.sleep(50);

        verify(client).connect();
        verify(client).disconnect();
        verify(client, atLeastOnce()).readLine();
    }

    @Test
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void shouldInstantiateWithNullClient() {
        exception.expect(NullPointerException.class);
        exception.expectMessage(NULL_CLIENT);
        new WitsCountProducer(null, matrix);
    }

    @Test
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void shouldInstantiateWithNullMatrix() {
        exception.expect(NullPointerException.class);
        exception.expectMessage(NULL_MATRIX);
        new WitsCountProducer(client, null);
    }

}
