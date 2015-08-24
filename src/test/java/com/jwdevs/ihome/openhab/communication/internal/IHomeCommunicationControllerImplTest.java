package com.jwdevs.ihome.openhab.communication.internal;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.jwdevs.ihome.openhab.message.HeaterMessage;
import com.jwdevs.ihome.openhab.message.PirMessage;
import com.jwdevs.ihome.openhab.message.MessageListener;
import com.jwdevs.ihome.openhab.message.ThermometerMessage;

public class IHomeCommunicationControllerImplTest {

    @Test
    public void testBrokenStart() {
        IHomeCommunicationControllerImpl controller = new IHomeCommunicationControllerImpl();
        MessageListener listener = mock(MessageListener.class);
        controller.registerListener(listener);

        controller.processData("12;23}12{10;0;0}{12;3");

        verify(listener).onHeaterMessage(isA(HeaterMessage.class));

    }

    @Test
    public void testScrambledTokens() {
        IHomeCommunicationControllerImpl controller = new IHomeCommunicationControllerImpl();
        MessageListener listener = mock(MessageListener.class);
        controller.registerListener(listener);

        controller.processData("12;23}{12{10;0;0}{12;3");

        verify(listener).onHeaterMessage(isA(HeaterMessage.class));

    }

    @Test
    public void testCumulativeData() {
        IHomeCommunicationControllerImpl controller = new IHomeCommunicationControllerImpl();
        MessageListener listener = mock(MessageListener.class);
        controller.registerListener(listener);

        controller.processData("12;23}12{10;0;0}{1;0");
        controller.processData("12;12}{1210;0;0}{1;0");

        verify(listener).onHeaterMessage(isA(HeaterMessage.class));
        verify(listener).onThermometerMessage(isA(ThermometerMessage.class));

    }

    @Test
    public void testCumulativeData2() {
        IHomeCommunicationControllerImpl controller = new IHomeCommunicationControllerImpl();
        MessageListener listener = mock(MessageListener.class);
        controller.registerListener(listener);

        controller.processData("12;23}}}{{{12{10;0;0}}}{1;0");
        controller.processData("12}{{{1210;0;0}}}{1;0");
        controller.processData("12;20}{{{1210;0;0}}}{3;0}{1;0");

        verify(listener).onHeaterMessage(isA(HeaterMessage.class));
        verify(listener).onThermometerMessage(isA(ThermometerMessage.class));
        verify(listener).onPirMessage(isA(PirMessage.class));

    }
}
